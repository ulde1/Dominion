package de.eppelt.roland.dominion.action;


import de.eppelt.roland.dominion.Dominion;
import de.eppelt.roland.dominion.Dran;


/** +4 Karten, +1 Kauf, Jeder Mitspieler zieht eine Karte nach */
public class Ratsversammlung extends AktionImpl {


	@Override public void ausführen(Dominion dominion, Dran dran) {
		dran.getSpieler().zieheKarten(4);
		dran.addKäufe(1);
		dran.getSpieler().alleAnderenSpieler()
			.forEach(s -> {
				s.play("Draw.mp3");
				s.zieheKarten(1);
			});
	}

}
