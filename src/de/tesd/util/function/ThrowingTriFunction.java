package de.tesd.util.function;

import java.util.function.Function;

/** {@link Function} mit drei Parametern, die auch {@link Throwable}s werfen darf.
 * @author Roland M. Eppelt
 *
 * @param <R> gelieferte Ergebnis-{@link Class}
 * @param <T> übergebene Parameter-{@link Class}
 * @param <U> übergebene Parameter-{@link Class}
 * @param <V> übergebene Parameter-{@link Class}
 * @param <EX> {@link Throwable}
 */
@FunctionalInterface
public interface ThrowingTriFunction<R, T, U, V, EX extends Throwable> {
    R apply(T t, U u, V v) throws EX;
}