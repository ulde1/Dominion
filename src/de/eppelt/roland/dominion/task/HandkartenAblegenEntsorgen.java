package de.eppelt.roland.dominion.task;

import java.util.function.BiConsumer;

import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Karten;
import de.eppelt.roland.dominion.ui.Handler;


public class HandkartenAblegenEntsorgen extends AufgabeImpl {
	
	
	public static enum Zähle {
		BISZU, GENAU;
	}
	
	
	public static enum Verwendung {

		ABLEGEN("Ablegen", "abgelegt", (handler, karte) -> handler.seite().legeAb(karte)),
		ENTSORGEN("Entsorgen", "entsorgt", (handler, karte) -> handler.trash().legeAb(karte));
		
		final String Ablegen, ablegen, abgelegt;
		final BiConsumer<Handler, Karte> verwende;
		
		
		private Verwendung(String Ablegen, String abgelegt, BiConsumer<Handler, Karte> verwende) {
			this.Ablegen = Ablegen;
			this.ablegen = Ablegen.toLowerCase();
			this.abgelegt = abgelegt;
			this.verwende = verwende;
		}
		
		
		public void verwende(Handler handler, Karte karte) {
			verwende.accept(handler, karte);
		}
		
	}
	
	
	protected int anzahl;
	final Verwendung verwendung;
	final Zähle zähle;
	
	
	public HandkartenAblegenEntsorgen(Verwendung verwendung, Zähle zähle, int anzahl) {
		this.anzahl = anzahl;
		this.zähle = zähle;
		this.verwendung = verwendung;
		if (getClass().equals(HandkartenAblegenEntsorgen.class)) {
			setName("Handkarten "+verwendung.ablegen);
		}
	}
	
	
	protected void vorherHinweis() {
		
	}
	
	
	protected void nachher(Handler handler) {
		
	}
	
	
	private void abbruch(HandkartenAblegenEntsorgen aufgabe) {
		
	}


	@Override public boolean anzeigen() {
		if (handkarten().isEmpty()) {
			abbruch(this);
			done();
			return false;
		} else {
			headerHandkartenTitle();
			if (anzahl==1) {
				vorherHinweis();
				say("Markiere die Handkarte, die du ");
				say(verwendung.ablegen);
				sayln(" möchtest:");
				oneKarte(handkarten(), (handler, karte) -> {
					handler.handkarten().entferne(karte);
					verwendung.verwende(handler, karte);
					anzahl--;
					if (anzahl<=0 || zähle==Zähle.BISZU) {
						nachher(handler);
						done();
					}
				});
				if (zähle==Zähle.BISZU) {
					ln();
					say("oder drücke ");
					button("Keine Karte "+verwendung.ablegen, 'k', true, handler -> done());
				}
				ln();
			} else { 
				vorherHinweis();
				say("Markiere ");
				if (zähle==Zähle.BISZU) {
					say("bis zu ");
				}
				say(anzahl);
				say(" Handkarten, die du ");
				say(verwendung.ablegen);
				sayln(" möchtest:");
				String indexKey = any(handkarten(), null);
				ln();
				say("Drücke anschließend auf ");
				button(verwendung.Ablegen, verwendung.ablegen.charAt(0), false, handler -> {
					int[] index = handler.getIndex(indexKey);
					Karten karten = new Karten();
					for (int i = 0; i<Math.min(anzahl, index.length); i++) {
						Karte karte = handler.handkarten().ziehe(index[i]);
						verwendung.verwende(handler, karte);
						karten.append(karte);
					}
					handler.spielerHat(karten.stream().collect(Karten.KURZ)+" "+verwendung.abgelegt+".");
					anzahl -= index.length;
					if (anzahl<=0 || zähle==Zähle.BISZU) {
						nachher(handler);
						done();
					}
				});
				sayln(".");
			}
			return true;
		}
	}


}
