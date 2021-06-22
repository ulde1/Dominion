package de.eppelt.roland.dominion.task;


import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Spieler;
import de.eppelt.roland.dominion.Spieler.EmptyDeckException;
import de.eppelt.roland.dominion.ui.Handler;


/** Jeder Mitspieler muss entweder 2 Karten ablegen oder eine Fluchkarte vom Vorrat auf die Hand nehmen. */
public class KerkermeisterOpfer extends OpferAufgabeImpl {
	
	
	public static final String EINEN_FLUCH_AUF_DIE_HAND_NEHMEN = "Einen Fluch auf die Hand nehmen";


	public static class FluchNehmen extends HexenOpfer {

		public FluchNehmen(Spieler täter, Spieler opfer) {
			super(täter, opfer);
			setName("Fluch nehmen");
		}
		
		
		@Override public void gibFluch(Handler handler) {
				try {
					handler.handkarten().legeAb(handler.vorrat().zieheKarte(Karte.FLUCH));
				} catch (EmptyDeckException e) {
					// Kaum möglich.
				}
				done();
			}
		
	}
	
	
	public static enum Wahl { KEINE, FLUCH, ABLEGEN };
	
	
	Wahl wahl = Wahl.KEINE;
	int ablegen = 2;
	int abgelegt = -1; // d.h. entscheiden


	public KerkermeisterOpfer(Spieler täter, Spieler opfer) {
		super(täter, opfer);
	}
	
	
	public String getMessage() {
		switch (wahl) {
			case KEINE:
				return getOpfer().getName()+" prüft noch seine Optionen.";
			case FLUCH:
				return getOpfer().getName()+" hat einen Fluch genommen.";
			case ABLEGEN:
				return getOpfer().getName()+" hat "+karteN(ablegen)+" abgelegt.";
			default:
				return "Wahl "+wahl.name()+" unbekannt. Bitte den Entwickler infomieren.";
		}
	}

	
	@Override public void vorbereiten() {
		super.vorbereiten();
		ablegen = Math.min(2, getOpfer().getHandkarten().size());
	}
	
	
	@Override public boolean anzeigen() {
		if (abgelegt>=ablegen) {
			done();
			return false;
		} else {
			headerHandkartenTitle();
			if (abgelegt<0) {
				sayln("Entscheide dich:");
				button(karteN(ablegen)+" ablegen", 'a', true, handler -> {
					wahl = Wahl.ABLEGEN;
					abgelegt = 0;
					getTäter().updateMe();
				});
				ln();
				button(EINEN_FLUCH_AUF_DIE_HAND_NEHMEN, 'f', true, handler -> {
					handler.sofortAufgabe(new FluchNehmen(getTäter(), getOpfer()));
					wahl = Wahl.FLUCH;
					getTäter().updateMe();
					done();
				});
			} else if (abgelegt==ablegen-2) {
				sayln("Lege zwei Handkarten ab. Wähle aus:");
				String anyKey = any(handkarten(), null);
				ln();
				say("Drücke dann ");
				button("Ablegen", 'a', false, handler -> {
					int[] index = handler.getIndex(anyKey);
					for (int i : index) {
						handler.seite().legeAb(handkarten().ziehe(i));
					}
					abgelegt += index.length;
				});
			} else if (abgelegt==ablegen-1) {
				sayln("Du musst noch eine Handkarte ablegen. Wähle aus:");
				oneIndex(handkarten(), (handler, index) -> {
					handler.seite().legeAb(handkarten().ziehe(index));
					abgelegt++;
					done();
				});
			}
			return true;
		}
	}


	private String karteN(int anzahl) {
		return Integer.toString(anzahl)+ (anzahl==1 ? " Karte" : " Karten");
	}


}
