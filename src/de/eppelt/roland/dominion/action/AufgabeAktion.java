package de.eppelt.roland.dominion.action;


import java.util.function.Supplier;

import de.eppelt.roland.dominion.Dominion;
import de.eppelt.roland.dominion.Dran;
import de.eppelt.roland.dominion.task.Aufgabe;


public class AufgabeAktion extends AktionImpl {

	
	Supplier<Aufgabe> aufgabeSupplier;
	

	public AufgabeAktion(Supplier<Aufgabe> aufgabeSupplier) {
		this.aufgabeSupplier = aufgabeSupplier;
	}
	
	
	@Override public void ausführen(Dominion dominion, Dran dran) {
		dran.getSpieler().sofortAufgabe(aufgabeSupplier.get());
	}
	

}
