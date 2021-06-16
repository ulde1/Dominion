package de.eppelt.roland.dominion.task;


import java.util.HashMap;

import org.eclipse.jdt.annotation.Nullable;

import de.eppelt.roland.dominion.Edition;
import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Karten;
import de.eppelt.roland.dominion.Kartenset;


public class WähleVorrat extends AufgabeImpl {
	
	
	public static final Karten ALLE_KARTEN = new Karten(Karte.values());
	
	
	@Nullable Kartenset kartenset = null;
	boolean edit = false;
	

	public WähleVorrat() {
		setName("Vorrat auswählen");
		ALLE_KARTEN.entferne(Karte.KUPFER);
		ALLE_KARTEN.entferne(Karte.SILBER);
		ALLE_KARTEN.entferne(Karte.GOLD);
		ALLE_KARTEN.entferne(Karte.ANWESEN);
		ALLE_KARTEN.entferne(Karte.HERZOGTUM);
		ALLE_KARTEN.entferne(Karte.PROVINZ);
		ALLE_KARTEN.entferne(Karte.FLUCH);
	}


	@SuppressWarnings("null")
	@Override public boolean anzeigen() {
		headerHandkartenTitle();
		if (kartenset==null) {
			sayln("Wähle aus:");
			for (Kartenset ks : Kartenset.RECOMMENDED) {
				if (ks.getTitel()!=null) {
					title(ks.getTitel());
				}
				button(ks.getName(), ' ', true, client -> {
					kartenset = ks;
				});
				ln();
			}
			title("Eigenes Kartenset");
			button("selber zusammenstellen", 's', true, client -> {
				kartenset = new Kartenset("Eigenes Kartenset", new Karte[] {});
			});
			ln();
		} else if (edit && "Eigenes Kartenset".equals(kartenset.getName())) {
			sayln("Wähle die Karten für dein Kartenset aus:");
			HashMap<Kartenset, String> indexKeys = new HashMap<>();
			for (Kartenset ks : Edition.getKartensets()) {
				title(ks.getName());
				indexKeys.put(ks, any(ks.getKarten(), kartenset.getKarten()));
				ln();
			}			
			say("und drücke anschließend ");
			button("Fertig", 'f', false, handler -> {
				Karten karten = new Karten();
				for (Kartenset ks : Edition.getKartensets()) {
					int[] index = handler.getIndex(indexKeys.get(ks));
					for (int i : index) {
						karten.legeAb(ks.getKarten().get(i));
					}
				}
				kartenset.setKarten(karten);
				edit = false;
			});
			ln();
		} else {
			title("Kartenset "+kartenset.getName());
			karten(kartenset.getKarten(), false);
			if (kartenset.getKarten().isEmpty()) {
				say("Dein Kartenset ist noch leer. Drücke ");
				button("Bearbeiten", 'b', true, handler -> {
					edit = true;
				});
				say(", um Karten auszuwählen.");
			}
			ln();
			button("Zurück", 'z', true, handler -> {
				kartenset = null;
			});
			if ("Eigenes Kartenset".equals(kartenset.getName())) {
				button("Bearbeiten", 'b', true, handler -> {
					edit = true;
				});
			}
			button("Dieses Kartenset auswählen", 'k', true, handler -> {
				fine(() -> "Wähle Kartenset "+kartenset.getName());
				handler.spielerHat("das Kartenset "+kartenset.getName()+" gewählt.");
				handler.getInstance().start(kartenset.getKarten());
				done();
			});
			ln();
		}
		return true;
	}


}
