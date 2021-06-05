package de.eppelt.roland.dominion.task;

/** Lege eine beliebige Handkarte verdeckt auf den Nachziehstapel. */
public class Burghof extends AufgabeImpl {
	
	
	@Override public boolean execute() {
		headerHandkartenTitle(getName());
		sayln("Welche Handkarte willst du zurück auf den Nachziehstapel legen?");
		oneKarte(handkarten(), (handler, karte) -> {
			handler.handkarten().entferne(karte);
			handler.nachziehStapel().legeAb(karte);
			handler.spielerHat(karte.getName()+" zurück auf den Nachziehstapel gelegt.");
			done();
		});
		ln();
		return true;
	}
	

}
