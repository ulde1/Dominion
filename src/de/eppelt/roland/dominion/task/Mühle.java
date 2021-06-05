package de.eppelt.roland.dominion.task;


import de.eppelt.roland.dominion.Dran;
import de.eppelt.roland.dominion.ui.Handler;


/** +1 Karte, +1 Aktion; Du darfst 2 Handkarten ablegen. Wenn du das tust: +$2. 1 Siegpunkt. */
public class Mühle extends HandkartenAblegenEntsorgen {


	public Mühle(Dran dran) {
		super(Verwendung.ABLEGEN, Zähle.BISZU, 2);
		dran.zieheKarten(1);
		dran.addAktionen(1);
	}


	@Override protected void vorherHinweis() {
		sayln("Du darfst 2 Handkarten ablegen, für die du dann 2 Münzen erhältst.");
	}
	
	
	@Override protected void nachher(Handler handler) {
		super.nachher(handler);
		if (anzahl==0) {
			handler.addGeld(2);
		}
	}

}
