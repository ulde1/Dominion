package de.eppelt.roland.dominion.task;

import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Spieler;
import de.eppelt.roland.dominion.Spieler.EmptyDeckException;
import de.eppelt.roland.dominion.ui.Handler;


/** Jeder Mitspieler muss einen Fluch nehmen. */
public class HexenOpfer extends OpferAufgabeImpl {
	
	
	public void gibFluch(Handler handler) {
		try {
			handler.seite().legeAb(handler.vorrat().zieheKarte(Karte.FLUCH));
		} catch (EmptyDeckException e) {
			// Das ist doch nicht möglich!
		}
		done();
	};


	public HexenOpfer(Spieler täter, Spieler opfer) {
		super(täter, opfer);
	}

	
	@Override public boolean anzeigen() {
		headerHandkartenTitle();
		if (vorrat().hat(Karte.FLUCH)) {
			play("Curse.mp3");
			sayln("Du bekommst einen Fluch:");
			button(Karte.FLUCH, true, (handler, karte)  -> gibFluch(handler));
			ln();
			button("Nicht gut.", 'n', true, handler -> gibFluch(handler));
			ln();
		} else {
			play("Luck.mp3");
			sayln("So ein Glück: Flüche sind aus");
			button("Ätsch!", 'n', true, handler -> done());
			ln();
		}
		return true;
		
	}
	
	
	}
