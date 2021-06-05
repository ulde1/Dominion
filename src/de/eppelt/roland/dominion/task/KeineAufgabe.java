package de.eppelt.roland.dominion.task;

import de.eppelt.roland.dominion.Dran;

/** Nur Handkarten anzeigen */
public class KeineAufgabe extends AufgabeImpl {


	@Override public boolean execute() {
		fine("append");
		headerHandkarten();
		Dran dran = getDran();
		if (dran!=null) {
			handkarten(dran.getSpieler());
			say(dran.getSpieler().getName());
			say(" tut gerade ");
			sayln(dran.getSpieler().currentAufgabe().getName());
		}
		return true;
	}


	@Override public String getName() {
		return "nichts";
	}

}