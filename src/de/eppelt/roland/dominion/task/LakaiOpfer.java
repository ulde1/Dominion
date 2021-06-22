package de.eppelt.roland.dominion.task;


import de.eppelt.roland.dominion.Spieler;


/** Jeder Mitspieler, der mindestens 5 Handkarten hat, muss alle ablegen und 4 Karten nachziehen.  */
public class LakaiOpfer extends OpferAufgabeImpl {
	
	
	public static final String FRIEDE_BUTTON_TEXT = "Soll mir recht sein.";
	public static final String GLÜCK_BUTTON_TEXT = "Gut so..";
	public static final String PECH_BUTTON_TEXT = "Alle Handkarten ablegen und 4 Karten nachziehen";


	public static enum Step { WARTEN, FRIEDE, GLÜCK, PECH }
	
	
	private Step step = Step.WARTEN;
	

	public LakaiOpfer(Spieler täter, Spieler opfer) {
		super(täter, opfer);
	}
	
	
	public void setAngriff(boolean angriff) {
		step = angriff
			? (getOpfer().getHandkarten().size()<5 ? Step.GLÜCK : Step.PECH)
			: Step.FRIEDE;
		getOpfer().updateMe();
	}


	@Override public boolean anzeigen() {
		headerHandkartenTitle();
		switch (step) {
			case WARTEN:
				say("Warte auf die Wahl von ");
				say(getTäter().getName());
				sayln(".");
				break;
			case FRIEDE:
				say(getTäter().getName());
				sayln(" hat die Aktion +2 Münzen gewählt. Also kein Angriff.");
				button(FRIEDE_BUTTON_TEXT, 's', true, handler -> done());
				break;
			case GLÜCK:
				sayln("Glück gehabt! Du hast weniger als  5 Karten auf der Hand. Deshalb trifft dich dieser Lakai-Angriff nicht..");
				button(GLÜCK_BUTTON_TEXT, 'g', true, handler -> done());
				break;
			case PECH:
				sayln("So ein Pech! Du musst");
				button(PECH_BUTTON_TEXT, 'a', true, handler -> {
					handler.seite().legeAlleAbVon(handler.handkarten());
					handler.zieheKarten(4);
					done();
				});
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
