package de.eppelt.roland.dominion.task;

import de.eppelt.roland.dominion.Spieler;
import de.eppelt.roland.dominion.ui.UI;

public abstract class OpferAufgabeImpl extends AufgabeImpl implements OpferAufgabe {


	Opferstatus opferstatus = Opferstatus.WARTEN;
	Spieler täter, opfer;
	
	
	public OpferAufgabeImpl(Spieler täter, Spieler opfer) {
		this.täter = täter;
		this.opfer = opfer;
		String simpleName = getClass().getSimpleName();
		if (simpleName.endsWith("Opfer")) {
			int posOfO = simpleName.length()-5;
			setName(simpleName.substring(0, posOfO)+"-"+simpleName.substring(posOfO));
		}
	}


	@Override public Opferstatus getOpferstatus() {
		return opferstatus;
	}


	@Override public void setOpferstatus(Opferstatus opferstatus) {
		this.opferstatus = opferstatus;
		getTäter().updateMe();
	}
	

	@Override public Spieler getTäter() {
		return täter;
	}
	
	
	@Override public Spieler getOpfer() {
		return opfer;
	}
	
	
	@Override public boolean isGeschützt() {
		return opferstatus==Opferstatus.GESCHÜTZT;
	}
	
	
	@Override public boolean isUngeschütztElseSay(UI ui) {
		switch (getOpferstatus()) {
			case WARTEN:
				ui.sayln("Bitte warten…");
				return false;
			case GESCHÜTZT:
				ui.sayln("Leider geschützt.");
				return false;
			default:
				return true;
		}
	}


}
