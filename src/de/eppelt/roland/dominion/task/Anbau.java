package de.eppelt.roland.dominion.task;

import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Karten;
import de.eppelt.roland.dominion.Vorrat;

/** +1 Karte. +1 Aktion. Entsorge eine Karte aus deiner Hand. Nimm dir eine Karte, die genau 1 mehr kostet. */
public class Anbau extends Umbau {

	
	@Override public void vorbereiten() {
		getSpieler().zieheKarten(1);
		getSpieler().addAktionen(1);
	}
	
	
	@Override public void sayAuswählen() {
		sayln("Welche Handkarte willst du eintauschen? Du bekommst dafür eine Karte, die genau 1 Münze mehr kostet.");
	}
	
	
	@Override public int calcGeld(Karte karte) {
		return kosten(karte)+1;
	}
	
	
	@Override public Karten getNeuKarten(Vorrat vorrat) {
		return vorrat.getKarten(k -> kosten(k)==geld);
	}
	
	
}
