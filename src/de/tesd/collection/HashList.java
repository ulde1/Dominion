package de.tesd.collection;


import static java.util.Collections.EMPTY_LIST;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.Nullable;

import de.tesd.util.O;


/** Eine HashMap, die ganze Listen von {@link V}  enthalten kann.
 * @author Roland M. Eppelt
 *
 * @param <K> {@link Class} der Schlüssel
 * @param <V> {@link Class} der Werte, die an {@link K Schlüssel} gebunden werden können. 
 * @author Roland M. Eppelt
 */
public class HashList<K, V> {
	
	
	public final static Collector<String[], ?, HashList<String, String>> FROM_STRING2 = new Collector<String[], HashList<String, String>, HashList<String, String>>() {
		@Override public Supplier< HashList<String, String>> supplier() { return HashList::new; }
		@Override public BiConsumer<HashList<String, String>, String[]> accumulator() { return (h, v) -> h.add(v[0], v.length>1 ? v[1] : ""); }
		@Override public BinaryOperator<HashList<String, String>> combiner() { return HashList::combine; } 
		@Override public Function<HashList<String, String>, HashList<String, String>> finisher() { return a -> a; }
		@SuppressWarnings("null")
		@Override public Set<Characteristics> characteristics() { return Collections.emptySet(); }
	};
	
	
	/** Vereinigt zwei {@link HashList}s.
	 * @param <K> {@link Class} der Schlüssel
	 * @param <V> {@link Class} der Werte, die an {@link K Schlüssel} gebunden werden können. 
	 * @param a erste {@link HashList}
	 * @param b zweite {@link HashList}
	 * @return {@link HashList}, die alle Elemente beider {@link HashList}s enthält.
	 */
	public static <K, V> HashList<K, V> combine(HashList<K, V> a, HashList<K, V> b) {
		HashList<K, V> result = new HashList<K, V>();
		result.addAll(a);
		result.addAll(b);
		return result;
	}


	private HashMap<K, ArrayList<V>> map = new HashMap<K, ArrayList<V>>();
	
	
	/** @return enthält diese {@link HashList} überhaupt Einträge?
	 * @see Map#isEmpty() */
	public boolean isEmpty() {
		return map.isEmpty();
	}
	
	
	/** @return Enthält diese {@link HashList} {@link V Werte} für {@link K key}?
	 * @see Map#containsKey(Object) */
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}
	
	
	/** Entfernt alle Einträge aus dieser {@link HashList}. Sie ist danach {@link #isEmpty() leer}.
	 * @see Map#clear() */
	public void clear() {
		map.clear();
	}


	/** Fügt {@link V element} zum {@link K key} hinzu. Bereits vorhandene {@link V Elemente} bleiben bestehen.
	 * @param key {@link K} Schlüssel
	 * @param element {@link V}, das hinzugefügt werden soll
	 */
	public void add(K key, V element) {
		ArrayList<V> liste = map.getOrCreate(key, ArrayList::new);
		liste.add(element);
	}


	/** Fügt alle Einträge der {@link HashList} hinzu.
	 * @param hashList {@link HashList}, deren einträge hinzugefügt werden.
	 */
	public void addAll(HashList<K, V> hashList) {
		hashList.map.entrySet().stream()
			.forEach(e -> getOrCreate(e.getKey()).addAll(e.getValue()));
	}


	/** Liefert die Liste aller {@link V Elemente}, die an {@link K key} gebunden sind, oder eine leere, unverbundene Liste.
	 * @param key {@link K} Schlüssel
	 * @return Liste aller {@link V Elemente} zum {@link K key} oder eine leere, unverbundene Liste.
	 * @see Map#getOrDefault(Object, Object) */
	@SuppressWarnings("unchecked")
	public List<V> getOrEmpty(K key) {
		return O.or(map.getOrNull(key), EMPTY_LIST);
	}


	/** Liefert die Liste aller {@link V Elemente}, die an {@link K key} gebunden sind.
	 * @param key {@link K} Schlüssel
	 * @return Liste aller {@link V Elemente} zum {@link K key}.
	 * @see Map#getOrDefault(Object, Object) */
	public List<V> getOrCreate(K key) {
		return map.getOrCreate(key, ArrayList::new);
	}
	
	
	public @Nullable V getFirstOrNull(K key) {
		return Lists.firstOrNull(getOrEmpty(key));
	}


	public @Nullable V getFirstOr(K key, V elseValue) {
		return Lists.firstOr(getOrEmpty(key), elseValue);
	}


	/** Entfernt {@link V element} vom {@link K key}. Andere {@link V Elemente} bleiben bestehen. 
	 * @param key {@link K} Schlüssel
	 * @param element {@link V}, das entfernt werden soll
	 */
	public void remove(K key, V element) {
		ArrayList<V> list = map.getOrNull(key);
		if (list!=null) {
			list.remove(element);
			if (list.isEmpty()) {
				map.remove(key);
			}
		}
	}
	
	
	/** Falls !present, dann {@link #remove(Object, Object) remove}, sonst {@link #add(Object, Object) add}, falls !{@link #getOrEmpty(Object) get(key)}.{@link List#contains(Object) contains(value)}   */
	public void set(K key, V value, boolean present) {
		if (!present) {
			remove(key, value);
		} else if (!getOrEmpty(key).contains(value)) {
			add(key, value);
		}
	}
	

	/** @return {@link Set} der {@link K keys} dieser {@link HashList}
	 * @see Map#keySet() */
	public Set<K> keySet() {
		return map.keySet();
	}
	
	
	/** @return {@link Stream} aller {@link V} */
	public Stream<V> streamValues() {
		return map.values().stream().flatMap(l -> l.stream());
	}
	
	
	@Override public String toString() {
		return map.toString();
	}


}