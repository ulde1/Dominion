package de.eppelt.roland.dominion.task;

import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Spieler.EmptyDeckException;
import de.eppelt.roland.dominion.ui.Handler;
import de.tesd.util.O;


/** +1 Kauf; Du darfst ein Anwesen ablegen. Wenn du das machst: +4. Ansonsten: Nimm dir ein Anwesen. */
public class Baron extends AufgabeImpl {
	
	
	public void ablegen(Handler handler) {
		handler.handkarten().entferne(Karte.ANWESEN);
		handler.seite().legeAb(Karte.ANWESEN);
		O.nnc(handler.getDran(), dran -> dran.addGeld(4));
		handler.spielerHat("ein Anwesen gegen 4 Münzen abgelegt.");
		done();
	}
	
	
	public void nehmen(Handler handler) {
		if (handler.vorrat().hat(Karte.ANWESEN)) {
			try {
				handler.seite().legeAb(handler.vorrat().zieheKarte(Karte.ANWESEN));
				handler.spielerHat("ein Anwesen genommen.");
			} catch (EmptyDeckException e) {
				handler.spielerHat("kein Anwesen genommen, weil der Vorrat leer war.");
			}
		}
		done();
	}


	@Override public boolean execute() {
		headerHandkartenTitle();
		if (handkarten().enthält(Karte.ANWESEN)) {
			sayln("Willst du ein Anwesen ablegen? Dann bekommst du 4 Münzen. Sonst musst du ein Anwesen nehmen.");
			button("Ablegen und 4 Münzen bekommen", 'a', true, this::ablegen);
			ln();
		} else {
			sayln("Mit einem Anwesen hättest du 4 Münzen bekommen. Ohne Anwesen musst du leider ein Anwesen nehmen.");
		}
		button("Ein Anwesen nehmen", 'e', true, this::nehmen);
		return true;
	}

	
}
