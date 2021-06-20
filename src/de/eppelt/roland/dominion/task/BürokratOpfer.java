package de.eppelt.roland.dominion.task;

import org.eclipse.jdt.annotation.Nullable;

import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Karten;
import de.eppelt.roland.dominion.Spieler;


/** Alle Mitspieler müssen eine Punktekarte zurück auf den Nachziehstapel legen oder ihre Kartenhand offen vorzeigen.
 * @author Roland M. Eppelt
 */
public class BürokratOpfer extends OpferAufgabeImpl {
	
	
	public static enum Step { PRE, GLÜCK, PECH, FERTIG };

	
	public static final String BÜROKRATEN_OPFER = "Bürokraten-Opfer";
	public static final String OK = "OK. Puh!";
	
	
	protected @Nullable Karten karten = null;
	public Step step = Step.PRE; 
	public @Nullable Karte zurück = null;
	
	
	public BürokratOpfer(Spieler täter, Spieler opfer) {
		super(täter, opfer);
		setName(BÜROKRATEN_OPFER);
	}
	
	
	@Override public void vorbereiten() {
		super.vorbereiten();
		karten = handkarten().stream()
				.filter(Karte::hatPunkte)
				.collect(Karten.COLLECT);
		step = karten.isEmpty() ? Step.GLÜCK : Step.PECH;
	}

	
	@SuppressWarnings("null")
	@Override public boolean anzeigen() {
		headerHandkartenTitle();
		switch (step) {
		case GLÜCK:
			play("Luck.mp3");
			sayln("Glück gehabt! Du kannst gar keine Punktekarte zurück  auf den Nachziehstapel legen.");
			button(OK, 'o', true, handler -> done());
			ln();
			break;
		case PECH:
			play("Bureaucrat.mp3");
			sayln("Welche Punktekarte willst du zurück  auf den Nachziehstapel legen?");
			oneKarte(karten, (handler, karte) -> {
				zurück = karte;
				handler.handkarten().entferne(karte);
				handler.nachziehStapel().legeAb(karte);
				step = Step.FERTIG;
				done();
				getTäter().updateMe();
			});
			ln();
			break;
		default:
			say("Der Step ");
			say(step.name());
			sayln(" darf eigentlich nicht vorkommen.");
			button("Ich sag's dem Programmierer.", 'i', true, handler -> done());
			break;
		}
		return true;
	}
	
	
}
