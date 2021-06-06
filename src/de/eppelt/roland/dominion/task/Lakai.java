package de.eppelt.roland.dominion.task;


import de.eppelt.roland.dominion.Dran;


/** +1 Aktion; Wähle eins: +$2 oder lege alle deine Handkarten ab: +4 Karten. Jeder Mitspieler, der mindestens 5
 * Handkarten hat, muss alle Handkarten ablegen und 4 Karten nachziehen. */
public class Lakai extends TäterAufgabe<LakaiOpfer> {


	public Lakai(Dran dran) {
		super(dran, LakaiOpfer::new);
	}
	
	
	@Override public void vorbereiten() {
		getSpieler().addAktionen(1);
		super.vorbereiten();
	}


	@Override public boolean anzeigen() {
		headerHandkartenTitle();
		sayln("Entscheide:");
		button("+2 Geldmünzen", 'g', true, handler -> {
			handler.addGeld(2);
			for (LakaiOpfer opfer : opfers) {
				opfer.setAktiv(false);
			}
			done();
		});
		ln();
		button("Handkarten ablegen und 4 neue Karten nachziehen", 'h', true, handler -> {
			handler.seite().legeAlleAbVon(handler.handkarten());
			handler.zieheKarten(4);
			for (LakaiOpfer opfer : opfers) {
				opfer.setAktiv(true);
			}
			done();
		});
		lnsayln("Dann müssen auch alle Mitspieler mit mindestens 5 Handkarten alle Handkarten ablegen und 4 Karten nachziehen.");
		super.anzeigen();
		return true;
	}


	@Override protected void executeHeader() {
	}


	@Override protected void opferAnzeigen(LakaiOpfer opfer) {
		say("Status: ");
		sayln(opfer.getOpferstatus().getName());
	}

}
