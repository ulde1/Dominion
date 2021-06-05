package de.eppelt.roland.dominion.task;

import de.eppelt.roland.dominion.Dran;
import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Karten;
import de.eppelt.roland.dominion.Vorrat;

/** +1 Karte. +1 Aktion. Entsorge eine Karte aus deiner Hand. Nimm dir eine Karte, die genau 1 mehr kostet. */
public class Anbau extends Umbau {

	
	public Anbau(Dran dran) {
		dran.zieheKarten(1);
		dran.addAktionen(1);
	}
	
	
	@Override public int calcGeld(Karte karte) {
		return kosten(karte)+1;
	}
	
	
	@Override public Karten getNeuKarten(Vorrat vorrat) {
		return vorrat.getKarten(k -> kosten(k)==geld);
	}
	
	
}
