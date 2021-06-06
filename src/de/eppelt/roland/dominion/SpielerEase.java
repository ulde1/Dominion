package de.eppelt.roland.dominion;

import java.util.function.BiFunction;

import de.eppelt.roland.dominion.Spieler.EmptyDeckException;
import de.eppelt.roland.dominion.task.Aufgabe;
import de.eppelt.roland.dominion.task.OpferAufgabe;

public interface SpielerEase extends DominionEase {


	Spieler getSpieler();
	
	
	@Override default Dominion getInstance() {
		return getSpieler().getInstance();
	}
	
	
	default Karten nachziehStapel() {
		return getSpieler().getNachziehStapel();
	}


	default Karten handkarten() {
		return getSpieler().getHandkarten();
	}


	default Karten seite() {
		return getSpieler().getSeite();
	}


	default Karten ablage() {
		return getSpieler().getAblageStapel();
	}
	
	
	default void spielerHat(String message) {
		getSpieler().spielerHat(message);
	}


	/** Zieht eine Karte vom {@link #nachziehStapel} nach 
	 * @param aufDieHand zieht die Karte auf die Hand. Sonst ist der Aufrufer für die Karte zuständig. */
	default Karte zieheKarte(boolean aufDieHand) throws EmptyDeckException  {
		return getSpieler().zieheKarte(aufDieHand);
	}


	/** Ziehl {@link Karte}n nach.
	 * @param anzahl Anzahl der {@link Karte}n, die vom {@link #nachziehStapel} auf die Hand nachgezogen werden.
	 * @see #zieheKarte(boolean)
	 */
	default void zieheKarten(int anzahl) {
		getSpieler().zieheKarten(anzahl);
	}
	
	
	default Karten zieheKartenKarten(int anzahl) {
		return getSpieler().zieheKartenKarten(anzahl);
	}


	default void sofortAufgabe(Aufgabe aufgabe) {
		getSpieler().sofortAufgabe(aufgabe);
	}
	
	
	default void angriff(BiFunction<Spieler, Spieler, OpferAufgabe> supplier) {
		getSpieler().angriff(supplier);
	}

}

