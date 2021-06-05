package de.eppelt.roland.dominion.task;


import de.eppelt.roland.dominion.Dominion;
import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.ui.Handler;


/** Lege pro leerem Vorratsstapel eine Handkarte ab */
public class Wilddiebin extends AufgabeImpl {
	
	
	int insgesamtAblegen, abgelegt, nochAblegen;
	

	public Wilddiebin(Dominion dominion) {
		insgesamtAblegen = dominion.getVorrat().getLeereStapel().size();
		nochAblegen = insgesamtAblegen;
		abgelegt = 0;
	}


	private void eineKarteAblegen(Handler handler, Karte karte) {
		handler.handkarten().entferne(karte);
		handler.seite().legeAb(karte);
		abgelegt++;
		nochAblegen--;
		done();
	}


	private void vieleKartenAblegen(Handler handler, String indexKey) {
		int[] index = handler.getIndex(indexKey);
		for (int i : index) {
			Karte karte = handler.handkarten().ziehe(i);
			handler.seite().legeAb(karte);
		}
		abgelegt += index.length;
		nochAblegen -=  index.length;
	}


	@Override public boolean execute() {
		if (nochAblegen==0) {
			done();
			return false;
//			sayln("Zum Glück sind noch alle Vorratsstapel gefüllt.");
//			button("Gut so.", 'g', true, handler -> done());
//			ln();
		} else {
			headerHandkartenTitle();
			if (insgesamtAblegen==1 && abgelegt==0) { // Nur eine Karte ablegen
				sayln("Da ein Vorratsstapel bereits leer ist, müssen Sie eine Handkarte ablegen. Welche Karte wollen Sie ablegen?");
				oneKarte(handkarten(), this::eineKarteAblegen);
				ln();
			} else if (abgelegt==0) { // Noch keine Karten abgelegt
				say("Es sind bereits ");
				say(insgesamtAblegen);
				say(" Vorratsstapel leer sind. Sie müssen ");
				say(insgesamtAblegen);
				sayln(" Handkarten ablegen. Welche Karten wollen Sie ablegen?");
				String indexKey = any(handkarten(), null);
				ln();
				say("Drücken Sie anschließend auf ");
				button("Ablegen", 'a', false, handler -> vieleKartenAblegen(handler, indexKey));
				ln();
			} else if (nochAblegen==1) { // Nur noch eine Karte ablegen
				say("Es sind bereits ");
				say(insgesamtAblegen);
				say(" Vorratsstapel leer sind. Sie haben aber erst ");
				say(abgelegt);
				say(abgelegt==1 ? " Karte" :  " Karten");
				sayln(" abgelegt. Sie müssen also noch eine Handkarte ablegen. Welche Karte wollen Sie ablegen?");
				oneKarte(handkarten(), this::eineKarteAblegen);
				ln();
			} else { // Noch mehrere Karten ablegen
				say("Es sind bereits ");
				say(insgesamtAblegen);
				say(" Vorratsstapel leer sind. Sie haben aber erst ");
				say(abgelegt);
				say(abgelegt==1 ? " Karte" :  " Karten");
				say(" abgelegt. Sie müssen also noch ");
				say(nochAblegen);
				sayln(" Handkarten ablegen. Welche Karten wollen Sie ablegen?");
				String indexKey = any(handkarten(), null);
				ln();
				say("Drücken Sie anschließend auf ");
				button("Ablegen", 'a', false, handler -> vieleKartenAblegen(handler, indexKey));
				ln();
			}
		}
		return true;
	}
	
	
}