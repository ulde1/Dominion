package de.eppelt.roland.dominion.task;

import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Karten;


/** +3 Karten; Decke die obersten 4 Karten deines Nachziehstapels auf. Nimm alle aufgedeckten Punkte- und Fluchkarten auf die Hand. Lege die übrigen Karten in beliebiger Reihenfolge auf deinen Nachziehstapel zurück.  */
public class Patrouille extends AufgabeImpl {
	
	
	@SuppressWarnings("null") Karten karten;
	

	@Override public void vorbereiten() {
		zieheKarten(3);
		karten = zieheKartenKarten(4);
		super.vorbereiten();
	}
	

	@Override public boolean anzeigen() {
		if (karten.isEmpty()) {
			done();
			return false;
		} else {
			headerHandkartenTitle();
			sayln("Diese Karten hast du vom Nachziehstapel gezogen. Räume sie in beliebiger Reihenfolge auf.");
			for (Karte karte : karten) {
				if (karte.hatPunkte()) {
					button(karte, true, (handler, k) -> {
						handler.handkarten().legeAb(karte);
						karten.entferne(karte);
					});
					button("Auf die Hand nehmen", ' ', true, handler -> {
						handler.handkarten().legeAb(karte);
						karten.entferne(karte);
					});
					ln();
				} else {
					button(karte, true, (handler, k) -> {
						handler.nachziehStapel().legeAb(karte);
						karten.entferne(karte);
					});
					button("Zurück auf den Nachziehstapel", 'z', true, handler -> {
						handler.nachziehStapel().legeAb(karte);
						karten.entferne(karte);
					});
					ln();
				}
			}
			return true;
		}
	}

}
