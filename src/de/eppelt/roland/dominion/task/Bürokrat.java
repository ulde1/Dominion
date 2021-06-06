package de.eppelt.roland.dominion.task;


import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Spieler.EmptyDeckException;


/** Nimm ein Silber vom Vorrat auf deinen Nachziehstapel. 
 * Alle Mitspieler müssen eine Punktekarte zurück auf den Nachziehstapel legen oder ihre Kartenhand offen vorzeigen.
 * @author Roland M. Eppelt */
public class Bürokrat extends AufgabeImpl {
	
	
	@Override public void vorbereiten() {
		getSpieler().angriff(BürokratOpfer::new);
		super.vorbereiten();
	}


	@Override public boolean anzeigen() {
		headerHandkartenTitle();
		if (vorrat().hat(Karte.SILBER)) {
			sayln("Nimm dieses Silber auf deinen Nachziehstapel.");
			button(Karte.SILBER, true, (handler, karte) -> {
				try {
					handler.nachziehStapel().legeAb(vorrat().zieheKarte(karte));
				} catch (EmptyDeckException e) {
				}
				done();
			});
			ln();
		} else {
			sayln("Oh! Es gibt gar keine Silber mehr, das du auf deinen Nachziehstapel legen könntest.");
			button("Sehr schade.", 's', true, client -> done());
			ln();
		}
		return true;
	}
	
	
}