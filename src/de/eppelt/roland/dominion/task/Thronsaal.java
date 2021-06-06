package de.eppelt.roland.dominion.task;


import org.eclipse.jdt.annotation.Nullable;

import de.eppelt.roland.dominion.Dran;
import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Karten;
import de.eppelt.roland.dominion.action.Aktion;


/** Eine andere Aktion doppelt ausführen */
public class Thronsaal extends AufgabeImpl {


	private @Nullable Karten aktionen = null;

	
	@SuppressWarnings("null")
	@Override public boolean anzeigen() {
		headerHandkartenTitle();
		aktionen = handkarten().stream()
			.filter(Karte::isAktion)
			.collect(Karten.COLLECT);
		if (aktionen.isEmpty()) {
			sayln("Du hast gar keine Aktion, die du doppelt ausführen könntest.");
			button("Ach sowas!", 'a', true, handler -> done());
			ln();
		} else {
			sayln("Wähle die Aktion, die du doppelt ausführen willst:");
			oneKarte(aktionen, (handler, karte) -> {
				handler.handkarten().entferne(karte);
				handler.seite().legeAb(karte);
				Aktion aktion = karte.getAktion();
				dran(Dran::incAusgespielteAktionen);
				aktion.ausführen(handler.getInstance());
				dran(Dran::incAusgespielteAktionen);
				aktion.ausführen(handler.getInstance());
				done();
			});
			ln();
		}
		return true;
	}
	
	
}
