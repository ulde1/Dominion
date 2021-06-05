package de.eppelt.roland.dominion.task;


import de.eppelt.roland.dominion.Dominion;
import de.eppelt.roland.dominion.Dran;
import de.eppelt.roland.dominion.action.Aktion;


/** +2 Geld; Du darfst sofort deinen kompletten Nachziehstapel auf deinen Ablagestapel legen. */
public class Kanzler extends AufgabeImpl implements Aktion {
	

	@Override public boolean execute() {
		headerHandkartenTitle(getName());
		sayln("Möchtest du deinen kompletten Nachziehstapel auf deinen Ablagestapel legen?");
		button("Nein", 'n', true, handler -> done());
		button("Ja", 'j', true, handler -> {
			handler.ablage().legeAlleAbVon(handler.nachziehStapel());
			done();
		});
		return true;
	}


	@Override public boolean möglich(Dominion dominion) {
		return true;
	}


	@Override public void ausführen(Dominion dominion) {
		Dran dran = dominion.getDran();
		if (dran!=null) {
			dran.addGeld(2);
			dran.getSpieler().sofortAufgabe(new Kanzler());
		}
	}

}
