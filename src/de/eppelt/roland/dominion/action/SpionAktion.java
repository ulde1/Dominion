package de.eppelt.roland.dominion.action;


import de.eppelt.roland.dominion.Dominion;
import de.eppelt.roland.dominion.Dran;
import de.eppelt.roland.dominion.task.Spion;


/** +1 Karte, +1 Aktion; Alle Spieler (auch du) decken die oberste Karte ihres Nachziehstapels auf. Du entscheidest, wer seine Karte entsorgt und wer sie wieder auf seinen Nachziehstapel zurück tun muss. */
public class SpionAktion extends AktionImpl {


	@Override public void ausführen(Dominion dominion, Dran dran) {
		dran.zieheKarten(1);
		dran.addAktionen(1);
		dran.sofortAufgabe(new Spion(dran.getSpieler()));
	}

}
