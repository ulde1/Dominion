package de.eppelt.roland.dominion.task;

import de.eppelt.roland.dominion.Spieler;

/** Nur Handkarten anzeigen */
public class KeineAufgabe extends AufgabeImpl {


	public KeineAufgabe(Spieler spieler) {
		setSpieler(spieler);
	}


	@Override public boolean anzeigen() {
		fine("append");
		headerHandkarten();
		dran(dran -> {
			handkarten(dran.getSpieler());
			say(dran.getSpieler().getName());
			say(" tut gerade ");
			sayln(dran.getSpieler().currentAufgabe().getName());
		});
		return true;
	}


	@Override public String getName() {
		return "nichts";
	}

}