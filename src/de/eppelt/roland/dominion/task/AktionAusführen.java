package de.eppelt.roland.dominion.task;


import de.eppelt.roland.dominion.Dran;
import de.eppelt.roland.dominion.Karten;
import de.eppelt.roland.dominion.Spieler;
import de.eppelt.roland.dominion.action.Aktion;


public class AktionAusführen extends AufgabeImpl {


	Dran dran;
	Spieler spieler;
	

	public AktionAusführen(Dran dran) {
		this.dran = dran;
		this.spieler = dran.getSpieler();
	}


	@Override public boolean execute() {
		fine("append ");
		Karten möglicheAktionen = spieler.möglicheAktionen();
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
				spieler.getHandkarten().entferne(karte);
				spieler.getSeite().legeAb(karte);
				spieler.updateOtherPlayers();
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
			done();
			if (spieler.currentAufgabe() instanceof OpferAufgabe) {
				spieler.nextAufgabe(new KartenKaufen(spieler.geld()+dran.getGeld(), dran.getKäufe()));
			} else {
				spieler.sofortAufgabe(new KartenKaufen(spieler.geld()+dran.getGeld(), dran.getKäufe()));
			}
			return false;
		}
	}


	@Override public String getName() {
		return "Aktion(en) ausführen";
	}

}