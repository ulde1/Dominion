package de.eppelt.roland.dominion.action;


import de.eppelt.roland.dominion.Dominion;
import de.eppelt.roland.dominion.Dran;
import de.eppelt.roland.dominion.task.Herumtreiberin;


/** +1 Aktion; Wähle eins: Entsorge eine Aktionskarte vom Vorrat oder nimm eine Aktionskarte vom Müll.  */
public class HerumtreiberinAktion extends AktionImpl {


	@Override protected void ausführen(Dominion dominion, Dran dran) {
		dran.addAktionen(1);
		dran.getSpieler().sofortAufgabe(new Herumtreiberin());
	}

}
