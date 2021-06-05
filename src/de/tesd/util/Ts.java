package de.tesd.util;


import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.IntFunction;


public class Ts<T> implements Iterable<T> {


	/** @return index of value in array or -1 if not found. */
	public static <T> int indexOf(T[] array, T value) {
		int result = array.length-1;
		while (result>=0 && !O.equals(array[result], value))
			result--;
		return result;
	}


	/** @return is value in array? 
	 * @see #indexOf(Object[], Object) */
	public static <T> boolean contains(T[] array, T value) {
		return indexOf(array, value)>=0;
	}

	@SafeVarargs public static <T> T[] concat(IntFunction<T[]> generator, T[]... arrays) {
			// Abkürzung: Evtl. nur ein einziges Array mit Elementen?
		T[] result = null;
		for (int i = 0; i<arrays.length; i++)
			if (arrays[i].length>0) {
				if (result==null)
					result = arrays[i];
				else {
					result = null;
					break;
				}
			}
		if (result!=null) // Juchhu!
			return result;
			// Dann halt die ausführliche Lösung
		result = generator.apply(Arrays.stream(arrays).mapToInt(a -> a.length).sum());
		int p = 0;
		for (int i = 0; i<arrays.length; i++) {
			int n = arrays[i].length;
			System.arraycopy(arrays[i], 0, result, p, n);
			p = p+n;
		}
		return result;
	}


	@SafeVarargs public static <T> T[] prepend(IntFunction<T[]> generator, T[] tail, T... ts) {
		return concat(generator, ts, tail);
	}


	@SafeVarargs public static <T> T[] append(IntFunction<T[]> generator, T[] head, T... ts) {
		return concat(generator, head, ts);
	}


	/** erzeugt einen Index als TreeMap über data. */
	public static <T, K> TreeMap<K, Integer> index(T[] data, Function<? super T, ? extends K> mapper) {
		TreeMap<K, Integer> result = new TreeMap<K, Integer>();
		for (int i = 0; i<data.length; i++)
			result.put(mapper.apply(data[i]), i);
		return result;
	}
	
	
	public static <T> T getRandomEntry(T[] array) {
		return array[(int) (array.length*Math.random())];
	}


	public static void reverse(int[] array) {
		int l = array.length;
		for (int i = 0; i<l/2; i++) {
			int temp = array[i];
			array[i] = array[l-i-1];
			array[l-i-1] = temp;
		}
	}


	// ========== Ts ==========

	
	protected T[] ts;


	public Ts(T[] ts) {
		this.ts = ts;
	}


	class ArrayIterator implements Iterator<T> {

		private int i;

		public ArrayIterator() {
			i = 0;
		}

		@Override public boolean hasNext() {
			return ts!=null&&i<ts.length;
		}

		@Override public T next() {
			return ts[i++];
		}
	}


	@Override public Iterator<T> iterator() {
		return new ArrayIterator();
	}


}
