package de.tesd.util.function;


import java.util.function.BiConsumer;


public interface ThrowingBiConsumer {


	/** {@link BiConsumer}, der auch {@link Throwable}s werfen darf.
	 * @author Roland M. Eppelt
	 * @param <S> erste ├╝bergebene {@link Class}
	 * @param <T> zweite ├╝bergebene {@link Class}
	 * @param <EX> {@link Throwable} */
	@FunctionalInterface
	public interface ThrowingConsumer<S, T, EX extends Throwable> {


		void accept(S s, T t) throws EX;

	}

	
}
