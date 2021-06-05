package de.eppelt.roland.dominion.task;


import de.eppelt.roland.dominion.Dran;


/** +3 Karten; Jeder Mitspieler muss entweder 2 Karten ablegen oder eine Fluchkarte vom Vorrat auf die Hand nehmen. */
public class Kerkermeister extends TäterAufgabe<KerkermeisterOpfer> {


	public Kerkermeister(Dran dran) {
		super(dran.getSpieler(), KerkermeisterOpfer::new);
		dran.zieheKarten(3);
	}


	@Override protected void executeOpfer(KerkermeisterOpfer opfer) {
	}
	
	
	@Override public boolean execute() {
		done();
		return false;
	}
	

}
