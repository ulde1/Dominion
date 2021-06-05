package de.eppelt.roland.dominion.task;

import de.eppelt.roland.dominion.Spieler;
import de.eppelt.roland.dominion.ui.UI;

public interface OpferAufgabe extends Aufgabe {
	
	
	public static enum Opferstatus { UNGESCHÜTZT, WARTEN, GESCHÜTZT;

		public String getName() {
			return name().substring(0,  1)+name().substring(1).toLowerCase();
		}

	}

	
	Opferstatus getOpferstatus();
	
	
	void setOpferstatus(Opferstatus opferstatus);
	
	
	Spieler getTäter();
	
	
	Spieler getOpfer();
	
	
	boolean isUngeschütztElseSay(UI ui);


	boolean isGeschützt();


}
