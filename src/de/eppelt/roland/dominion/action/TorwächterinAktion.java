package de.eppelt.roland.dominion.action;

import de.eppelt.roland.dominion.Dominion;
import de.eppelt.roland.dominion.Dran;
import de.eppelt.roland.dominion.task.Torwächterin;


/** +1 Karte, +1 Aktion, Oberste 2 Nachziehkarten entsorgen oder ablegen oder in beliebiger Reihenfolge zurück */
public class TorwächterinAktion extends AktionImpl {
	
	
	@Override public void ausführen(Dominion dominion, Dran dran) {
		dran.getSpieler().zieheKarten(1);
		dran.addAktionen(1);
		dran.getSpieler().sofortAufgabe(new Torwächterin());
	}

	
}
