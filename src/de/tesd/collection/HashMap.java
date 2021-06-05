package de.tesd.collection;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import de.tesd.util.KeyNotFoundException;
import de.tesd.util.O;


/** Die {@link java.util.HashMap} mit Typsicherheit und @Nullable-Annotation.
 * @author Roland M. Eppelt
 *
 * @param <K> Schlüssel, zu denen Werte gespeichert werden
 * @param <V> Werte, die zu Schlüsseln hinterlegt werden können.
 */
public class HashMap<K, V> extends java.util.HashMap<K, V> {

	
	private static final long serialVersionUID = -5431162720265180908L;
	
	
	/** Lädt {@link HashMap} aus diesem {@link Reader}: zeilenweise im Format "key = value"
	 * @param reader {@link Reader}
	 * @return {@link HashMap}
	 */
	public static HashMap<String, @Nullable String> loadFromFile(BufferedReader reader) {
		HashMap<String, @Nullable String> result = new HashMap<>();
		reader.lines().forEach(line -> {
			String[] keyValue = line.split("\\s*=\\s*", 2);
			result.put(keyValue[0], keyValue.length==1 ? null : keyValue[1]);
		});
		return result;
	}
	
	
	/** Lädt {@link HashMap} aus dieser {@link File}: zeilenweise im Format "key = value"
	 * @param file {@link File}
	 * @return {@link HashMap}
	 */
	public static HashMap<String, @Nullable String> loadFromFile(File file) throws FileNotFoundException, IOException {
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			return loadFromFile(reader);
		}
	}
	
	
	/** Lädt {@link HashMap} aus dieser Datei: zeilenweise im Format "key = value"
	 * @param filename
	 * @return {@link HashMap}
	 */
	public static HashMap<String, @Nullable String> loadFromFile(String filename) throws FileNotFoundException, IOException {
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			return loadFromFile(reader);
		}
	}
	
	
	
	/** @see java.util.HashMap#HashMap() */
	public HashMap() {
		super();
	}


	/** @see java.util.HashMap#HashMap(int, float) */
	public HashMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}


	/** @see java.util.HashMap#HashMap(int) */
	public HashMap(int initialCapacity) {
		super(initialCapacity);
	}


	/** @see java.util.HashMap#HashMap(Map) */
	public HashMap(Map<? extends K, ? extends V> m) {
		super(m);
	}


	public V getOr(Object key, V defaultValue) {
		return super.getOrDefault(key, defaultValue);
	}


	/** Liefert Value zum Key oder null. */
	@SuppressWarnings("null") public @Nullable V getOrNull(K key) {
		return super.getOrDefault(key, null);
	}
	
	
	/** Liefert Value zum Key oder null. */
	@SuppressWarnings("null") public @Nullable V getOrNullByObject(Object key) {
		return super.getOrDefault(key, null);
	}
	
	
	/** Liefert den value zum key als {@link T} oder null
	 * @param <T> Gewünschte {@link Class} des Values zum key 
	 * @param tClass {@link Class} für {@link T}
	 * @param key {@link K}, dessen value gefragt ist
	 * @return Den value zum key als {@link T} oder null
	 * @throws ClassCastException Der value zum key ist kein {@lnk T}
	 */
	public <T extends V> @Nullable T getOrNull(Class<T> tClass, K key) throws ClassCastException {
		return tClass.cast(getOrNull(key));
	}
	
	
	/** Liefert den value zum key als {@link T} oder wirft {@link X}
	 * @param <T> Gewünschte {@link Class} des Values zum key 
	 * @param <X> {@link Exception}, falls es keinen value zum key gibt
	 * @param tClass {@link Class} für {@link T}
	 * @param key {@link K}, dessen value gefragt ist
	 * @param supplier {@link Function}, die zum key eine {@link X} liefert
	 * @return Den value zum key als {@link T} oder wirft {@link X}
	 * @throws X Zum key ist kein value hinterlegt.
	 * @throws ClassCastException Der value zum key ist kein {@lnk T}
	 */
	public <T extends V, X extends Throwable> T getOrX(Class<T> tClass, K key, Function<K, X> supplier) throws X, ClassCastException {
		if (!super.containsKey(key))
			throw supplier.apply(key);
		return tClass.cast(super.get(key));
	}


	/** Liefert value zum key oder wirft {@link X}.
	 * @param <X> {@link Exception}, falls es keinen value zum key gibt
	 * @param key {@link K}, dessen value gefragt ist
	 * @param supplier {@link Function}, die zum key eine {@link X} liefert
	 * @return Den value zum key oder wirft {@link X}.
	 * @throws X Zum key ist kein value hinterlegt.
	 */
	public <X extends Throwable> V getOrX(K key, Function<K, X> supplier) throws X {
		return getOrX(key, () -> supplier.apply(key));
	}


	/** Liefert value zum key.
	 * @param key {@link K}, dessen value gefragt ist
	 * @return Value zum key.
	 * @throws KeyNotFoundException zum key ist kein value hinterlegt.
	 */
	public <X extends Throwable> V getOrX(K key, Supplier<X> supplier) throws X {
		if (!super.containsKey(key))
			throw supplier.get();
		return super.get(key);
	}


	/** Liefert value zum key oder wirft {@link KeyNotFoundException}.
	 * @param <T> Gewünschte {@link Class} des Values zum key 
	 * @param key {@link K}, dessen value gefragt ist
	 * @return Value zum key.
	 * @throws KeyNotFoundException zum key ist kein value hinterlegt.
	 */
	public <T extends V> T getOrX(Class<T> tClass, K key) throws KeyNotFoundException, ClassCastException {
		return getOrX(tClass, key, (k) -> new KeyNotFoundException(O.toStringOr(k, "null")));
	}


	/** Liefert value zum key oder wirft {@link KeyNotFoundException}.
	 * @param key {@link K}, dessen value gefragt ist
	 * @return Value zum key.
	 * @throws KeyNotFoundException zum key ist kein value hinterlegt.
	 */
	public V getOrX(K key) throws KeyNotFoundException {
		return getOrX(key, () -> new KeyNotFoundException(O.toStringOr(key, "null")));
	}


	/** Liefert non-null value zum key.
	 * @param key {@link K}, dessen value gefragt ist
	 * @return Value zum key, value!=null
	 * @throws KeyNotFoundException
	 * @throws NullPointerException
	 */
	public @NonNull V getMOrX(K key) throws KeyNotFoundException {
		V result = getOrX(key);
		if (result==null) {
			throw new NullPointerException(key+"==null");
		}
		return result;
	}


	/** Entfernt den Key aus der {@link HashMap} und gibt den zugehörigen value oder null zurück.
	 * @param key {@link K} Der zu lesende und zu löschende Key
	 * @return Den Wert, der zu Key gehört hat */
	public @Nullable V getOrNullAndRemove(K key) {
		@Nullable V result = getOrNull(key);
		remove(key);
		return result;
	}
	
	
	
	/** Liefert value zum key. Falls es für den key keinen Eintrag gibt, wird mit dem supplier ein neuen Eintrag erzeugt und dieser zurückgegeben.
	 * @param key {@link K}, dessen value gefragt ist
	 * @param supplier {@link Supplier}, der den neuen Wert liefert, falls es zum key noch keinen Eintrag gab.
	 * @return Value zum key oder vom supplier gelieferter Wert
	 */
	public V getOrCreate(K key, Supplier<V> supplier) {
		try {
			return getOrX(key);
		} catch (KeyNotFoundException e) {
			V result = supplier.get();
			put(key, result);
			return result;
		}
	}
	
	
	/** Liefert value zum key. Falls es für den key keinen Eintrag gibt, wird mit dem supplier ein neuen Eintrag erzeugt und dieser zurückgegeben.
	 * @param key {@link K}, dessen value gefragt ist
	 * @param supplier {@link Supplier}, der den neuen Wert liefert, falls es zum key noch keinen Eintrag gab.
	 * @return Value zum key oder vom supplier gelieferter Wert
	 */
	public <T extends V> T getOrCreate(Class<T> tClass, K key, Supplier<T> supplier) {
		try {
			return getOrX(tClass, key);
		} catch (KeyNotFoundException e) {
			T result = supplier.get();
			put(key, result);
			return result;
		}
	}
	
	
	// ========== HashMap ==========

	
	
	/** @deprecation Klarer und typsicherer sind {@link #getOrNull} oder {@link #getX} */ 
	@Deprecated @SuppressWarnings("null") @Override public @Nullable V get(Object key) {
		return super.get(key);
	}


	@SuppressWarnings("null") @Override public @Nullable V put(K key, V value) {
		return super.put(key, value);
	}


	@SuppressWarnings("null") @Override @Nullable public V remove(@Nullable Object key) {
		return super.remove(key);
	}


	@SuppressWarnings("null") @Override public Set<@NonNull K> keySet() {
		return super.keySet();
	}


	@SuppressWarnings("null") @Override public Set<Entry<@NonNull K, V>> entrySet() {
		return super.entrySet();
	}


	@SuppressWarnings("null") @Override public @NonNull V getOrDefault(@Nullable Object key, @NonNull V defaultValue) {
		return super.getOrDefault(key, defaultValue);
	}


	@SuppressWarnings("null") @Override public @Nullable V putIfAbsent(K key, V value) {
		return super.putIfAbsent(key, value);
	}


	@SuppressWarnings("null") @Override public @Nullable V replace(K key, V value) {
		return super.replace(key, value);
	}


	@SuppressWarnings("null") @Override public @Nullable V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
		return super.computeIfAbsent(key, mappingFunction);
	}


	@SuppressWarnings("null") @Override public @Nullable V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		return super.computeIfPresent(key, remappingFunction);
	}


	@SuppressWarnings("null") @Override public @Nullable V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		return super.compute(key, remappingFunction);
	}


	@SuppressWarnings("null") @Override public @Nullable V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
		return super.merge(key, value, remappingFunction);
	}


//	 public void clear() {
//		 map.clear();
//	}
//	 public boolean isEmpty() {
//		return map.isEmpty();
//	}
//	 public boolean containsKey(Object key) {
//		return map.containsKey(key);
//	}
//	 public int size() {
//		return map.size();
//	}
//	 public Collection<V> values() {
//		return map.values();
//	}
//	 public void putAll(Map<? extends K, ? extends V> m) {
//		map.putAll(m);
//	}
//	 public boolean remove(Object key, Object value) {
//		return map.remove(key, value);
//	}
//	
//	
//	/** @deprecation Klarer und typsicherer sind {@link #getOrNull} oder {@link #getX} */ 
//	@Deprecated @SuppressWarnings("null") public @Nullable V getOrNull(Object key) {
//		return map.get(key);
//	}
//
//
//	@SuppressWarnings("null") public @Nullable V put(K key, V value) {
//		return map.put(key, value);
//	}
//
//
//	@SuppressWarnings("null") @Nullable public V remove(Object key) {
//		return map.remove(key);
//	}
//
//
//	@SuppressWarnings("null") public Set<@NonNull K> keySet() {
//		return map.keySet();
//	}
//
//
//	@SuppressWarnings("null") public Set<Entry<@NonNull K, V>> entrySet() {
//		return map.entrySet();
//	}
//
//
//	@SuppressWarnings("null") public @NonNull V getOrDefault(Object key, @NonNull V defaultValue) {
//		return map.getOrDefault(key, defaultValue);
//	}
//
//
//	@SuppressWarnings("null") public @Nullable V putIfAbsent(K key, V value) {
//		return map.putIfAbsent(key, value);
//	}
//
//
//	@SuppressWarnings("null") public @Nullable V replace(K key, V value) {
//		return map.replace(key, value);
//	}
//
//
//	@SuppressWarnings("null") public @Nullable V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
//		return map.computeIfAbsent(key, mappingFunction);
//	}
//
//
//	@SuppressWarnings("null") public @Nullable V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
//		return map.computeIfPresent(key, remappingFunction);
//	}
//
//
//	@SuppressWarnings("null") public @Nullable V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
//		return map.compute(key, remappingFunction);
//	}
//
//
//	@SuppressWarnings("null") public @Nullable V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
//		return map.merge(key, value, remappingFunction);
//	}
	
	
	@SuppressWarnings("unchecked")
	@Override public HashMap<K, V> clone() {
		return (HashMap<K, V>) super.clone();
	}
	
	
}
