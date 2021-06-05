package de.eppelt.roland.dominion.task;


/** Wähle eins: +3 Karten oder +2 Aktionen */
public class Nobles extends AufgabeImpl {


	@Override public boolean execute() {
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
