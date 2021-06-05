package de.tesd.collection;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.Nullable;

import de.tesd.util.KeyNotFoundException;
import de.tesd.util.O;
import de.tesd.util.function.ThrowingSupplier;


public class TemporaryHashMap<K, V> {
	
	
	/** 10 Minuten Standard-Vergesslichkeit */
	public static final long DEFAULT_MILLIS_TO_KEEP = 600000;
	public static final Logger LOG = Logger.getLogger(TemporaryHashMap.class.getName());
	
	private static final Timer timer = new Timer("TemporaryHashMapTimer");
	
	
	public static interface RemoveNotification<K, V> {
		void onRemove(TemporaryHashMap<K, V> sender, K key, V value);
	}

	
		// ========== Entry ==========
	
	
	static class Entry<K, V> implements Comparable<Entry<K, V>> {
		
		
		K key;
		V value;
		long millisToKeep;
		Date deadline;
		
		
		public Entry(K key, V value, long millisToKeep, @Nullable Date deadline) {
			this.key = key;
			this.value = value;
			this.millisToKeep = millisToKeep;
			this.deadline = deadline!=null ? deadline : new Date(System.currentTimeMillis()+millisToKeep); 
		}
		
		
		public K getKey() {
			return key;
		}
		
		
		public V getValue() {
			return value;
		}

		
		public void set(V value, long millisToKeep, @Nullable Date deadline) {
			this.value = value;
			this.millisToKeep = millisToKeep;
			this.deadline = deadline!=null ? deadline : millisToKeep>0L ? new Date(System.currentTimeMillis()+millisToKeep) : this.deadline; 
		}

		
		public void update() {
			if (millisToKeep>0L)
				this.deadline = new Date(System.currentTimeMillis()+millisToKeep); 
		}
	
	
			// ========== Comparable ==========
		
		
		@Override public int compareTo(Entry<K, V> o) {
			return deadline.compareTo(o.deadline);
		}
		
		
			// ========== Object ==========
		
		
		@Override public String toString() {
			return getKey()+" -> "+getValue()+" ("+deadline+")";
		}


	}
	
	
		// ========== TemporaryHashMap ==========
	
	
	public final Comparator<Entry<K, V>> COMPARATOR = new Comparator<Entry<K, V>>() {
		@Override public int compare(Entry<K, V> o1, Entry<K, V> o2) {
			return o1.deadline.compareTo(o2.deadline);
		}
	};

	
	class RemoveOldEntries extends TimerTask {

		@Override public void run() {
			try {
				synchronized(TemporaryHashMap.this) {
					deadlines.sort(COMPARATOR);
					Date now = new Date();
					while (!deadlines.isEmpty()) {
						@SuppressWarnings("null") Entry<K, V> first = deadlines.get(0);
						if (first.deadline.before(now)) {
							int oldSize = deadlines.size();
							deadlines.remove(0);
							remove(first.key);
							if (deadlines.size()>=oldSize) {
								LOG.warning("TemporaryHashMap internal error"); // TODO KATASTROPHE!
							}
						} else {
							break;
						}
					}
				}
			} catch (Throwable t) {
				LOG.log(Level.WARNING, t.getMessage(), t);
			}
			updateTimer();
		}
		
	}

	
	long defaultMillis;
	ArrayList<Entry<K, V>> deadlines = new ArrayList<>();
	HashMap<K, Entry<K, V>> map = new HashMap<>();
	@Nullable RemoveOldEntries timerTask = null;
	ArrayList<RemoveNotification<K, V>> removeNotifications = new ArrayList<>();
	
	
	public TemporaryHashMap(long defaultMillis) {
		this.defaultMillis = defaultMillis;
	}
	
	
	/** Erzeugt eine {@link TemporaryHashMap} mit {@link #DEFAULT_MILLIS_TO_KEEP} Standard-Vergesslichkeit.*/
	public TemporaryHashMap() {
		this(DEFAULT_MILLIS_TO_KEEP);
	}
	
	
	protected synchronized void updateTimer() {
		if (timerTask!=null) {
			timerTask.cancel();
			timerTask = null;
		}
		if (!deadlines.isEmpty()) {
//			Entry<K, V> first = deadlines.first();
			deadlines.sort(COMPARATOR);
			@SuppressWarnings("null") Entry<K, V> first = deadlines.get(0);
			timerTask = new RemoveOldEntries();
			timer.schedule(timerTask, first.deadline);
		}
	}
	
	
	public int size() {
		return map.size( );
	}


	public boolean isEmpty() {
		return map.isEmpty( );
	}


	/**
	 * @param key {@link K Key}
	 * @param value {@link V Value}
	 * @param millisToKeep Millisekunden Lebenszeit für dieses Mapping, falls >=0L
	 * @param deadline {@link Date}, zu dem das Mapping entfernt wird, falls nicht null
	 * @return alter {@link V Value} zum {@link K Key} oder null 
	 */
	public synchronized @Nullable V put(K key, V value, long millisToKeep, @Nullable Date deadline) {
		@Nullable V result;
		Entry<K, V> entry;
		try {
			entry = map.getOrX(key);
			deadlines.remove(entry);
			entry.set(value, millisToKeep>=0L ? millisToKeep : entry.millisToKeep, deadline);
			result = value;
		} catch (KeyNotFoundException e) {
			entry = new Entry<>(key, value, millisToKeep>=0L ? millisToKeep : deadline!=null ? 0L : defaultMillis, deadline);
			result = null;
		}
		deadlines.add(entry);
		map.put(key, entry);
		updateTimer();
		Entry<K, V> logEntry = entry;
		LOG.finer(() -> logEntry.toString());
		return result;
	}
	
	
	public Stream<V> valueStream() {
		return map.values().stream().map(e -> e.getValue());
	}


	public @Nullable V put(K key, V value, Date deadline) {
		return put(key, value, -1L, deadline);
	}
	
	
	public @Nullable V put(K key, V value, long millisToKeep) {
		return put(key, value, millisToKeep, null);
	}
	
	
	public @Nullable V put(K key, V value) {
		return put(key, value, -1L, null);
	}
	
	
	public boolean containsKey(K key) {
		return map.containsKey(key);
	}
	
	
	public boolean containsValue(V value) {
		return map.values().stream()
			.anyMatch(e -> Objects.equals(value, e.getValue()));
	}
	
	
	/** Liefert value zum key.
	 * @param key {@link K}, dessen value gefragt ist
	 * @return Value zum key.
	 * @throws KeyNotFoundException zum key ist kein value hinterlegt.
	 */
	public <E extends Throwable> V getOrX(K key, Supplier<E> supplier) throws E {
		Entry<K, V> entry = map.getOrX(key, supplier);
		entry.update();
		updateTimer();
		return entry.value;
	}


	/** Liefert value zum key.
	 * @param key {@link K}, dessen value gefragt ist
	 * @return Value zum key.
	 * @throws KeyNotFoundException zum key ist kein value hinterlegt.
	 */
	public synchronized V getOrX(K key) throws KeyNotFoundException {
		Entry<K, V> entry = map.getOrX(key);
		entry.update();
		updateTimer();
		return entry.value;
	}
	
	
	public synchronized @Nullable V getOrNull(K key) {
		@Nullable V result = null;
		Entry<K,V> entry = map.getOrNull(key);
		if (entry!=null) {
			entry.update();
			updateTimer();
			result = entry.value;
		}
		return result;
	}


	/** Liefert value zum key. Falls es für den key keinen Eintrag gibt, wird mit dem supplier ein neuen Eintrag erzeugt und dieser zurückgegeben.
	 * @param EX {@link Throwable}, das supplier werfen darf.
	 * @param key {@link K}, dessen value gefragt ist
	 * @param supplier {@link Supplier}, der den neuen Wert liefert, falls es zum key noch keinen Eintrag gab.
	 * @return Value zum key oder vom supplier gelieferter Wert
	 * @throws EX 
	 */
	public <EX extends Throwable> V getOrCreate(K key, ThrowingSupplier<V, EX> supplier) throws EX {
		try {
			return getOrX(key);
		} catch (KeyNotFoundException e) {
			V result = supplier.get();
			put(key, result);
			return result;
		}
	}
	
	
	public @Nullable V remove(K key) {
		Entry<K,V> remove = map.remove(key);
		if (remove!=null) {
			LOG.fine(() -> "removed: "+remove);
			for (RemoveNotification<K, V> removeNotification : removeNotifications) {
				removeNotification.onRemove(this, remove.getKey(), remove.getValue());
			}
		}
		return O.nn(remove, Entry::getValue);
	}
	
	
	public int removeValue(V value) {
		int result = 0;
		@SuppressWarnings("unchecked") Entry<K, V>[] values = map.values().toArray(Entry[]::new);
		for (Entry<K, V> entry : values) {
			if (O.equals(entry.getValue(), value)) {
				remove(entry.getKey());
				result++;
			}
		}
		return result;
	}
	
	
	public void clear() {
		LOG.fine("clear");
		for (Entry<K, V> entry : map.values()) {
			for (RemoveNotification<K, V> removeNotification : removeNotifications) {
				removeNotification.onRemove(this, entry.getKey(), entry.getValue());
			}
		}
		
	}
	
	
	public void addRemoveNotification(RemoveNotification<K, V> removeNotification) {
		removeNotifications.add(removeNotification);
	}
	
	
	public void removeRemoveNotification(RemoveNotification<K, V> removeNotification) {
		removeNotifications.remove(removeNotification);
	}
	
	
	@SuppressWarnings("null")
	public void close() {
		clear();
		if (timerTask!=null) {
			timerTask.cancel();
		}
		deadlines.clear();
		map.clear();
		deadlines = null;
		map = null;
		timerTask = null;
		removeNotifications = null;
	}
	
	
		// ========== Object ==========
	
	
	@Override public String toString() {
		return map.toString();
	}


}
