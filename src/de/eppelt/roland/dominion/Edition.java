package de.eppelt.roland.dominion;


import static de.eppelt.roland.dominion.Karte.*;

import java.util.Arrays;

import org.eclipse.jdt.annotation.Nullable;

import de.tesd.util.KeyNotFoundException;


public enum Edition {


	DOMINION("Dominion", BURGGRABEN, TÖPFEREI), 
	DOMINION_V1("Dominion (Edition 1)", KANZLER, ABENTEURER), 
	INTRIGE("Intrige", BURGHOF, ADELIGE);


	private static Kartenset @Nullable [] kartensets = null;
	
	
	public static Kartenset[] getKartensets() {
		if (kartensets==null) {
			kartensets = Arrays.stream(values())
				.map(Edition::getKartenset)
				.toArray(Kartenset[]::new);
		}
		assert kartensets!=null;
		return kartensets;
	}
	
	
	public static Edition of(Karte karte) throws KeyNotFoundException {
		for (Edition edition : values()) {
			if (edition.getKartenset().getKarten().enthält(karte)) {
				return edition;
			}
		}
		throw new KeyNotFoundException("Keine Edition gefunden für "+karte.getName());
	}
	
	
		// ========== Edition ==========
	

	private Kartenset kartenset;


	Edition(String name, Karte von, Karte bis) {
		this.kartenset = new Kartenset(name, Karten.vonBis(von, bis));
	}


	public Kartenset getKartenset() {
		return kartenset;
	}


}