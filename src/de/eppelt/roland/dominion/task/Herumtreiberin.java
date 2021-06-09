package de.eppelt.roland.dominion.task;


import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Karten;
import de.eppelt.roland.dominion.Spieler.EmptyDeckException;


/** +1 Aktion; Wähle eins: Entsorge eine Aktionskarte vom Vorrat oder nimm eine Aktionskarte vom Müll.  */
public class Herumtreiberin extends AufgabeImpl {
	
	
	public static enum Status { KEIN, ENTSORGEN, NEHMEN };
	
	
	Status status = Status.KEIN;
	
	
	@Override public void vorbereiten() {
		addAktionen(1);
		super.vorbereiten();
	}


	@Override public boolean anzeigen() {
		headerHandkartenTitle(getName());
		switch (status) {
			case KEIN: {
				sayln("Wähle:");
				button("Entsorge eine Aktionskarte vom Vorrat", 'e', true, handler -> {
					status = Status.ENTSORGEN;
				});
				ln();
				button("Nimm eine Aktionskarte vom Müll", 'n', true, handler -> {
					status = Status.NEHMEN;
				});
				return true;
			} case ENTSORGEN: {
				sayln("Welche Aktionskarte vom Vorrat willst du entsorgen?");
				Karten karten = vorrat().getVolleStapel().stream()
					.filter(Karte::isAktion)
					.collect(Karten.COLLECT);
				oneKarte(karten, (handler, karte) -> {
					try {
						handler.trash().legeAb(handler.vorrat().zieheKarte(karte));
					} catch (EmptyDeckException e) {
						// Kann ja eigentlich nicht auftreten.
					}
					handler.spielerHat(karte.getName()+" vom Vorrat entsorgt.");
					done();
				});
				return true;
			}
			case NEHMEN: {
				Karten karten = trash().stream()
					.filter(Karte::isAktion)
					.collect(Karten.COLLECT);
				if (karten.isEmpty()) {
					sayln("Ups. Es gibt gar keine Aktionskarten auf dem Müll.");
					button("Schade", 's', true, handler -> done());
				} else {
					sayln("Welche Aktionskarte willst du vom Müll nehmen?");
					oneKarte(karten, (handler, karte) -> {
						handler.trash().entferne(karte);
						handler.seite().legeAb(karte);
						handler.spielerHat(karte.getName()+" vom Müll genommen.");
						done();
					});
				}
				return true;
			}
			default: {
				done();
				return false;
			}
		}
	}

}
