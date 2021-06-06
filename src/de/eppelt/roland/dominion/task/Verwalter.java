package de.eppelt.roland.dominion.task;

import de.eppelt.roland.dominion.task.HandkartenAblegenEntsorgen.Verwendung;
import de.eppelt.roland.dominion.task.HandkartenAblegenEntsorgen.Zähle;
import de.tesd.util.O;

/** Wähle eins: +2 Karten oder +2 Geld oder entsorge 2 Handkarten */
public class Verwalter extends AufgabeImpl {


	@Override public boolean anzeigen() {
		headerHandkartenTitle();
		sayln("Wähle aus");
		button("+2 Karten", 'k', true, handler -> {
			handler.getSpieler().zieheKarten(2);
			handler.spielerHat("2 Karten nachgezogen");
			done();
		});
		ln();
		button("+2 Münzen", 'm', true, handler -> {
			O.nnc(handler.getDran(), dran -> dran.addGeld(2));
			handler.spielerHat("2 Münzen erhalten");
			done();
		});
		ln();
		button("2 Handkarten entsorgen", 'e', true, handler -> {
			handler.getSpieler().sofortAufgabe(new HandkartenAblegenEntsorgen(Verwendung.ENTSORGEN, Zähle.GENAU, 2));
			done();
		});
		return true;
	}

}
