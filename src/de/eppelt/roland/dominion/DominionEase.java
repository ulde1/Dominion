package de.eppelt.roland.dominion;

import java.util.function.Consumer;

import org.eclipse.jdt.annotation.Nullable;

public interface DominionEase {
	
	
	Dominion getInstance();
	
	
	default Vorrat vorrat() {
		return getInstance().getVorrat();
	}


	default int kosten(Karte karte) {
		return getInstance().getKosten(karte);
	}
	
	
	/** @return {@link Spieler}, der an der Reihe ist */
	default public @Nullable Dran getDran() {
		return getInstance().getDran();
	}


	default void dran(Consumer<Dran> consumer) {
		getInstance().dran(consumer);
	}
	
	
	default void addAktionen(int aktionen) {
		getInstance().addAktionen(aktionen);
	}
	
	
	default void addGeld(int geld) {
		getInstance().addGeld(geld);
	}
	
	
	default void addKäufe(int käufe) {
		getInstance().addKäufe(käufe);
	}
	
	
	default Karten trash() {
		return getInstance().getTrash();
	}
	
	
	default void log(String message) {
		getInstance().logEintrag(message);
	}


	/** @return Ist das {@link Dominion} zu Ende? */
	default boolean zuEnde() {
		return getInstance().zuEnde(); 
	}
	
	

}
