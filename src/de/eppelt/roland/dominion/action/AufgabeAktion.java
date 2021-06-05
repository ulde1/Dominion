package de.eppelt.roland.dominion.action;


import java.util.function.Function;
import java.util.function.Supplier;

import org.eclipse.jdt.annotation.Nullable;

import de.eppelt.roland.dominion.Dominion;
import de.eppelt.roland.dominion.Dran;
import de.eppelt.roland.dominion.task.Aufgabe;


public class AufgabeAktion extends AktionImpl {

	
	@Nullable Supplier<Aufgabe> supplier;
	@Nullable Function<Dran, Aufgabe> dranSupplier;
	

	public AufgabeAktion(Supplier<Aufgabe> supplier) {
		this.supplier = supplier;
	}
	
	
	public AufgabeAktion(Function<Dran, Aufgabe> dranSupplier) {
		this.dranSupplier = dranSupplier;
	}
	
	
	@SuppressWarnings("null")
	@Override public void ausführen(Dominion dominion, Dran dran) {
		if (supplier!=null) {
			dran.getSpieler().sofortAufgabe(supplier.get());
		}
		if (dranSupplier!=null) {
			dran.getSpieler().sofortAufgabe(dranSupplier.apply(dran));
		}
	}
	

}
