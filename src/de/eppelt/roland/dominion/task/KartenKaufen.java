package de.eppelt.roland.dominion.task;


import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.ui.Handler;


public class KartenKaufen extends KaufenAufgabe {


	public KartenKaufen(int geld, int käufe) {
		super(geld, käufe);
	}


	@Override public String getName() {
		return kaufNr>1 ? "Karten kaufen ("+kaufNr+")" : "Karten kaufen";
	}

	
	@Override public boolean anzeigen() {
		if (käufe>0) {
			fine("Kaufen");
			headerHandkartenTitle();
			say("Du hast ");
			say(geld);
			say(" Geldmünzen");
			if (käufe>1) {
				say(" für ");
				say(käufe);
				sayln(" Käufe.");
			} else {
				sayln(".");
			}
			kauf();
			return true;
		} else {
			fine("append Skip");
			done();
			getSpieler().sofortAufgabe(new Aufräumen(getSpieler()));
			return false;
		}
	}
	
	
	@Override protected void kaufAbschluss(Handler handler, Karte kaufKarte) {
		if (handler.getInstance().isOpenForNewPlayers()) {
			handler.getInstance().closeForNewPlayers();
			try (FileOutputStream s = new FileOutputStream("games.log", true)) {
				s.write(String.format("%tF %<tT Dominion %s%n", new Date(), handler.getInstance().getSpieler().toString()).getBytes());
			} catch (IOException e) {
				warning(e);
			}
		}
		super.kaufAbschluss(handler, kaufKarte);
		if (käufe<=0) {
			handler.getSpieler().sofortAufgabe(new Aufräumen(handler.getSpieler()));
		}
	}
	
	
	@Override protected void keinKauf(Handler handler) {
		super.keinKauf(handler);
		handler.getSpieler().sofortAufgabe(new Aufräumen(handler.getSpieler()));
	}
	
	
}