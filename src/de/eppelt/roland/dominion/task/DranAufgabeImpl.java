package de.eppelt.roland.dominion.task;


import org.eclipse.jdt.annotation.NonNull;

import de.eppelt.roland.dominion.Dran;


public abstract class DranAufgabeImpl extends AufgabeImpl {


	protected Dran dran;


	public DranAufgabeImpl(Dran dran) {
		super();
		this.dran = dran;
		setSpieler(dran.getSpieler());
	}


	@Override public @NonNull Dran getDran() {
		return dran;
	}


}
