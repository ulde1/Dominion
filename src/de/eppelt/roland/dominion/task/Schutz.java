package de.eppelt.roland.dominion.task;

import de.eppelt.roland.dominion.Karten;
import de.eppelt.roland.dominion.Spieler;
import de.eppelt.roland.dominion.action.Aktion;
import de.eppelt.roland.dominion.action.Reaktion;
import de.eppelt.roland.dominion.ui.UI;


public class Schutz extends AufgabeImpl implements OpferAufgabe {
	
	
	OpferAufgabe aufgabe;
	private Karten karten;

	
	public Schutz(OpferAufgabe aufgabe) {
		this.aufgabe = aufgabe;
		karten  = aufgabe.getOpfer().getHandkarten().stream()
			.filter(k -> k.isMöglicheReaktion(aufgabe))
			.collect(Karten.COLLECT);
	}
	

	public OpferAufgabe getAufgabe() {
		return aufgabe;
	}

	
	@Override public boolean execute() {
		if (getOpferstatus()==Opferstatus.GESCHÜTZT) {
			done();
			return false;
		} else if (aufgabe.getTäter()==aufgabe.getOpfer() || karten.isEmpty()) {
			done();
			aufgabe.setOpferstatus(Opferstatus.UNGESCHÜTZT);
			aufgabe.getOpfer().nextAufgabe(aufgabe);
			return false;
		} else {
			play("Luck.mp3");
			headerHandkartenTitle();
			say("Sie sind ein ");
			say(aufgabe.getName());
			sayln(", können aber reagieren. Wählen Sie eine Reaktion");
			oneKarte(karten, (handler, karte) -> {
				Aktion aktion = karte.getAktion();
				if (aktion instanceof Reaktion) {
					((Reaktion) aktion).reagiere(this, handler);
				}
				done();
			});
			ln();
			say("oder drücken Sie ");
			button("Keine Reaktion", 'k', true, handler -> {
				handler.getSpieler().nextAufgabe(aufgabe);
				aufgabe.setOpferstatus(Opferstatus.UNGESCHÜTZT);
				done();
			});
			ln();
			return true;
		}
	}
	
	
		// ========== OpferAufgabe ==========


	@Override public Opferstatus getOpferstatus() {
		return aufgabe.getOpferstatus();
	}


	@Override public void setOpferstatus(Opferstatus opferstatus) {
		aufgabe.setOpferstatus(opferstatus);		
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
