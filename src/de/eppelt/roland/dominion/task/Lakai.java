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
			// 	kein super.vorbereiten();

	}


	@Override protected void showHeader() {
	}


	@Override protected void opferAnzeigen(LakaiOpfer opfer) {
		say("Status: ");
		sayln(opfer.getOpferstatus().getName());
	}
	

	@Override public boolean anzeigen() {
		headerHandkartenTitle();
		sayln("Entscheide:");
		button("+2 Geldmünzen", 'g', true, handler -> {
			handler.addGeld(2);
			done();
		});
		ln();
		button("Handkarten ablegen und 4 neue Karten nachziehen", 'h', true, handler -> {
			handler.seite().legeAlleAbVon(handler.handkarten());
			handler.zieheKarten(4);
			super.vorbereiten(); // Das aktiviert die Opfer
			done();
		});
		lnsayln("Dann müssen auch alle Mitspieler mit mindestens 5 Handkarten alle Handkarten ablegen und 4 Karten nachziehen.");
		super.anzeigen();
		return true;
	}


}
