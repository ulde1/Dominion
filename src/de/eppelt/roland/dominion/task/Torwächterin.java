package de.eppelt.roland.dominion.task;


import org.eclipse.jdt.annotation.Nullable;

import de.eppelt.roland.dominion.Karten;


/** Oberste 2 Nachziehkarten entsorgen oder ablegen oder in beliebiger Reihenfolge zurück */
public class Torwächterin extends AufgabeImpl {


	@Nullable Karten karten = null;
	
	
	@SuppressWarnings("null")
	@Override public boolean execute() {
		if (karten==null) {
			karten = getSpieler().zieheKartenKarten(2);
		}
		if (karten.size()==0) {
			done();
			return false;
		} else {
			headerHandkartenTitle();
			sayln("Du hast diese Karten gezogen. Was soll mit ihnen geschehen?");
			columnsStart();
			for (int i = 0; i<karten.size(); i++) {
				nextColumn();
				karte(karten.get(i));
				ln();
				int li = i;
				button("Zurück auf den Nachziehstapel", ' ', true, handler -> {
					handler.nachziehStapel().legeAb(karten.ziehe(li));
				});
				ln();
				button("Ablegen", ' ', true, handler -> {
					handler.seite().legeAb(karten.ziehe(li));
				});
				ln();
				button("Entsorgen", ' ', true, handler -> {
					handler.trash().legeAb(karten.ziehe(li));
				});
				ln();
			}
			columnsEnd();
		}
		return true;
	}
	
	
}
