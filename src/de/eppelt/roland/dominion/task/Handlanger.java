package de.eppelt.roland.dominion.task;

/** Wähle zwei verschiedene:
+1 Karte
+1 Aktion
+1 Kauf
+$1 */
public class Handlanger extends OptionAufgabe {

	
	public Handlanger() {
		super(2, Option.KARTE, Option.AKTION, Option.KAUF, Option.GELD1);
	}


}
