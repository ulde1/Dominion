package de.eppelt.roland.dominion.action;

import de.eppelt.roland.dominion.Dominion;
import de.eppelt.roland.dominion.Dran;
import de.eppelt.roland.dominion.task.Burghof;


/** +3 Karten; Lege eine beliebige Handkarte verdeckt auf den Nachziehstapel. */
public class BurghofAktion extends AktionImpl {

	
	@Override public void ausführen(Dominion dominion, Dran dran) {
		dran.getSpieler().zieheKarten(3);
		dran.getSpieler().sofortAufgabe(new Burghof());
	}

	
}
