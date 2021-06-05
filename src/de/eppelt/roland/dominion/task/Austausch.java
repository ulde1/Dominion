package de.eppelt.roland.dominion.task;

import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Spieler;
import de.eppelt.roland.dominion.ui.Handler;

/** Entsorge eine Handkarte. Nimm eine Karte vom Vorrat, die bis zu 2 mehr kostet als die entsorgte Karte.
 * Ist die genommene Karte eine Aktions- oder Geldkarte, lege sie auf deinen Nachziehstapel.
 * Ist es eine Punktekarte, nimmt jeder Mitspieler einen Fluch vom Vorrat.  */
public class Austausch extends Umbau {
	

	/** Jeder Mitspieler nimmt einen Fluch vom Vorrat. */
	public class AustauschOpfer extends HexenOpfer {
		public AustauschOpfer(Spieler täter, Spieler opfer) {
			super(täter, opfer);
		}
	}
	
	
	@Override public void neueKarteAblegen(Handler handler, Karte karte) {
		if (karte.isAktion() || karte.isGeld()) {
			handler.nachziehStapel().legeAb(karte);
		} else {
			super.neueKarteAblegen(handler, karte);
		}
		if (karte.hatPunkte()) {
			handler.angriff(AustauschOpfer::new);
		}
	}
	
	
}
