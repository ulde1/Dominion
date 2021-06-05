package de.eppelt.roland.dominion.action;

import de.eppelt.roland.dominion.Dominion;
import de.eppelt.roland.dominion.Dran;
import de.eppelt.roland.dominion.task.MilizOpfer;


/** +2 Geld; Jeder Mitspieler muss alle bis auf drei Karten ablegen */
public class MilizAktion extends AktionImpl {
	
	
	@Override public void ausführen(Dominion dominion, Dran dran) {
		dran.addGeld(2);
		dran.getSpieler().angriff(MilizOpfer::new);
	}

	
}
