package de.eppelt.roland.dominion.task;


import org.eclipse.jdt.annotation.Nullable;

import de.eppelt.roland.dominion.Dominion;
import de.eppelt.roland.dominion.Dran;
import de.eppelt.roland.dominion.Karten;
import de.eppelt.roland.dominion.action.Aktion;


/** +1 Karte, +1 Aktion, Du darfst eine beliebe Karte aus dem Ablagestapel auf den Nachziehstapel legen */
public class Vorbotin extends AufgabeImpl implements Aktion {
	
	
	protected @Nullable Karten karten = null;


	@SuppressWarnings("null")
	@Override public boolean execute() {
		if (karten==null) {
			karten = ablage().stream().distinct().collect(Karten.COLLECT);
		}
		headerHandkartenTitle();
		if (karten.isEmpty()) {
			sayln("Das ist jetzt gerade ein bissele ungünstig: Ihr Ablagestapel ist nämlich leer. Sie können also gar keine Karte vom Ablagestapel zurück auf den Nachziehstapel legen.");
			button("Sorry. Ehrlich.", 's', true, handler -> done());
			ln();
		} else {
			sayln("Welche Karte möchtest du vom Ablagestapel zurück auf den Nachziehstapel legen?");
			oneKarte(karten, (handler, karte) -> {
				handler.ablage().entferne(karte);
				handler.nachziehStapel().legeAb(karte);
				done();
			});
			ln();
		}
		return true;
	}
	
	
		// ========== Aktion ==========
	

	@Override public boolean möglich(Dominion dominion) {
		return true;
	}
	

	@Override public void ausführen(Dominion dominion) {
		Dran dran = dominion.getDran();
		if (dran!=null) {
			dran.zieheKarten(1);
			dran.addAktionen(1);
			dran.sofortAufgabe(new Vorbotin());
		}
	}

}
