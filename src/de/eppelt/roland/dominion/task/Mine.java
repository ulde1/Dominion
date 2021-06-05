package de.eppelt.roland.dominion.task;


import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Karten;
import de.eppelt.roland.dominion.Vorrat;
import de.eppelt.roland.dominion.ui.Handler;


/** Entsorge Geld-Handkarte, Neue Geldkarte+3 auf die Hand */
public class Mine extends Umbau {
	
	
	@Override public Karten getAltKarten() {
		return handkarten().stream()
			.filter(Karte::isGeld)
			.collect(Karten.COLLECT);		
	}
	
	
	@Override public int calcGeld(Karte karte) {
		return kosten(karte)+3;
	}
	
	
	@Override public Karten getNeuKarten(Vorrat vorrat) {
		return vorrat.getKarten(k -> k.isGeld() && kosten(k)<=geld);
	}
	
	
	@Override public void neueKarteAblegen(Handler handler, Karte karte) {
		handler.handkarten().legeAb(karte);
	}
	
	
}
