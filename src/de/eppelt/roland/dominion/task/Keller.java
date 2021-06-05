package de.eppelt.roland.dominion.task;


import de.eppelt.roland.dominion.Dominion;
import de.eppelt.roland.dominion.Dran;
import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Karten;
import de.eppelt.roland.dominion.action.Aktion;


/** Beliebig viele Handkarten ablegen und nachziehen; +1 Aktion */
public class Keller extends AufgabeImpl implements Aktion {


		// ========== Aufgabe ==========


	@Override public boolean execute() {
		headerHandkartenTitle();
		sayln("Markiere die Handkarten, die du ablegen möchtest, und ziehe gleich viele Karten nach:");
		String indexKey = any(handkarten(), null);
		ln();;
		say("Drücke anschließend auf ");
		button("Ablegen", 'a', false, handler -> {
			int[] index = handler.getIndex(indexKey);
			String count = Integer.toString(index.length);
			String n = index.length==1 ? "" : "n";
			handler.spielerHat(count+" Handkarte"+n+" abgelegt und "+count+" Karte"+n+" nachgezogen.");
			Karten handkarten = handler.handkarten();
			Karten ablage = handler.ablage();
				// ablegen
			for (int i : index) {
				Karte karte = handkarten.ziehe(i);
				ablage.legeAb(karte);
			}
			handler.getSpieler().zieheKarten(index.length);
			done();
			
		});
		ln();
		return true;
		
	}

	
		// ========== Aktion ==========


	@Override public boolean möglich(Dominion dominion) {
		return true;
	}


	@Override public void ausführen(Dominion dominion) {
		Dran dran = dominion.getDran();
		if (dran!=null) {
			dran.addAktionen(1);
			dran.getSpieler().sofortAufgabe(new Keller());
		}
	}
	
	
}
