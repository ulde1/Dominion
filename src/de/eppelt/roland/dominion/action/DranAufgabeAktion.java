package de.eppelt.roland.dominion.action;


import java.util.function.Function;

import de.eppelt.roland.dominion.Dominion;
import de.eppelt.roland.dominion.Dran;
import de.eppelt.roland.dominion.task.Aufgabe;


public class DranAufgabeAktion extends AktionImpl {

	
	Function<Dran, Aufgabe> dranAufgabeSupplier;
	

	public DranAufgabeAktion(Function<Dran, Aufgabe> dranAufgabeSupplier) {
		this.dranAufgabeSupplier = dranAufgabeSupplier;
	}
	
	
	@Override public void ausführen(Dominion dominion, Dran dran) {
		dran.getSpieler().sofortAufgabe(dranAufgabeSupplier.apply(dran));
	}
	

}
