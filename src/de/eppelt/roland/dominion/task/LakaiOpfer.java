package de.eppelt.roland.dominion.task;


import de.eppelt.roland.dominion.Spieler;


/** Jeder Mitspieler, der mindestens 5 Karten auf der Hand hat, muss alle  (HiG) | Handkarten hat, muss alle (ASS) Handkarten ablegen und 4 Karten nachziehen.  */
public class LakaiOpfer extends OpferAufgabeImpl {
	

	public static enum Step { WARTEN, NICHTS, ABLEGEN }
	
	
	Step step = Step.WARTEN;


	public LakaiOpfer(Spieler täter, Spieler opfer) {
		super(täter, opfer);
		if (opfer.getNachziehStapel().size()<5) {
			setOpferstatus(Opferstatus.GESCHÜTZT);
		}
	}


	public void setAktiv(boolean aktiv) {
		step = aktiv ? Step.ABLEGEN : Step.NICHTS;
		getOpfer().updateMe();
	}

	
	@Override public boolean execute() {
		headerHandkartenTitle();
		switch (step) {
			case WARTEN:
				say("Bitte warte, bis ");
				say(getTäter().getName());
				sayln(" sich entschieden hat.");
				break;
			case ABLEGEN:
				say("Drücke auf ");
				button("Alle Handkarten ablegen und 4 Karten nachziehen", 'a', true, handler -> {
					handler.seite().legeAlleAbVon(handler.handkarten());
					handler.zieheKarten(4);
					done();
				});
				break;
			case NICHTS:
				say("Nichts passiert. ");
				say(getTäter().getName());
				sayln(" hat sich anders entschieden.");
				button("OK", 'o', true, handler -> done());
				break;
			default:
				sayln("Internes Problem: Diese Situation darf eigentlich nicht vorkommen.");
				button("OK", 'o', true, handler -> done());
				break;
		}
		return true;
	}


}
