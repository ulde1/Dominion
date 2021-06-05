package de.eppelt.roland.dominion.task;


import de.eppelt.roland.dominion.Karte;


/** Entsorge ein Kupfer aus deiner Hand und erhalte dafür 3 Geld */
public class Geldverleiher extends AufgabeImpl {
	
	
	@Override public boolean execute() {
		headerHandkartenTitle();
		if (handkarten().stream().anyMatch(k -> k==Karte.KUPFER)) {
			sayln("Möchtest du dieses Kupfer aus deiner Hand gegen 3 Münzen entsorgen?");
			karte(Karte.KUPFER);
			ln();
			button("Ja, gegen 3 Münzen entsorgen", 'j', true, handler -> {
				handler.handkarten().entferne(Karte.KUPFER);
				handler.trash().legeAb(Karte.KUPFER);
				dran(d -> d.addGeld(3));
				done();
			});
			button("Nein, Kupfer behalten", 'n', true, handler -> done());
			ln();
		} else {
			sayln("Du hast doch gar kein Kupfer auf der Hand, das du gegen 3 Münzen entsorgen könntest.");
			button("Ach so.", 'a', true, clinet -> done());
			ln();
		}
		return true;
	}
	
	
}
