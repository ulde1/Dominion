package de.eppelt.roland.dominion.task;


import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Karten;
import de.eppelt.roland.dominion.Spieler;


/** Jeder Mitspieler muss alle bis auf drei Karten ablegen */
public class MilizOpfer extends OpferAufgabeImpl {


	public MilizOpfer(Spieler täter, Spieler opfer) {
		super(täter, opfer);
		setName("Miliz-Opfer");
	}


	@Override public boolean anzeigen() {
		Karten handkarten = handkarten();
		int anzahl = handkarten.size()-3;
		if (anzahl>0) {
			play("Luck.mp3");
			headerHandkartenTitle();
			say("Du musst leider ");
			say(anzahl);
			say(anzahl==1 ? " Handkarte" : " Handkarten");
			sayln(" ablegen. Bitte wähle aus, welche:");
			String indexKey = any(handkarten, null);
			ln();
			say("Drücke anschließend auf ");
			button("Ablegen", 'a', false, handler -> {
				Karten ablage = handler.ablage();
				// ablegen
				int[] index = handler.getIndex(indexKey);
				for (int i : index) {
					Karte karte = handkarten.ziehe(i);
					ablage.legeAb(karte);
				}
				if (handkarten.size()<=3) {
					done();
				}
			});
			ln();
			return true;
		} else {
			done();
			return false;
		}
	}

}
