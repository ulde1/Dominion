package de.eppelt.roland.dominion.action;


import de.eppelt.roland.dominion.Dominion;
import de.eppelt.roland.dominion.Dran;


/** +2 Münzen; Wenn du in diesem Zug 3 oder mehr Aktionskarten ausgespielt hast (diese eingeschlossen): +1 Karte, +1 Aktion */
public class Verschwörer extends AktionImpl {


	@Override protected void ausführen(Dominion dominion, Dran dran) {
		dran.addGeld(2);
		if (dran.getAusgespielteAktionen()>=3) {
			dran.zieheKarten(1);
			dran.addAktionen(1);
		}
	}

}
