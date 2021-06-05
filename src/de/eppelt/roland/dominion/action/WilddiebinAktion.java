package de.eppelt.roland.dominion.action;


import de.eppelt.roland.dominion.Dominion;
import de.eppelt.roland.dominion.Dran;
import de.eppelt.roland.dominion.task.Wilddiebin;


/** +1 Karte, +1 Aktion, +1 Geld, Lege pro leerem Vorratsstapel eine Handkarte ab */
public class WilddiebinAktion extends AktionImpl {


	@Override public void ausführen(Dominion dominion, Dran dran) {
		dran.zieheKarten(1);
		dran.addAktionen(1);
		dran.addGeld(1);
		dran.sofortAufgabe(new Wilddiebin(dran.getSpieler().getInstance()));
	}


}
