package de.eppelt.roland.dominion.task;


/** +2 Geld; Du darfst sofort deinen kompletten Nachziehstapel auf deinen Ablagestapel legen. */
public class Kanzler extends AufgabeImpl {


	@Override public void vorbereiten() {
		addGeld(2);
	}


	@Override public boolean anzeigen() {
		headerHandkartenTitle(getName());
		sayln("Möchtest du deinen kompletten Nachziehstapel auf deinen Ablagestapel legen?");
		button("Nein", 'n', true, handler -> done());
		button("Ja", 'j', true, handler -> {
			handler.ablage().legeAlleAbVon(handler.nachziehStapel());
			done();
		});
		return true;
	}

}
