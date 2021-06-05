package de.eppelt.roland.dominion.action;


import java.util.ArrayList;

import de.eppelt.roland.dominion.Dominion;
import de.eppelt.roland.dominion.Dran;
import de.eppelt.roland.dominion.Spieler;
import de.eppelt.roland.dominion.task.Maskerade;
import de.eppelt.roland.dominion.task.MaskeradeEntsorgen;


/** +2 Karten; Alle Spieler (auch du selbst) müssen gleichzeitig eine Karte aus ihrer Hand an ihren linken Nachbarn weiter geben. Danach darfst du eine Karte aus deiner Hand entsorgen. */
public class MaskeradeAktion extends AktionImpl {
	
	
	@Override protected void ausführen(Dominion dominion, Dran dran) {
		dran.getSpieler().zieheKarten(2);
		ArrayList<Maskerade> gebend = new ArrayList<>();
		ArrayList<Maskerade> wartend = new ArrayList<>();
		for (Spieler spieler : dominion.getSpieler()) {
			if (!spieler.getHandkarten().isEmpty()) {
				gebend.add(new Maskerade(spieler, gebend, wartend));
			}
		}
		dran.getSpieler().sofortAufgabeOhneUpdate(new MaskeradeEntsorgen()); // sofort nach Maskerade
		for (int i = 0; i<gebend.size(); i++) {
			Maskerade maskerade = gebend.get(i);
			maskerade.setIndex(i);
			maskerade.getSpieler().sofortAufgabeOhneUpdate(maskerade);
		}
		dominion.updateAllPlayers();
	}

}
