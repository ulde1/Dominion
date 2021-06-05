package de.eppelt.roland.dominion.task;

/** Nimm eine Karte vom Vorrat, die bis zu 4 Geld kostet */
public class Werkstatt extends KaufenAufgabe {
	
	
	public Werkstatt() {
		super(4, 1);
	}

	@Override public boolean execute() {
		headerHandkartenTitle();
		sayln("Du darfst dir eine Karte für bis zu 4 Münzen kaufen.");
		kauf();
		return true;
	}
	
	
}
