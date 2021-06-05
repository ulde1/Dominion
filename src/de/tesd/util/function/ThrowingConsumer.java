package de.tesd.util.function;


import java.util.function.Consumer;


/** {@link Consumer}, der auch {@link Throwable}s werfen darf.
 * @author Roland M. Eppelt
 * @param <T> übergebene {@link Class}
 * @param <EX> {@link Throwable} */
@FunctionalInterface
public interface ThrowingConsumer<T, EX extends Throwable> {


	void accept(T t) throws EX;

	
}