package de.eppelt.roland.dominion.action;


import de.eppelt.roland.dominion.Dominion;
import de.eppelt.roland.dominion.Dran;


public abstract class AktionImpl implements Aktion {


	String name = getClass().getSimpleName();


	@Override public String getName() {
		return name;
	}
	
	
	@Override public void setName(String name) {
		this.name = name;
	}


	@Override public boolean möglich(Dominion dominion) {
		return true;
	}
	
	
	@Override public void ausführen(Dominion dominion) {
		Dran dran = dominion.getDran();
		if (dran!=null) {
			ausführen(dominion, dran);
		}
	}


	protected abstract void ausführen(Dominion dominion, Dran dran);


}

