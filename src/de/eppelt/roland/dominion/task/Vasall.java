package de.eppelt.roland.dominion.task;


import de.eppelt.roland.dominion.Dran;
import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.action.Aktion;
import de.eppelt.roland.dominion.ui.Handler;


public class Vasall extends AufgabeImpl {


	Karte karte;


	public Vasall(Karte karte) {
		this.karte = karte;
	}


	void ablegen(Handler handler, Karte karte) {
		handler.legeAb(karte);
		handler.updateOtherPlayers();
		done();
	}


	@Override public boolean execute() {
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
