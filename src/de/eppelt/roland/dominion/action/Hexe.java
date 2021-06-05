package de.eppelt.roland.dominion.action;

import de.eppelt.roland.dominion.Dominion;
import de.eppelt.roland.dominion.Dran;
import de.eppelt.roland.dominion.task.HexenOpfer;


/** +2 Karten und jeder Mitspieler muss einen Fluch nehmen */
public class Hexe extends AktionImpl {

	
	@Override public void ausführen(Dominion dominion, Dran dran) {
		dran.getSpieler().play("Witch.ogg");
		dran.getSpieler().zieheKarten(2);
		dran.getSpieler().angriff(HexenOpfer::new);
	}

	
}
