package de.eppelt.roland.dominion.action;


import de.eppelt.roland.dominion.Dominion;
import de.eppelt.roland.dominion.Dran;
import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Spieler;
import de.eppelt.roland.dominion.task.Vasall;


/** +2 Geld und lege die oberste Nachziehkarte ab; ist es eine Aktion, darfst du sie ausführen */
public class VasallAktion extends AktionImpl {
	

	@Override public void ausführen(Dominion dominion, Dran dran) {
		dran.addGeld(2);
		Spieler spieler = dran.getSpieler();
		Karte karte = spieler.getNachziehStapel().ziehe();
		spieler.sofortAufgabe(new Vasall(karte));
	}

}
