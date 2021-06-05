package de.eppelt.roland.dominion.task;

import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.ui.Handler;


/** Nimm eine Karte, die bis zu 4 kostet. Ist es eine Aktionskarte: +1 Aktion / Geldkarte: +1 Münze / Punktekarte: +1 Karte */
public class Eisenhütte extends KaufenAufgabe {
	
	
	public Eisenhütte() {
		super(4, 1);
	}


	@Override public boolean execute() {
		headerHandkartenTitle();
		say("Du hast 4 Münzen. ");
		kauf();
		return true;
	}
	
	
	@Override protected void kaufAbschluss(Handler handler, Karte kaufKarte) {
		super.kaufAbschluss(handler, kaufKarte);
		if (kaufKarte.isAktion()) {
			handler.addAktionen(1);
		} else if (kaufKarte.isGeld()) {
			handler.addGeld(1);
		} else if (kaufKarte.hatPunkte()) {
			handler.zieheKarten(1);
		}
	}

}
