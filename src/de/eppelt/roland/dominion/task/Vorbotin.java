package de.eppelt.roland.dominion.task;


import org.eclipse.jdt.annotation.Nullable;

import de.eppelt.roland.dominion.Dran;
import de.eppelt.roland.dominion.Karten;


/** +1 Karte, +1 Aktion, Du darfst eine beliebe Karte aus dem Ablagestapel auf den Nachziehstapel legen */
public class Vorbotin extends DranAufgabeImpl {
	
	
	protected @Nullable Karten karten = null;
	
	
	public Vorbotin(Dran dran) {
		super(dran);
	}


	
	@Override public void vorbereiten() {
		dran.zieheKarten(1);
		dran.addAktionen(1);
		super.vorbereiten();
	}

	@SuppressWarnings("null")
	@Override public boolean anzeigen() {
		if (karten==null) {
			karten = ablage().stream().distinct().collect(Karten.COLLECT);
		}
		headerHandkartenTitle();
		if (karten.isEmpty()) {
			sayln("Das ist jetzt gerade ein bissele ungünstig: Dein Ablagestapel ist nämlich leer. Du kannst also gar keine Karte vom Ablagestapel zurück auf den Nachziehstapel legen.");
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
	
	
}
