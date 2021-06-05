package de.eppelt.roland.dominion.task;

import org.eclipse.jdt.annotation.Nullable;

import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Karten;
import de.eppelt.roland.dominion.Spieler;


/** Entsorge eine der beiden obersten Geldkarten und legt den Rest ab */
public class BanditinOpfer extends OpferAufgabeImpl {
	
	
	protected @Nullable Karten karten = null;
	protected long count = -1L;
	
	
	public BanditinOpfer(Spieler täter, Spieler opfer) {
		super(täter, opfer);
		setName("Banditin-Opfer");
	}


	@SuppressWarnings("null")
	@Override public boolean execute() {
		headerHandkartenTitle();
		if (karten==null) {
			karten = getSpieler().zieheKartenKarten(2);
			count = karten.stream()
				.filter(Karte::isGeld)
				.filter(k -> k!=Karte.KUPFER)
				.count();
		}
		if (count==0L) {
			play("Luck.mp3");
			sayln("Glück gehabt! Das sind deine beiden obersten Karten vom Nachziehstapel.");
			karten(karten);
			button("Ablegen", 'a', true, handler -> {
				handler.seite().legeAlleAbVon(karten);
				done();
			});
			ln();
		} else if (count==1L) {
			play("Bandit.mp3");
			sayln("Das sind deine beiden obersten Karten vom Nachziehstapel.");
			karten(karten);
			if (karten.get(0).isGeld() && karten.get(0)!=Karte.KUPFER) {
				button(karten.get(0).getName()+" entsorgen, "+karten.get(1).getName()+" ablegen", 'a', true, handler -> {
					handler.trash().legeAb(karten.get(0));
					handler.seite().legeAb(karten.get(1));
					done();
				});
			} else {
				button(karten.get(0).getName()+" ablegen, "+karten.get(1).getName()+" entsorgen", 'a', true, handler -> {
					handler.seite().legeAb(karten.get(0));
					handler.trash().legeAb(karten.get(1));
					done();
				});
			}
			ln();
		} else if (count==2L) {
			play("Bandit.mp3");
			sayln("Das sind deine beiden obersten Karten vom Nachziehstapel. Welche davon willst du entsorgen?");
			oneKarte(karten, (handler, karte) -> {
				for (Karte k : karten) {
					if (k==karte) {
						handler.trash().legeAb(k);
					} else {
						handler.seite().legeAb(k);						
					}
				}
				done();
			});
			ln();
		} else {
			sayln("Interner Fehler #1 in Banditin-Opfer.");
			sayln("Bitte beschreiben Sie dem Programmierer, wie Sie das geschafft haben.");
			button("OK", 'o', true, handler -> done());
			ln();
		}
		return true;
	}


}