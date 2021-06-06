package de.eppelt.roland.dominion.task;


import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Spieler;
import de.eppelt.roland.dominion.Spieler.EmptyDeckException;
import de.eppelt.roland.dominion.ui.Handler;


/** Jeder Mitspieler muss entweder 2 Karten ablegen oder eine Fluchkarte vom Vorrat auf die Hand nehmen. */
public class KerkermeisterOpfer extends OpferAufgabeImpl {
	
	
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
	
	
	int abgelegt = -1; // d.h. entscheiden


	public KerkermeisterOpfer(Spieler täter, Spieler opfer) {
		super(täter, opfer);
	}
	
	
	@Override public boolean anzeigen() {
		if (abgelegt>=2) {
			done();
			return false;
		} else {
			headerHandkartenTitle();
			if (abgelegt<0) {
				sayln("Entscheide dich:");
				button("2 Karten ablegen", 'a', true, handler -> {
					abgelegt = 0;
				});
				ln();
				button("Einen Fluch auf die Hand nehmen", 'f', true, handler -> {
					handler.sofortAufgabe(new FluchNehmen(getTäter(), getOpfer()));
					done();
				});
			} else if (abgelegt==0) {
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
			} else if (abgelegt==1) {
				sayln("Du musst noch eine Handkarte ab legen. Wähle aus:");
				oneIndex(handkarten(), (handler, index) -> {
					handler.seite().legeAb(handkarten().ziehe(index));
					done();
				});
			}
			return true;
		}
	}

}
