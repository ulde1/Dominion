package de.eppelt.roland.dominion.task;


import org.eclipse.jdt.annotation.Nullable;

import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Spieler.EmptyDeckException;


/** Ziehe nach bis 7 Handkarten (du darfst Aktionskarten zur Seite legen, die anschließend abgelegt werden) */
public class Bibliothek extends AufgabeImpl {


	private @Nullable Karte karte = null;


	@SuppressWarnings("null")
	@Override public boolean execute() {
		if (karte!=null && handkarten().size()>=7) {
			done();
			return false;
		} else {
			headerHandkartenTitle();
			if (handkarten().size()>=7) {
				sayln("Du hast doch schon 7 Karten auf der Hand!");
				button("Stimmt ja!", 's', true, handler -> done());
				ln();
			} else {
				try {
					karte = zieheKarte(false);
					if (karte.isAktion()) {
						sayln("Möchtest du diese neu gezogene Aktionskarte ablegen oder auf die Hand nehmen?");
						karte(karte);
						ln();
						button("Ablegen", 'a', true, handler -> {
							handler.seite().legeAb(karte);
						});
						button("Auf die Hand nehmen", 'h', true, handler -> {
							handler.handkarten().legeAb(karte);
						});
						ln();
					} else {
						sayln("Du hast diese Karte neu gezogen:");
						handkarten().legeAb(karte);
						button(karte, true, (handler, karte) -> {});
						ln();
						button("OK", 'o', true, handler -> {});
						ln();
					}
				} catch (EmptyDeckException e) {
					sayln(e.getMessage()+"! Weiter ziehen geht nicht mehr.");
					done();
				}
			}
			return true;
		}
	}
	
	
}
