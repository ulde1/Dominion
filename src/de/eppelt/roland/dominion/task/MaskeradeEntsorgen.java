package de.eppelt.roland.dominion.task;

public class MaskeradeEntsorgen extends AufgabeImpl {
	
	
	

	public MaskeradeEntsorgen() {
		setName("Maskerade - Karte entsorgen");
	}

	
	@Override public boolean anzeigen() {
		headerHandkartenTitle(getName());
		sayln("Welche Karte möchtest du entsorgen?");
		oneKarte(handkarten(), (handler, karte) -> {
			handler.handkarten().entferne(karte);
			handler.trash().legeAb(karte);
			spielerHat(karte.getName()+" entsorgt");
			done();
		});
		ln();
		say(" oder ");
		button("Keine Karte entsorgen", 'k', true, handler -> done());
		return true;
	}
	

}
