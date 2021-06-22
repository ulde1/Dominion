package de.eppelt.roland.dominion.task;


import de.eppelt.roland.dominion.Spieler;
import de.eppelt.roland.dominion.Spieler.EmptyDeckException;


public class Aufräumen extends AufgabeImpl {

	
	public Aufräumen(Spieler spieler) {
		setSpieler(spieler);
	}


	@Override public boolean anzeigen() {
		fine("append");
		spieler.getAblageStapel().legeAlleAbVon(spieler.getSeite());
		spieler.handkartenAblegen();
		try {
			spieler.handkartenAuffüllen();
		} catch (EmptyDeckException e) {
			// Ist dann halt so.
		}
		done();
		getInstance().derNächsteBitte();
		return false;
	}

}