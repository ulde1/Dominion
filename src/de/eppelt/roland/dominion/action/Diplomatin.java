package de.eppelt.roland.dominion.action;


import de.eppelt.roland.dominion.Dominion;
import de.eppelt.roland.dominion.Dran;
import de.eppelt.roland.dominion.task.HandkartenAblegenEntsorgen;
import de.eppelt.roland.dominion.task.HandkartenAblegenEntsorgen.Verwendung;
import de.eppelt.roland.dominion.task.HandkartenAblegenEntsorgen.Zähle;
import de.eppelt.roland.dominion.task.OpferAufgabe;
import de.eppelt.roland.dominion.task.Schutz;
import de.eppelt.roland.dominion.ui.Handler;


/** +2 Karten; Hast du nach dem Ziehen 5 oder weniger Handkarten: +2 Aktionen.
 * Wenn ein Mitspieler eine Angriffskarte ausspielt und du mindestens 5 Handkarten hast, darfst du diese Karte aus deiner Hand aufdecken.
 * Wenn du das tust: Ziehe 2 Karten und lege dann 3 Karten ab. */
public class Diplomatin extends AktionImpl implements Reaktion {

	
	@Override protected void ausführen(Dominion dominion, Dran dran) {
		dran.zieheKarten(2);
		if (dran.handkarten().size()<=5) {
			dran.addAktionen(2);
		}
	}
	

	@Override public boolean reaktionMöglich(OpferAufgabe aufgabe) {
		return aufgabe.getOpfer().getHandkarten().size()>=5;
	}


	@Override public void reagiere(Schutz schutz, Handler handler) {
		if (handler.handkarten().size()>=5) {
			handler.zieheKarten(2);
			handler.sofortAufgabe(new HandkartenAblegenEntsorgen(Verwendung.ABLEGEN, Zähle.GENAU, 3));
		}
	}


}
