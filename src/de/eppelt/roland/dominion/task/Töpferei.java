package de.eppelt.roland.dominion.task;


import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.ui.Handler;


/** Nimm Karte vom Vorrat auf die Hand, die bis zu 5 kostet und lege Handkarte auf den Nachziehstapel */
public class Töpferei extends KaufenAufgabe {

	
	public Töpferei() {
		super(5, 1);
	}


	@Override public boolean anzeigen() {
		headerHandkartenTitle();
		sayln("Du darfst dir eine Karte für bis zu 5 Münzen nehmen.");
		kauf();
		return true;
	}
	
	
	@Override protected void kaufAbschluss(Handler handler, Karte kaufKarte) {
		handler.handkarten().legeAb(kaufKarte);
		done();
		handler.getSpieler().sofortAufgabe(new HandkarteAufNachziehstapel());
	}
	
	
	@Override protected void keinKauf(Handler handler) {
			super.keinKauf(handler);
			handler.getSpieler().sofortAufgabe(new HandkarteAufNachziehstapel());
		}
	

}