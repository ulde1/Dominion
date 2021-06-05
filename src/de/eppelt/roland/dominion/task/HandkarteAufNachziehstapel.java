package de.eppelt.roland.dominion.task;

public class HandkarteAufNachziehstapel extends AufgabeImpl {


	public HandkarteAufNachziehstapel() {
		setName("Handkarte ablegen");
	}


	@Override public boolean execute() {
		fine("append");
		headerHandkartenTitle();
		sayln("Wähle die Karte aus, die du zurück auf den Nachziehstapel legen willst:");
		oneKarte(handkarten(), (handler, karte) -> {
			handler.handkarten().entferne(karte);
			handler.nachziehStapel().legeAb(karte);
			handler.spielerHat(karte.getName() + " zurück auf seinen Nachziehstapel gelegt.");
			done();
		});
		return true;
	}


}
