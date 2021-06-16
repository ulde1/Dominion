package de.eppelt.roland.dominion.task;


import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.ui.Handler;


public class KartenKaufen extends KaufenAufgabe {
	
	
	private boolean prepared = false;


	public KartenKaufen() {
		super(0, 0);
	}


	@Override public String getName() {
		return kaufNr>1 ? "Karten kaufen ("+kaufNr+")" : "Karten kaufen";
	}
	
	
	@Override public void vorbereiten() {
		super.vorbereiten();
		if (!prepared) {
			dran(dran -> {
				geld = getSpieler().getHandkartenGeld()+dran.getGeld();
				käufe = dran.getKäufe();
			});
			prepared = true;
		}
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
			getSpieler().sofortAufgabe(new Aufräumen(getSpieler()));
			done();
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
			handler.getSpieler().putAufgabe2(new Aufräumen(handler.getSpieler()));
			done();
		}
	}
	
	
	@Override protected void keinKauf(Handler handler) {
		handler.getSpieler().putAufgabe2(new Aufräumen(handler.getSpieler()));
		super.keinKauf(handler);
	}
	
	
}