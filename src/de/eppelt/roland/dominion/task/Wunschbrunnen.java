package de.eppelt.roland.dominion.task;

import org.eclipse.jdt.annotation.Nullable;

import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Karten;
import de.eppelt.roland.dominion.Spieler.EmptyDeckException;

/** +1 Karte,  +1 Aktion, Benenne eine Karte. Decke die oberste Karte von deinem Nachziehstapel auf. Wenn es die benannte Karte ist, nimm sie auf die Hand. */
public class Wunschbrunnen extends AufgabeImpl {
	
	
	@Nullable Karte tipp, gezogen;


	@Override public void vorbereiten() {
		zieheKarten(1);
		addAktionen(1);
		super.vorbereiten();
	}
	
	
	@SuppressWarnings("null")
	@Override public boolean anzeigen() {
		headerHandkartenTitle();
		if (tipp==null) {
			sayln("Welche Karte liegt wohl ganz oben auf deinem Nachziehstapel? Tippst du richtig, darfst du sie auf die Hand nehmen.");
			Karten karten = vorrat().getKarten(k -> true);
			oneKarte(karten, (handler, karte) -> {
				tipp = karte;
				try {
					gezogen = handler.zieheKarte(false);
				} catch (EmptyDeckException e) {
					// Das ist aber ungeschickt.
				}
			});
		} else if (gezogen==null) {
			sayln("Der Wunschbrunnen bringt's gar nicht, wenn du keine Karte zum Nachziehen hast.");
			button("OK", 'o', true, handler -> done());
		} else if (gezogen==tipp) {
			sayln("Jabba-dabba-duuuu! Du hast richtig geraten!");
			sayln("Nimm die Karte auf die Hand.");
			button(gezogen, true, (handler, karte) -> {
				handler.handkarten().legeAb(gezogen);
				handler.spielerHat("richtig getippt und hat "+karte.getName()+" nachgezogen.");
				done();
			});
		} else {
			sayln("Diese Karte war auf dem Nachziehstapel:");
			karte(gezogen);
			say("Du musst sie wohl wieder ");
			button("zurück legen", 'z', true, handler -> {
				handler.nachziehStapel().legeAb(gezogen);
				handler.spielerHat(" falsch getippt.");
				done();
			});
		}
		return true;
	}

	
}
