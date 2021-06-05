package de.eppelt.roland.dominion.task;


import de.eppelt.roland.dominion.Karte;


/** Entsorge diese Karte. Nimm dafür eine Karte vom Vorrat, die höchstens 5 kostet. */
public class Festmahl extends KaufenAufgabe {
	
	
	boolean entsorgt = false;


	public Festmahl() {
		super(5, 1);
	}


	@Override public boolean execute() {
		headerHandkartenTitle(getName());
		if (!entsorgt) {
			seite().entferne(Karte.FESTMAHL);
			entsorgt = true;
		}
		sayln("Du hast dieses Festmahl entsorgt und darfst dafür jetzt eine Karte bis zu 5 Geldmünzen kaufen.");
		kauf();
		return true;
	}

}
