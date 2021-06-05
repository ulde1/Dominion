package de.eppelt.roland.dominion.task;

import de.eppelt.roland.dominion.Dran;
import de.eppelt.roland.dominion.Karte;

/** +1 Karte, +2 Aktionen; Du darfst diese Karte für +$2 entsorgen. */
public class Bergwerk extends AufgabeImpl {
	
	
	public Bergwerk(Dran dran) {
		dran.zieheKarten(1);
		dran.addAktionen(2);
	}
	

	@Override public boolean execute() {
		headerHandkartenTitle();
		sayln("Möchtest du das Bergwerk für 2 Münzen entsorgen?");
		button("Nein", 'n', true, handler -> done());
		button("Entsorgen für 2 Münzen", 'e', true, handler -> {
			handler.seite().entferne(Karte.BERGWERK);
			handler.trash().legeAb(Karte.BERGWERK);
			handler.addGeld(2);
			done();
		});
		return true;
	}
	

}
