package de.eppelt.roland.dominion.task;


import de.eppelt.roland.dominion.Dran;
import de.eppelt.roland.dominion.Karten;
import de.eppelt.roland.dominion.action.Aktion;


public class AktionAusführen extends DranAufgabeImpl {


	public AktionAusführen(Dran dran) {
		super(dran);
	}


	@Override public boolean anzeigen() {
		fine("append ");
		Karten möglicheAktionen = getSpieler().möglicheAktionen();
		int aktionen = dran.getAktionen();
		if (aktionen>0 && !möglicheAktionen.isEmpty()) {
			fine("Aktionen");
			headerHandkartenTitle();
			if (aktionen>1) {
				say("Du hast folgende Aktionen (und danach noch ");
				say(aktionen-1);
				sayln(aktionen==2 ? " weitere Aktion):" : " weitere Aktionen):");
			} else {
				sayln("Du hast folgende Aktionen:");
			}
			oneKarte(möglicheAktionen, (handler, karte) -> {
				Aktion aktion = karte.getAktion();
				dran.addAktionen(-1);
				assert aktion!=null : "aktion==null";
				fine(() -> "Aktion ausführen: "+aktion);
				handler.spielerHat("Aktion "+aktion.getName()+" ausgeführt.");
				handkarten().entferne(karte);
				seite().legeAb(karte);
				getSpieler().updateOtherPlayers();
				dran.incAusgespielteAktionen();
				aktion.ausführen(handler.getInstance());
			});
			ln();
			say("Klicke die Karte an, die du ausspielen willst oder drücke ");
			button("Keine Aktionskarte ausspielen", 'k', true, handler -> {
				möglicheAktionen.clear();
				dran.setAktionen(0);
			});
			ln();
			return true;
		} else {
			fine("Skip");
			getSpieler().putAufgabe(new KartenKaufen(getSpieler().geld()+dran.getGeld(), dran.getKäufe()));
			done();
			return false;
		}
	}


	@Override public String getName() {
		return "Aktion(en) ausführen";
	}

}