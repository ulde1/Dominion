package de.eppelt.roland.dominion.task;

import org.eclipse.jdt.annotation.Nullable;

import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Karten;
import de.eppelt.roland.dominion.Spieler;


/** Alle Mitspieler müssen eine Punktekarte zurück auf den Nachziehstapel legen oder ihre Kartenhand offen vorzeigen.
 * @author Roland M. Eppelt
 */
public class BürokratOpfer extends OpferAufgabeImpl {

	
	protected @Nullable Karten karten = null;
	
	
	public BürokratOpfer(Spieler täter, Spieler opfer) {
		super(täter, opfer);
		setName("Bürokraten-Opfer");
	}

	
	@SuppressWarnings("null")
	@Override public boolean execute() {
		headerHandkartenTitle();
		karten = handkarten().stream()
			.filter(Karte::hatPunkte)
			.collect(Karten.COLLECT);
		if (karten.isEmpty()) {
			play("Luck.mp3");
			sayln("Glück gehabt! Sie können gar keine Punktekarte zurück  auf den Nachziehstapel legen.");
			button("OK. Puh!", 'o', true, handler -> done());
			ln();
		} else {
			play("Bureaucrat.mp3");
			sayln("Welche Punktekarte wollen Sie zurück  auf den Nachziehstapel legen?");
			oneKarte(karten, (handler, karte) -> {
				handler.handkarten().entferne(karte);
				handler.nachziehStapel().legeAb(karte);
				done();
			});
			ln();
		}
		return true;
	}
	
	
}
