package de.eppelt.roland.dominion.task;


import de.eppelt.roland.dominion.Spieler;


/** Jeder Mitspieler, der mindestens 5 Handkarten hat, muss alle ablegen und 4 Karten nachziehen.  */
public class LakaiOpfer extends OpferAufgabeImpl {
	

	public LakaiOpfer(Spieler täter, Spieler opfer) {
		super(täter, opfer);
	}
	
	
	@Override public void vorbereiten() {
		if (opfer.getNachziehStapel().size()<5) {
			setOpferstatus(Opferstatus.GESCHÜTZT);
		}
		super.vorbereiten();
	}


	/** Entscheidet, ob das {@link LakaiOpfer} alle Handkarten ablegen und 4 nachziehen muss oder nichts tun muss. */
	public void setAktiv(boolean aktiv) {
		getOpfer().updateMe();
	}

	
	@Override public boolean anzeigen() {
		headerHandkartenTitle();
		say("Drücke auf ");
		button("Alle Handkarten ablegen und 4 Karten nachziehen", 'a', true, handler -> {
			handler.seite().legeAlleAbVon(handler.handkarten());
			handler.zieheKarten(4);
			done();
		});
		return true;
	}


}
