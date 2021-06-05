package de.eppelt.roland.dominion.action;


import java.util.function.Function;

import de.eppelt.roland.dominion.Dominion;
import de.eppelt.roland.dominion.Dran;
import de.eppelt.roland.dominion.Karte;


/** +1 Kauf, +1 Geld, Alle Karten (auch die Karten, die die Spieler auf der Hand halten) kosten in diesem Zug 1 weniger,
 * niemals jedoch weniger als 0. */
public class Bridge extends AktionImpl {


	@Override protected void ausführen(Dominion dominion, Dran dran) {
		dran.addKäufe(1);
		dran.addGeld(1);
		Function<Karte, Integer> kosten = dominion.getKosten();
		dominion.setKosten(karte -> Math.max(0, kosten.apply(karte)-1));
		dran.spielerHat("die Kartenpreise reduziert.");
	}

}
