package de.eppelt.roland.dominion.task;

import de.eppelt.roland.dominion.Karten;
import de.eppelt.roland.dominion.Spieler;
import de.eppelt.roland.dominion.action.Aktion;
import de.eppelt.roland.dominion.action.Reaktion;
import de.eppelt.roland.dominion.ui.UI;


public class Schutz extends AufgabeImpl implements OpferAufgabe {
	
	
	OpferAufgabe aufgabe;

	
	public Schutz(OpferAufgabe aufgabe) {
		this.aufgabe = aufgabe;
	}
	
	
	public OpferAufgabe getAufgabe() {
		return aufgabe;
	}
	
	
	public Karten möglicheReaktionen() {
		return getAufgabe().getOpfer().getHandkarten().stream()
			.filter(k -> k.isMöglicheReaktion(aufgabe))
			.collect(Karten.COLLECT);
	}
	

	@Override public void vorbereiten() {
		if (aufgabe.getTäter()==aufgabe.getOpfer() || möglicheReaktionen().isEmpty()) {
			aufgabe.setOpferstatus(Opferstatus.UNGESCHÜTZT);
		}
		super.vorbereiten();
	}


	@Override public boolean anzeigen() {
		if (getOpferstatus()==Opferstatus.GESCHÜTZT) {
			done();
			return false;
		} else if (getOpferstatus()==Opferstatus.UNGESCHÜTZT) {
			getOpfer().nextAufgabe(aufgabe);
			done();
			return false;
		} else {
			Karten möglicheReaktionen = möglicheReaktionen();
			if (möglicheReaktionen.isEmpty()) {
				headerHandkartenTitle();
				say("Du bist ein ");
				say(aufgabe.getName());
				sayln(", hast aber keine mögliche Reaktion.");
				ln();
				button("Ich, Opfer.", 'i', true, handler -> {
					getOpfer().nextAufgabe(aufgabe);
					aufgabe.setOpferstatus(Opferstatus.UNGESCHÜTZT);
					done();
				});
			} else {
				play("Luck.mp3");
				headerHandkartenTitle();
				say("Du bist ein ");
				say(aufgabe.getName());
				sayln(", kannst aber reagieren. Wähle eine Reaktion:");
				oneKarte(möglicheReaktionen, (handler, karte) -> {
					Aktion aktion = karte.getAktion();
					if (aktion instanceof Reaktion) {
						((Reaktion) aktion).reagiere(this, handler);
					}
				});
				ln();
				say("oder drücke ");
				button("Keine Reaktion", 'k', true, handler -> {
					getOpfer().nextAufgabe(aufgabe);
					aufgabe.setOpferstatus(Opferstatus.UNGESCHÜTZT);
					done();
				});
				ln();
			}
			return true;
		}
	}
	
	
		// ========== OpferAufgabe ==========


	@Override public Opferstatus getOpferstatus() {
		return aufgabe.getOpferstatus();
	}


	@Override public void setOpferstatus(Opferstatus opferstatus) {
		aufgabe.setOpferstatus(opferstatus);
		getTäter().updateMe();
		getSpieler().updateMe();
	}


	@Override public Spieler getTäter() {
		return aufgabe.getTäter();
	}


	@Override public Spieler getOpfer() {
		return aufgabe.getOpfer();
	}


	@Override public boolean isUngeschütztElseSay(UI ui) {
		return aufgabe.isUngeschütztElseSay(ui);
	}


	@Override public boolean isGeschützt() {
		return aufgabe.isGeschützt();
	}
	
	
}
