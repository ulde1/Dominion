package de.eppelt.roland.dominion.task;


import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Karten;
import de.eppelt.roland.dominion.Spieler.EmptyDeckException;
import de.eppelt.roland.dominion.Vorrat;
import de.eppelt.roland.dominion.ui.Handler;


/** {@link AufgabeImpl} mit Kauf-Routine {@link #kauf()} und Hilfsroutinen {@link #getKaufbareKarten(Vorrat, int)}, 
 * {@link #kaufLog(Karte)}, {@link #kaufAbschluss(Handler, Karte)}, {@link #keinKauf(Handler)}.*/
public abstract class KaufenAufgabe extends AufgabeImpl {


	int geld;
	int käufe;
	int kaufNr = 1;


	public KaufenAufgabe(int geld, int käufe) {
		this.geld = geld;
		this.käufe = käufe;
	}


	protected Karten getKaufbareKarten(Vorrat vorrat, int geld) {
		return vorrat.getKarten(k -> vorrat.kosten(k)<=geld);
	}


	protected void kauf() {
		sayln("Welche Karte willst du kaufen?");
		Karten kaufbareKarten = getKaufbareKarten(vorrat(), geld);
		oneKarte(kaufbareKarten, (handler, karte) -> {
			fine(() -> "gekauft: "+karte);
			try {
				vorrat().zieheKarte(karte);
				handler.spielerHat(kaufLog(karte));
				geld -= handler.kosten(karte);
				käufe--;
				kaufNr++;
				kaufAbschluss(handler, karte);
			} catch (EmptyDeckException e) {
				fine("handle Vorrat leer");
				keinKauf(handler);
			}
		});
		ln();
		say("Klicke die Karte an, die du kaufen willst oder drücke ");
		button("Keine Karte kaufen", 'k', true, handler -> {
			fine("handle Kein Kauf");
			keinKauf(handler);
		});
		ln();
	}
		


	protected String kaufLog(Karte kaufKarte) {
		return kaufKarte.getName()+" gekauft.";
	}


	protected void kaufAbschluss(Handler handler, Karte kaufKarte) {
		handler.ablage().legeAb(kaufKarte);
		if (käufe<=0) {
			done();
			if (handler.zuEnde()) {
				handler.updateOtherPlayers();
			}
		} else {
			handler.getInstance().updatePlayersExcept(handler.getSpieler());
		}
	}


	protected void keinKauf(Handler handler) {
		done();
	}

}
