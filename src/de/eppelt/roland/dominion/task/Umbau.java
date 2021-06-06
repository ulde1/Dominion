package de.eppelt.roland.dominion.task;


import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Karten;
import de.eppelt.roland.dominion.Spieler.EmptyDeckException;
import de.eppelt.roland.dominion.Vorrat;
import de.eppelt.roland.dominion.ui.Handler;


/** Entsorge eine Karte aus deiner Hand. Nimm eine Karte, die bis zu 2 mehr kostet als die entsorgte Karte. 
 * 
 * Kann mit {@link #getAltKarten()}, {@link #sayAuswählen()}, {@link #calcGeld(Karte)}, {@link #getNeuKarten(Vorrat)}, 
 * {@link #neueKarteAblegen(Handler, Karte)} angepasst werden. */ 
public class Umbau extends AufgabeImpl {
	
	
	protected int geld = 0;
	@SuppressWarnings("null") protected Karte alteKarte = null;
	
	
	/** @return {@link #handkarten()} */
	public Karten getAltKarten() {
		return getSpieler().getHandkarten();		
	}
	
	
	/** sayln: Welche Handkarte willst du eintauschen? Du bekommst dafür eine Karte, die bis zu 2 Münzen mehr kostet. */
	public void sayAuswählen() {
		sayln("Welche Handkarte willst du eintauschen? Du bekommst dafür eine Karte, die bis zu 2 Münzen mehr kostet.");
	}
	
	
	public int calcGeld(Karte karte) {
		return kosten(karte)+2;
	}
	
	
	public Karten getNeuKarten(Vorrat vorrat) {
		return vorrat.getKarten(k -> kosten(k)<=geld);
	}
	
	
	/** Legt Karte zur Seite. */
	public void neueKarteAblegen(Handler handler, Karte karte) {
		handler.seite().legeAb(karte);
	}
	
	
	public void umbau(Handler handler, Vorrat vorrat, Karte neueKarte) {
		handler.spielerHat(alteKarte.getName()+" entsorgt und dafür "+neueKarte.getName()+" bekommen.");
		try {
			neueKarteAblegen(handler, vorrat.zieheKarte(neueKarte));
		} catch (EmptyDeckException e) {
			// Das ist ungünstig. 
		}
		done();
	}
	
	
		// ========== Aufgabe ==========


	@Override public boolean anzeigen() {
		headerHandkartenTitle();
		if (geld==0) {
			fine("Phase 1");
				// Phase 1: Geldkarte auswählen und entsorgen 
			Karten altKarten = getAltKarten();
			if (altKarten.size()>0) {
				sayAuswählen();
				oneKarte(altKarten, (handler, karte) -> {
					alteKarte  = karte;
					handler.handkarten().entferne(karte);
					handler.trash().legeAb(karte);
					handler.spielerHat("Karte "+karte.getName()+" entsorgt.");
					geld = calcGeld(karte);
					handler.getSpieler().updateOtherPlayers();
				});
				ln();
			} else {
				sayln("Du hast leider gar keine Karte, die du eintauschen könntest.");
				button("Oh. Schade.", 'o', true, handler -> done());
			}
		} else {
			fine("Phase 2");
				// Phase 2: Neue Geldkarte+3 auf die Hand kaufen
			Karten neuKarten = getNeuKarten(vorrat());
			if (neuKarten.size()==0) {
				say("Du hast nur ");
				say(geld);
				sayln(" Münzen. Das reicht aber nicht für eine neue Karte.");
				button("Oh. Schade.", 'o', true, handler -> done());
			} else if (neuKarten.size()==1) {
				if (alteKarte!=null ) {
					say("Wogegen möchtest du dein ");
					say(alteKarte.getName());
					sayln(" eintauschen?");
				}
				say("Du hast ");
				say(geld);
				sayln(" Münzen für eine neue Karte. Das reicht nur für diese eine:");
				oneKarte(neuKarten, (handler, neueKarte) -> umbau(handler, handler.vorrat(), neueKarte));
				ln();
				button("Ok, Danke.", 'o', true, handler -> umbau(handler, handler.vorrat(), neuKarten.get(0)));
			} else {
				say("Du hast ");
				say(geld);
				sayln(" Münzen für eine neue Karte. Welche darf es sein?");
				oneKarte(neuKarten, (handler, neueKarte) -> umbau(handler, handler.vorrat(), neueKarte));
				ln();
			}
		}
		return true;
	}
	

}
