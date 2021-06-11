package de.eppelt.roland.dominion.task;

import org.eclipse.jdt.annotation.Nullable;

import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Karten;
import de.eppelt.roland.dominion.Spieler;


/** Entsorge eine der beiden obersten Geldkarten und legt den Rest ab */
public class BanditinOpfer extends OpferAufgabeImpl {
	
	
	protected Karten karten;
	protected long count = -1L;
	protected @Nullable Karte entsorgt = null;
	
	
	@SuppressWarnings("null")
	public BanditinOpfer(Spieler täter, Spieler opfer) {
		super(täter, opfer);
	}
	

	public long getCount() {
		return count;
	}
	

	public @Nullable Karte getEntsorgt() {
		return entsorgt;
	}
	
	
	@Override public void vorbereiten() {
		karten = getSpieler().zieheKartenKarten(2);
		count = karten.stream()
			.filter(Karte::isGeld)
			.filter(k -> k!=Karte.KUPFER)
			.count();
		super.vorbereiten();
	}


	@Override public boolean anzeigen() {
		headerHandkartenTitle();
		if (count==0L) {
			play("Luck.mp3");
			sayln("Glück gehabt! Das sind deine beiden obersten Karten vom Nachziehstapel.");
			karten(karten, false);
			button("Ablegen", 'a', true, handler -> {
				handler.seite().legeAlleAbVon(karten);
				done();
			});
			ln();
		} else if (count==1L) {
			play("Bandit.mp3");
			sayln("Das sind deine beiden obersten Karten vom Nachziehstapel.");
			karten(karten, false);
			if (karten.get(0).isGeld() && karten.get(0)!=Karte.KUPFER) {
				button(karten.get(1).getName()+" ablegen, "+karten.get(0).getName()+" entsorgen", 'a', true, handler -> {
					Karte karte = karten.get(0);
					entsorgt = karte;
					handler.trash().legeAb(karte);
					handler.seite().legeAb(karten.get(1));
					getTäter().updateMe();
					done();
				});
			} else {
				button(karten.get(1).getName()+" entsorgen, "+karten.get(0).getName()+" ablegen", 'a', true, handler -> {
					handler.seite().legeAb(karten.get(0));
					Karte karte = karten.get(1);
					entsorgt = karte;
					handler.trash().legeAb(karte);
					getTäter().updateMe();
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
						entsorgt = k;
						handler.trash().legeAb(k);
					} else {
						handler.seite().legeAb(k);						
					}
				}
				getTäter().updateMe();
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