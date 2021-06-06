package de.eppelt.roland.dominion.task;


import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Karten;


/** Beliebig viele Handkarten ablegen und nachziehen; +1 Aktion */
public class Keller extends AufgabeImpl {

	
	@Override public void vorbereiten() {
		addAktionen(1);
	}

	@Override public boolean anzeigen() {
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

	
}
