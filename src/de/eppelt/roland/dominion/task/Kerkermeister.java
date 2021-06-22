package de.eppelt.roland.dominion.task;


import de.eppelt.roland.dominion.Dran;


/** +3 Karten; Jeder Mitspieler muss entweder 2 Karten ablegen oder eine Fluchkarte vom Vorrat auf die Hand nehmen. */
public class Kerkermeister extends TäterAufgabe<KerkermeisterOpfer> {


	public Kerkermeister(Dran dran) {
		super(dran, KerkermeisterOpfer::new);
	}
	
	
	@Override public void vorbereiten() {
		getSpieler().zieheKarten(3);
		super.vorbereiten();
	}
	
	
	@Override protected void showHeader() {
		super.showHeader();
		button("Weiter! Aber zackig!", 'w', true, handler -> done());
		ln();
	}


	@Override protected void opferAnzeigen(KerkermeisterOpfer opfer) {
		sayln(opfer.getMessage());
	}
	
	
}
