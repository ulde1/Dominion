package de.eppelt.roland.dominion.task;

import org.eclipse.jdt.annotation.Nullable;

import de.eppelt.roland.dominion.Dran;
import de.eppelt.roland.dominion.Karte;

/** +2 Karten, +1 Aktion; Lege eine Handkarte an eine beliebige Stelle in deinen Nachziehstapel. */
public class Geheimgang extends AufgabeImpl {

	
	@Nullable Karte zurück;
	

	public Geheimgang(Dran dran) {
		dran.zieheKarten(2);
		dran.addAktionen(1);
	}
	
	
	@SuppressWarnings("null")
	@Override public boolean execute() {
		headerHandkartenTitle();
		if (zurück==null) {
			sayln("Welche Handkarte möchtest du an eine beliebige Stelle in deinen Nachziehstapel zurücklegen?");
			oneKarte(handkarten(), (handler, karte) -> {
				handler.handkarten().entferne(karte);
				zurück = karte;
				handler.spielerHat(karte.getName()+" zum Zurücklegen ausgewählt.");
			});
			ln();
			say("Oder lieber doch ");
			button("Keine Karte zurücklegen", 'k', true, handler -> done());
			sayln("?");
		} else {
			sayln("Wo soll deine Karte einsortiert werden? Klicke die Karte an, über die du deine Karte legen willst (die erste Karte ist oben):");
			oneIndex(nachziehStapel(), (handler, index) -> {
				handler.nachziehStapel().insert(index, zurück);
				done();
			});
			ln();
			say("Oder ");
			button("Ganz unten", 'g', true, handler -> {
				handler.nachziehStapel().append(zurück);
				done();
			});
		}
		return true;
	}

}
