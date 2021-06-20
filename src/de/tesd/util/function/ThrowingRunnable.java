package de.tesd.util.function;

/** {@link Runnable}, das auch {@link Throwable}s werfen darf.
 * @author Roland M. Eppelt
 * @param <EX> {@link Throwable} */
@FunctionalInterface
public interface ThrowingRunnable<EX extends Throwable> {
	
	void run() throws EX;

}
