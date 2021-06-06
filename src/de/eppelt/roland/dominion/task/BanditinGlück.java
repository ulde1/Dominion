package de.eppelt.roland.dominion.task;


import de.eppelt.roland.dominion.Karte;


public class BanditinGlück extends AufgabeImpl {


	@Override public boolean anzeigen() {
		headerHandkartenTitle();
		sayln("Du bekommst ein Gold auf deinen Ablagestapel.");
		button(Karte.GOLD, true, (handler, karte) -> done());
		ln();
		return true;
	}


}