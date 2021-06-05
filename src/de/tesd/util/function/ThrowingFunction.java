package de.tesd.util.function;

import java.util.function.Function;

/** {@link Function}, die auch {@link Throwable}s werfen darf.
 * @author Roland M. Eppelt
 *
 * @param <R> gelieferte Ergebnis-{@link Class}
 * @param <T> übergebene Parameter-{@link Class}
 * @param <EX> {@link Throwable}
 */
@FunctionalInterface
public interface ThrowingFunction<R, T, EX extends Throwable> {
    R apply(T t) throws EX;
}