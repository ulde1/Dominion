package de.tesd.util.function;


import java.util.function.Supplier;


/** {@link Supplier}, der auch {@link Throwable}s werfen darf.
 * @author Roland M. Eppelt
 * @param <T> gelieferte {@link Class}
 * @param <EX> {@link Throwable} */
@FunctionalInterface
public interface ThrowingSupplier<T, EX extends Throwable> {


	T get() throws EX;

}