package de.eppelt.roland.dominion.task;

import de.eppelt.roland.dominion.Spieler;

public class EndeAufgabe extends AufgabeImpl {


	public EndeAufgabe(Spieler spieler) {
		setSpieler(spieler);
	}


	@Override public boolean anzeigen() {
		header();
		title("Ende");
		sayln("Das Spiel ist zu Ende.");
		handkarten(getSpieler());
		return true;
	}


	@Override public String getName() {
		return "nichts mehr";
	}

}