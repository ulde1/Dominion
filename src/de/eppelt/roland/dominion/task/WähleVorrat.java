package de.eppelt.roland.dominion.task;


import java.util.HashMap;

import org.eclipse.jdt.annotation.Nullable;

import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Karten;
import de.eppelt.roland.dominion.Kartenset;
import de.eppelt.roland.dominion.Vorrat;


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
	@Override public boolean execute() {
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
			sayln("Wähle Sie die Karten für Ihr Kartenset aus:");
			HashMap<Kartenset, String> indexKeys = new HashMap<>();
			for (Kartenset ks : Kartenset.EDITIONEN) {
				title(ks.getName());
				indexKeys.put(ks, any(ks.getKarten(), kartenset.getKarten()));
				ln();
			}			
			say("und drücken Sie anschließend ");
			button("Fertig", 'f', false, handler -> {
				Karten karten = new Karten();
				for (Kartenset ks : Kartenset.EDITIONEN) {
					int[] index = handler.getIndex(indexKeys.get(ks));
					for (int i : index) {
						karten.legeAb(ks.getKarten().get(i));
					}
				}
				kartenset.setKarten(karten);
				edit = false;
			});
			ln();
			
//			String indexKey = any(ALLE_KARTEN, kartenset.getKarten());
//			ln();
//			say("und drücken Sie anschließend ");
//			button("Fertig", 'f', false, handler -> {
//				int[] index = handler.getIndex(indexKey);
//				Karten karten = new Karten();
//				for (int i : index) {
//					karten.legeAb(ALLE_KARTEN.get(index[i]));
//				}
//				kartenset.setKarten(karten);
//				edit = false;
//			});
//			ln();
		} else {
			title("Kartenset "+kartenset.getName());
			for (Karte karte : kartenset.getKarten()) {
				karte(karte);
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
				handler.getInstance().setVorrat(new Vorrat(handler.getInstance(), kartenset.getKarten()));
				done();
			});
			ln();
		}
		return true;
	}


}
