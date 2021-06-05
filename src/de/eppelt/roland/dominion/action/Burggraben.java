package de.eppelt.roland.dominion.action;

import de.eppelt.roland.dominion.Dominion;
import de.eppelt.roland.dominion.Dran;
import de.eppelt.roland.dominion.task.OpferAufgabe;
import de.eppelt.roland.dominion.task.OpferAufgabe.Opferstatus;
import de.eppelt.roland.dominion.task.Schutz;
import de.eppelt.roland.dominion.ui.Handler;

public class Burggraben extends AktionImpl implements Reaktion {

	
	@Override protected void ausführen(Dominion dominion, Dran dran) {
		dran.zieheKarten(2);
	}


	@Override public boolean reaktionMöglich(OpferAufgabe aufgabe) {
		return true;
	}


	@Override public void reagiere(Schutz schutz, Handler handler) {
		schutz.setOpferstatus(Opferstatus.GESCHÜTZT);
	}


}
