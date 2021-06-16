package de.eppelt.roland.dominion.task;


/** Wähle eins: +3 Karten oder +2 Aktionen */
public class Adelige extends AufgabeImpl {


	@Override public boolean anzeigen() {
		headerHandkartenTitle();
		sayln("Wähle:");
		button("+3 Karten", 'k', true, handler -> {
			handler.zieheKarten(3);
			done();
		});
		ln();
		button("+2 Aktionen", 'k', true, handler -> {
			handler.addAktionen(2);
			done();
		});		
		return true;
	}

}
