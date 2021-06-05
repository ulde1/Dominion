package de.eppelt.roland.dominion.action;

import de.eppelt.roland.dominion.Dominion;
import de.eppelt.roland.dominion.Dran;
import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Spieler;
import de.eppelt.roland.dominion.Spieler.EmptyDeckException;
import de.eppelt.roland.dominion.Vorrat;
import de.eppelt.roland.dominion.task.Aufgabe;
import de.eppelt.roland.dominion.task.BanditinGlück;
import de.eppelt.roland.dominion.task.BanditinOpfer;
import de.eppelt.roland.dominion.task.BanditinPech;


/** Nimm ein Gold vom Vorrat; Jeder Mitspieler entsorgt eine der beiden obersten Geldkarten und legt den Rest ab */
public class Banditin extends AktionImpl {
	
	
	@Override public void ausführen(Dominion dominion, Dran dran) {
		Spieler spieler = dran.getSpieler();
		Aufgabe aufgabe;
		Vorrat vorrat = dominion.getVorrat();
		try {
			spieler.getSeite().legeAb(vorrat.zieheKarte(Karte.GOLD));
			aufgabe = new BanditinGlück();
		} catch (EmptyDeckException e) {
			aufgabe = new BanditinPech();
		}
		aufgabe.setName(getName());
		spieler.sofortAufgabe(aufgabe);
		spieler.angriff(BanditinOpfer::new);
	}

		
}