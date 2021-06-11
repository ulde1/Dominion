package de.eppelt.roland.dominion.task;

import de.eppelt.roland.dominion.Dran;
import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Spieler.EmptyDeckException;


/** Nimm ein Gold vom Vorrat; Jeder Mitspieler entsorgt eine der beiden obersten Geldkarten und legt den Rest ab */
public class Banditin extends TäterAufgabe<BanditinOpfer> {
	
	
	boolean glück = false;
	
	
	public Banditin(Dran täter) {
		super(täter, BanditinOpfer::new);
	}


	@Override public void vorbereiten() {
		try {
			spieler.getSeite().legeAb(vorrat().zieheKarte(Karte.GOLD));
			glück = true;
		} catch (EmptyDeckException e) {
			// glück bleibt false;
		}
		super.vorbereiten();
	}
	
	
	@Override protected void showHeader() {
		super.showHeader();
		if (glück) {
			sayln("Du bekommst ein Gold auf deinen Ablagestapel.");
			button(Karte.GOLD, true, (handler, karte) -> done());
		} else {
			sayln("Es gibt leider kein Gold mehr im Vorrat, das du nachziehen könntest. Schade.");
			sayln("Aber die anderen Mitspieler müssen trotzdem eine ihrer beiden obersten Geldkarten entsorgen! Ha!");
			button("OK", 'o', true, client -> done());
		}
		ln();
	}
	

	@Override protected void opferAnzeigen(BanditinOpfer opfer) {
		Karte entsorgt = opfer.getEntsorgt();
		if (opfer.getCount()<=0) {
			say(opfer.getSpieler().getName());
			sayln(" hat keine Geldkarte gezogen.");
		} else if (entsorgt==null) {
			say(opfer.getSpieler().getName());
			sayln(" denkt noch nach.");
		} else {
			say(opfer.getSpieler().getName());
			sayln(" hat ein ");
			karte(entsorgt);
			lnsayln(" entsorgt.");
		}
	}

		
}