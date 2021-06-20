package de.eppelt.roland.dominion.task;


import de.eppelt.roland.dominion.Dran;
import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Spieler.EmptyDeckException;
import de.tesd.util.O;


/** Nimm ein Silber vom Vorrat auf deinen Nachziehstapel. 
 * Alle Mitspieler müssen eine Punktekarte zurück auf den Nachziehstapel legen oder ihre Kartenhand offen vorzeigen.
 * @author Roland M. Eppelt */
public class Bürokrat extends TäterAufgabe<BürokratOpfer> {
	
	
	public Bürokrat(Dran täter) {
		super(täter, BürokratOpfer::new);
	}


	@Override public void vorbereiten() {
		super.vorbereiten();
	}


	@Override public void showHeader() {
		super.showHeader();
		if (vorrat().hat(Karte.SILBER)) {
			sayln("Nimm dieses Silber auf deinen Nachziehstapel.");
			button(Karte.SILBER, true, (handler, karte) -> {
				try {
					handler.nachziehStapel().legeAb(vorrat().zieheKarte(karte));
				} catch (EmptyDeckException e) {
				}
				done();
			});
			ln();
		} else {
			sayln("Oh! Es gibt gar keine Silber mehr, das du auf deinen Nachziehstapel legen könntest.");
			button("Sehr schade.", 's', true, client -> done());
			ln();
		}
	}


	@Override protected void opferAnzeigen(BürokratOpfer opfer) {
		switch (opfer.step) {
			case PRE:
				say(opfer.getOpfer().getName());
				sayln(" bereitet sich noch vor.");
				break;
			case GLÜCK:
				say(opfer.getOpfer().getName());
				sayln(" hat Glück gehabt: keine Punktekarte auf der Hand.");
				break;
			case PECH:
				say(opfer.getOpfer().getName());
				sayln(" hat Pech und sucht aus, welche Punktekarte er zurück auf seinen Nachziehstapel legt.");
				break;
			case FERTIG:
				say(opfer.getOpfer().getName());
				say(" hat 1x ");
				say(O.nnOr(opfer.zurück, Karte::getName, " <keine Karte???> "));
				sayln(" zurück auf seinen Nachziehstapel gelegt.");
				break;
			}
	}
	
	
}
