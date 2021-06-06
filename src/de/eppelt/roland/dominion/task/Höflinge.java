package de.eppelt.roland.dominion.task;


import de.eppelt.roland.dominion.Karte;


/** Decke eine Handkarte auf. Für jeden Kartentyp (Aktion, Angriff ...), den sie hat, wähle eine andere Option: +1
 * Aktion / +1 Kauf / +$3 oder nimm ein Gold vom Vorrat. */
public class Höflinge extends OptionAufgabe {


	public Höflinge() {
		super(-1, Option.AKTION, Option.KAUF, Option.GELD3, Option.GOLD);
	}


	@Override public boolean anzeigen() {
		if (anzahl<0) {
			headerHandkartenTitle();
			if (handkarten().size()==0) {
				sayln("Problem. Du hast gar keine Karten zum Aufdecken auf der Hand.");
				button("Ups.", 'u', true, handler -> done());
			} else {
				sayln("Welche Handkarte deckst du auf?");
				oneKarte(handkarten(), (handler, karte) -> {
					anzahl = Karte.countKartenTypen(karte);
				});
			}
			return true;
		} else {
			return super.anzeigen();
		}
	}

}
