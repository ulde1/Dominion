package de.eppelt.roland.dominion.action;


import de.eppelt.roland.dominion.Dominion;
import de.eppelt.roland.dominion.Dran;
import de.eppelt.roland.dominion.Karte;


/** +2 Aktionen; Decke deine Handkarten auf. Wenn du keine Aktionskarte auf der Hand hast: +2 Karten */
public class ShantyTown extends AktionImpl {


	@Override protected void ausführen(Dominion dominion, Dran dran) {
		dran.addAktionen(2);
		long count = dran.getSpieler().getHandkarten().stream()
			.filter(Karte::isAktion)
			.count();
		if (count==0) {
			dran.getSpieler().zieheKarten(2);
			dran.spielerHat("2 Karten nachgezogen");
		}

	}

}
