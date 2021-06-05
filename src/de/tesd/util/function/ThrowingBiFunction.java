package de.tesd.util.function;

import java.util.function.BiFunction;

/** {@link BiFunction}, die auch {@link Throwable}s werfen darf.
 * @author Roland M. Eppelt
 *
 * @param <R> gelieferte Ergebnis-{@link Class}
 * @param <T> übergebene Parameter-{@link Class}
 * @param <U> übergebene Parameter-{@link Class}
 * @param <EX> {@link Throwable}
 */
@FunctionalInterface
public interface ThrowingBiFunction<R, T, U, EX extends Throwable> {
    R apply(T t, U u) throws EX;
}