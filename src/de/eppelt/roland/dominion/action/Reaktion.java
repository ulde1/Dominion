package de.eppelt.roland.dominion.action;

import de.eppelt.roland.dominion.task.OpferAufgabe;
import de.eppelt.roland.dominion.task.Schutz;
import de.eppelt.roland.dominion.ui.Handler;


/** Reaktion auf eine {@link Aktion} mittels {@link Schutz}. */ 
public interface Reaktion {

	
	boolean reaktionMöglich(OpferAufgabe aufgabe);

	
	/** Am Ende {@link Schutz#done()} aufrufen, sonst Endlosschleife. */
	void reagiere(Schutz schutz, Handler handler);


}
