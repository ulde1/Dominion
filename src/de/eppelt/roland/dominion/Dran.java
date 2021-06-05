package de.eppelt.roland.dominion;

import de.eppelt.roland.dominion.action.Aktion;

/** Zustand des {@link Spieler}s, der gerade dran ist. */
public class Dran implements SpielerEase {


	/** {@link Spieler}, der dran ist */
	Spieler spieler;
	/** Zusätzliche Geldmünzen */
	int geld = 0;
	/** Anzahl Käufe, die der {@link Dran} noch tätigen darf */
	int käufe = 1;
	/** Anzahl {@link Aktion}en, die der {@link Dran} noch ausführen darf */
	int aktionen = 1;
	private int ausgespielteAktionen = 0;


	public Dran(Spieler spieler) {
		this.spieler = spieler;
	}


	@Override public Spieler getSpieler() {
		return spieler;
	}


	public int getGeld() {
		return geld;
	}


	public void setGeld(int geld) {
		this.geld = geld;
	}


	@Override public void addGeld(int geld) {
		this.geld += geld;
	}


	public int getKäufe() {
		return käufe;
	}


	public void setKäufe(int käufe) {
		this.käufe = käufe;
	}


	@Override public void addKäufe(int käufe) {
		this.käufe += käufe;
	}


	public int getAktionen() {
		return aktionen;
	}


	public void setAktionen(int aktionen) {
		this.aktionen = aktionen;
	}


	@Override public void addAktionen(int aktionen) {
		this.aktionen += aktionen;
	}
	
	
	public void incAusgespielteAktionen() {
		ausgespielteAktionen++;
	}

	
	public int getAusgespielteAktionen() {
		return ausgespielteAktionen;
	}
	

	@Override public String toString() {
		return getSpieler().toString()+" +"+getGeld()+" Geld, +"+getKäufe()+" Käufe, "+getAktionen()+" Aktionen";
	}


}
