package de.tesd.util;


import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import de.tesd.util.function.ThrowingFunction;
import de.tesd.util.function.ThrowingSupplier;


public class O {
	
	
	/** Liefert a.equals(b) auch wenn a==null.
	 * @return  a==null ? b==null : a.equals(b) */
	public static boolean equals(@Nullable Object a, @Nullable Object b) { return a==null ? b==null : a.equals(b); }
	
	
	/** Liefert object.hashCode() auch wenn a==null (dann: 0). 
	 * @return object==null ? 0 : object.hashCode() */
	public static int hashCode(@Nullable Object object) {
		return object==null ? 0 : object.hashCode();
	}
	

	/** @param o {@link Object}, dessen {@link Object#toString()}-Methode aufgerufen werden soll.
	 * @param nullString {@link String}, der bei null zurückgegeben werden soll
	 * @return {@link Object#toString()} oder nullString falls o==null,<br>
	 * also: o==null ? nullString : o.toString()
	 * @see String#valueOf(Object) */
	public static String toStringOr(@Nullable Object o, String nullString) {
		return nnOr(o, Object::toString, nullString);
	}
	

	/** @param o {@link Object}, dessen {@link Object#toString()}-Methode aufgerufen werden soll.
	 * @return {@link Object#toString()} oder null falls o==null.*/
	public static @Nullable String toString(@Nullable Object o) {
		return nn(o, Object::toString);
	}
	

	/** vergleicht auch null-Werte; null ist dabei am kleinsten. */
	public static <T extends Comparable<T>> int compareNullLow(@Nullable T a, @Nullable T b) {
		return a==null ? (b==null ? 0 : Integer.MIN_VALUE) : b==null ? Integer.MAX_VALUE : a.compareTo(b);
	}

	/** vergleicht auch null-Werte; null ist dabei am größten. */
	public static <T extends Comparable<T>> int compareNullHigh(@Nullable T a, @Nullable T b) {
		return a==null ? (b==null ? 0 : Integer.MAX_VALUE) : b==null ? Integer.MIN_VALUE : a.compareTo(b);
	}


	/** @return value oder defaultValue, falls null. */ 
	public static <T> T or(@Nullable T value, T defaultValue) {
		return value==null ? defaultValue : value;
	}
	

	/** @return value oder defaultValue, falls null. */
	public static <T> T or(@Nullable T value, Supplier<T> defaultValue) {
		return value!=null ? value : defaultValue.get();
	}
	

	/** @return value oder defaultValue, falls null. 
	 * @throws Exception */
	public static <T, EX extends Throwable> T orx(@Nullable T value, ThrowingSupplier<T, EX> defaultValue) throws EX {
		return value!=null ? value : defaultValue.get();
	}
	

	/**
	 * @param <T> Ergebnis-{@link Class}
	 * @param supplier {@link ThrowingSupplier} gewünschtes Ergebnis
	 * @param defaultValue {@link T} Ergebnis, falls null oder {@link Throwable}
	 * @param logger {@link Logger}, auf dem {@link Throwable}s als {@link Level#WARNING} geloggt werden  
	 * @return supplier.get() oder defaultValue, falls null oder {@link Throwable} */
	public static <T, EX extends Throwable> T  or(ThrowingSupplier<T, EX> supplier, T defaultValue, @Nullable Logger logger) {
		try {
			return O.or(supplier.get(), defaultValue);
		} catch (Throwable e) {
			if (logger!=null) {
				Loggers.warning(logger, e);
			}
			return defaultValue;
		}
	}


	/**
	 * @param <T> Ergebnis-{@link Class}
	 * @param supplier {@link ThrowingSupplier} gewünschtes Ergebnis
	 * @param defaultValue {@link T} Ergebnis, falls null oder {@link Throwable}
	 * @return supplier.get() oder defaultValue, falls null oder {@link Throwable} */
	public static <T, EX extends Throwable> T or(ThrowingSupplier<T, EX> supplier, T defaultValue) {
		return or(supplier, defaultValue, null);
	}
	

	/** Wirft eine {@link NullPointerException}, falls value==null. */
	public static <T> T orX(@Nullable T value, String message) {
		if (value!=null)
			return value;
		else
			throw new NullPointerException(message);
	}

	
	/** Führt consumer nur aus, wenn value!=null.
	 * @return value==null ? null : function.apply(value) */
	public static <T> void nnc(T value, Consumer<@NonNull T> consumer) {
		if (value!=null)
			consumer.accept(value);
	}

	
	/** Führt function nur aus, wenn value!=null, sonst null.
	 * @return value==null ? null : function.apply(value) */
	public static <T, R> @Nullable R nn(@Nullable T value, Function<T, R> function) {
		return value==null ? null : function.apply(value);
	}

	
	/**  Führt function1 nur aus, wenn value!=null, und function2 nur, wenn function1!=null. Sonst: null.
	 * @see #nn(Object, Function) nn (2x) */
	public static <T, U, R> @Nullable R nn(@Nullable T value, Function<T, U> function1, Function<U, R> function2) {
		return nn(nn(value, function1), function2);
	}

	
	/**  Führt function1 nur aus, wenn value!=null, und function2 nur, wenn function1!=null, usw. Sonst: null.
	* @see #nn(Object, Function) nn (3x) */
	public static <T, U1, U2, R> @Nullable R nn(@Nullable T value, Function<T, U1> function1, Function<U1, U2> function2, Function<U2, R> function3) {
		return nn(nn(nn(value, function1), function2), function3);
	}


	/** @return Klasse[Wert] */
	public static String classValue(@Nullable Object o) {
		if (o==null)
			return "null";
		else
			return o.getClass().getName()+"("+o+")";
	}
	
	
	public static <T, R> R nnOr(@Nullable T value, Function<T, R> function, R defaultValue) {
		return value!=null ? function.apply(value) : defaultValue;
	}


	public static <T, R> R nnOr(@Nullable T value, Function<T, R> function, Supplier<R> defaultValue) {
		return value!=null ? function.apply(value) : defaultValue.get();
	}


	public static <T, R, EX extends Throwable> R nnxOr(@Nullable T value, ThrowingFunction<R, T, EX> function, R defaultValue) {
		try {
			return value!=null ? function.apply(value) : defaultValue;
		} catch (Throwable t) {
			return defaultValue;
		}
	}


	@SuppressWarnings("null")
	public static <T> @Nullable T orNull(Optional<T> optional) {
		return optional.orElse(null);
	}
	
	
	public static <T> @Nullable T nullable(T t) {
		return t;
	}
	

	@SuppressWarnings("null")
	public static <@NonNull T> Class<@Nullable T> nullable(Class<T> tClass) {
		return tClass;
	}
	

	/** Identisch mit {@link Optional#orElseThrow(Supplier)}, nur mit Null-Annotations. */
	public static <T, X extends Throwable> T orX(Optional<? extends T> optional, Supplier<? extends X> exceptionSupplier) throws X {
		if (optional.isPresent())
			return optional.get();
		else
			throw exceptionSupplier.get();
	}


	public static <T> T orX(Optional<? extends T> optional) throws NoSuchElementException {
		return orX(optional, () -> new NoSuchElementException("No value present"));
	}


	public static <T> T assertNotNull(@Nullable T t) throws NullPointerException {
		if (t==null)
			throw new NullPointerException();
		return t;
	}


	public static boolean isNeutral(@Nullable Object o) {
		return o==null
			|| (o instanceof String && ((String) o).isBlank())
			|| (o instanceof Integer && ((Integer) o)==0)
			|| (o instanceof Long && ((Long) o)==0L)
			|| (o instanceof Float && ((Float) o)==0.0f)
			|| (o instanceof Double && ((Double) o)==0.0)
			|| (o instanceof Short && ((Integer) o)==0)
			|| (o instanceof Byte && ((Byte) o)==0)
			|| (o instanceof Character && ((Character) o)=='\000');
	}


}
