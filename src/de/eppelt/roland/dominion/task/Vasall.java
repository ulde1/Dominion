package de.eppelt.roland.dominion.task;


import de.eppelt.roland.dominion.Dran;
import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.action.Aktion;
import de.eppelt.roland.dominion.ui.Handler;


/** +2 Geld und lege die oberste Nachziehkarte ab; ist es eine Aktion, darfst du sie ausführen */
public class Vasall extends AufgabeImpl {


	@SuppressWarnings("null") Karte karte;


	@Override public void vorbereiten() {
		addGeld(2);
		karte = getSpieler().getNachziehStapel().ziehe();
		super.vorbereiten();
	}
	
	
	void ablegen(Handler handler, Karte karte) {
		handler.seite().legeAb(karte);
		handler.updateOtherPlayers();
		done();
	}


	@Override public boolean anzeigen() {
		headerHandkartenTitle();
		sayln("Du hast diese Karte vom Nachziehstapel gezogen, um sie abzulegen:");
		if (karte.getAktion()!=null) {
			karte(karte);
			ln();
			say("Möchtest du sie vorher ausführen? ");
			button("Ja", 'j', true, handler -> {
				Aktion aktion = karte.getAktion();
				assert aktion!=null;
				fine(() -> "Aktion ausführen: "+aktion);
				handler.spielerHat("Aktion "+aktion.getName()+" ausgeführt.");
				dran(Dran::incAusgespielteAktionen);
				aktion.ausführen(handler.getInstance());
				ablegen(handler, karte);
			});
			button("Nein", 'n', true, handler -> ablegen(handler, karte));
			ln();
		} else {
			button(karte, true, (handler, karte) -> ablegen(handler, karte));
			ln();
			button("OK", 'o', true, handler -> ablegen(handler, karte));
		}
		return true;
	}

}
