package de.eppelt.roland.dominion.task;

import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Karten;
import de.eppelt.roland.dominion.Spieler.EmptyDeckException;



/** Decke solange Karten vom Nachziehstapel auf, bis du 2 Geldkarten aufgedeckt hast. Nimm diese auf die Hand und lege die anderen aufgedeckten Karten ab.*/
public class Abenteurer extends AufgabeImpl {
	
	
	@Override public boolean execute() {
		headerHandkartenTitle(getName());
		Karten karten = new Karten();
		int geldkarten = 0;
		while (geldkarten<2) {
			try {
				Karte karte = zieheKarte(false);
				if (karte.isGeld()) {
					geldkarten++;
				}
				karten.legeAb(karte);
			} catch (EmptyDeckException e) {
				sayln(e.getMessage()+"! Weiter ziehen geht nicht mehr.");
				geldkarten = 2;
			}
		}
		sayln("Du hast diese Karten gezogen:");
		karten(karten);
		ln();
		button("Geldkarten auf die Hand nehmen, Rest ablegen", 'g', true, handler -> {
			for (Karte karte : karten) {
				if (karte.isGeld()) {
					handler.handkarten().legeAb(karte);
				} else {
					handler.seite().legeAb(karte);
				}
			}
			spielerHat(karten.stream().filter(Karte::isGeld).collect(Karten.KURZ)+" nachgezogen.");
			done();
		});
		ln();
		return true;
	}


}
