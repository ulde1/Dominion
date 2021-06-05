package de.eppelt.roland.dominion.task;


import de.eppelt.roland.dominion.Spieler;
import de.eppelt.roland.dominion.Spieler.EmptyDeckException;


public class Aufräumen extends AufgabeImpl {

	
	Spieler spieler;
	

	public Aufräumen(Spieler spieler) {
		this.spieler = spieler;
	}


	@Override public boolean execute() {
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