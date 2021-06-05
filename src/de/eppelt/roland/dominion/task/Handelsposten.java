package de.eppelt.roland.dominion.task;

import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Spieler.EmptyDeckException;
import de.eppelt.roland.dominion.ui.Handler;


/** Entsorge 2 Karten aus deiner Hand. Wenn du das machst: Nimm dir ein Silber auf die Hand. */
public class Handelsposten extends HandkartenAblegenEntsorgen {


	public Handelsposten() {
		super(Verwendung.ENTSORGEN, Zähle.GENAU, 2);
	}
	
	
	@Override protected void nachher(Handler handler) {
		super.nachher(handler);
		try {
			handler.handkarten().legeAb(handler.vorrat().zieheKarte(Karte.SILBER));
		} catch (EmptyDeckException e) {
			// Pech sowas.
		}
	}

}
