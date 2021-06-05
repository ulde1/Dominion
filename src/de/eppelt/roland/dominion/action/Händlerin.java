package de.eppelt.roland.dominion.action;

import de.eppelt.roland.dominion.Dominion;
import de.eppelt.roland.dominion.Dran;
import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Spieler;
import de.eppelt.roland.dominion.Spieler.EmptyDeckException;


/** +1 Karte, +1 Aktion, +1 Geld für das erste Silber */
public class Händlerin extends AktionImpl {

	
	@Override public void ausführen(Dominion dominion, Dran dran) {
		Spieler spieler = dran.getSpieler();
		try {
			spieler.zieheKarte(true);
		} catch (EmptyDeckException e) {
			// Wo nichts ist, kann man auch nichts holen.
		}
		dran.addAktionen(1);
		if (spieler.getHandkarten().asList().contains(Karte.SILBER)) {
			dran.addGeld(1);
		}
	}
	

}
