package de.eppelt.roland.dominion.task;


import de.eppelt.roland.dominion.Dran;
import de.eppelt.roland.dominion.Karten;
import de.eppelt.roland.dominion.action.Aktion;


public class AktionAusführen extends DranAufgabeImpl {


	private Karten möglicheAktionen;
	private int aktionen;
	
	
	@SuppressWarnings("null")
	public AktionAusführen(Dran dran) {
		super(dran);
	}
	
	
	public void erledigt() {
		fine("Erledigt");
		getSpieler().putAufgabe2(new KartenKaufen());
		done();
	}
	
	
	@Override public void vorbereiten() {
		super.vorbereiten();
		möglicheAktionen = getSpieler().möglicheAktionen();
		aktionen = dran.getAktionen();
	}
	
	
	@Override public boolean anzeigen() {
		fine("append ");
		vorbereiten();
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
				assert aktion!=null : "aktion==null";
				fine(() -> "Aktion ausführen: "+aktion);
				handler.spielerHat("Aktion "+aktion.getName()+" ausgeführt.");
				dran.addAktionen(-1);
				handkarten().entferne(karte);
				seite().legeAb(karte);
				getSpieler().updateOtherPlayers();
				dran.incAusgespielteAktionen();
				aktion.ausführen(handler.getInstance());
//				abschluss();
			});
			ln();
			say("Klicke die Karte an, die du ausspielen willst oder drücke ");
			button("Keine Aktionskarte ausspielen", 'k', true, handler -> {
				dran.setAktionen(0);
				erledigt();
			});
			ln();
			return true;
		} else {
			erledigt();
			return false;
		}
	}


	@Override public String getName() {
		return "Aktion(en) ausführen";
	}

}