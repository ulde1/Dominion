package de.eppelt.roland.dominion.action;

import de.eppelt.roland.dominion.Dominion;
import de.eppelt.roland.dominion.Dran;
import de.eppelt.roland.dominion.Karte;

/** Aktion, die mit einer {@link Karte} ausgeführt werden kann.
 * @author Roland M. Eppelt */
public interface Aktion {
	

	String getName();
	
	
	void setName(String name);
	
	
	/** @return Ist diese Aktion möglich/erlaubt? */
	boolean möglich(Dominion dominion);
	

	/** Die {@link Aktion} ausführen. Vor dem Aufruf {@link Dran#incAusgespielteAktionen()}!
	 * @param dominion {@link Dominion} */
	void ausführen(Dominion dominion);
	
}
