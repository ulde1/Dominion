package de.eppelt.roland.dominion.task;


import de.eppelt.roland.dominion.Karte;


/** +1 Karte, +2 Aktionen; Du darfst diese Karte für +$2 entsorgen. */
public class Bergwerk extends AufgabeImpl {


	@Override public void vorbereiten() {
		zieheKarten(1);
		addAktionen(2);
	}


	@Override public boolean anzeigen() {
		headerHandkartenTitle();
		sayln("Möchtest du das Bergwerk für 2 Münzen entsorgen?");
		button("Nein, Bergwerk behalten", 'n', true, handler -> done());
		button("Entsorgen für 2 Münzen", 'e', true, handler -> {
			handler.seite().entferne(Karte.BERGWERK);
			handler.trash().legeAb(Karte.BERGWERK);
			handler.addGeld(2);
			done();
		});
		return true;
	}

}
