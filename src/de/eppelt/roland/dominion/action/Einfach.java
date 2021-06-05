package de.eppelt.roland.dominion.action;


import de.eppelt.roland.dominion.Dominion;
import de.eppelt.roland.dominion.Dran;
import de.eppelt.roland.dominion.Karte;
import de.tesd.util.Strings;


/** Eine einfache {@link Aktion}. mit {@link #getKarten()}, {@link #getAktionen()}, {@link #getWert()} und
 * {@link #getKäufe()}. */
public class Einfach extends AktionImpl {


	/** Anzahl der {@link Karte}n, die nachgezogen werden dürfen */
	int karten;
	/** Anzahl der {@link Aktion}en, die zusätzlich ausgeführt werden dürfen */
	int aktionen;
	/** Anzahl zusätzlicher Käufe */
	int käufe;
	/** Anzahl der Geldmünzen */
	int geld;


	/** Erzeugt eine {@link Einfach}. */
	public Einfach(int karten, int aktionen, int käufe, int geld) {
		this.name = getEinfachName();
		this.karten = karten;
		this.aktionen = aktionen;
		this.käufe = käufe;
		this.geld = geld;
	}
	
	
	public String getEinfachName() {
		StringBuffer sb = new StringBuffer();
		boolean first = true;
		if (karten!=0) {
			first = false;
			sb.append("+");
			sb.append(karten);
			sb.append(" Karten");
		}
		if (aktionen!=0) {
			first = Strings.appendNotFirst(sb, ", ", first);
			sb.append("+");
			sb.append(aktionen);
			sb.append(" Aktionen");
		}
		if (käufe!=0) {
			first = Strings.appendNotFirst(sb, ", ", first);
			sb.append("+");
			sb.append(käufe);
			sb.append(" Käufe");
		}
		if (geld!=0) {
			first = Strings.appendNotFirst(sb, ", ", first);
			sb.append("+");
			sb.append(geld);
			sb.append(" Geldmünzen");
		}
		return sb.toString();
	}


	/** @return Anzahl der {@link Karte}n, die nachgezogen werden dürfen */
	public int getKarten() {
		return karten;
	}


	/** @return Anzahl der {@link Aktion}en, die zusätzlich ausgeführt werden dürfen */
	public int getAktionen() {
		return aktionen;
	}


	/** @return Anzahl zusätzlicher Käufe */
	public int getKäufe() {
		return käufe;
	}


	/** @return Anzahl der Geldmünzen */
	public int getGeld() {
		return geld;
	}


	@Override public void ausführen(Dominion dominion, Dran dran) {
		dran.getSpieler().zieheKarten(karten);
		dran.addAktionen(aktionen);
		dran.addGeld(geld);
		dran.addKäufe(käufe);
		if (karten>0) {
			dran.getSpieler().updateOtherPlayers();
		}
	}


	@Override public String toString() {
		return name;
	}

}
