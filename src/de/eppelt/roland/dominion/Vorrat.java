package de.eppelt.roland.dominion;


import static de.eppelt.roland.dominion.Karte.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import de.eppelt.roland.dominion.Spieler.EmptyDeckException;


/** Die Vorrats-{@link Karten} in der Tischmitte.
 * @author Roland M. Eppelt */
public class Vorrat implements DominionEase {
	
	
	Dominion dominion;
	HashMap<Karte, Integer> stapel = new HashMap<>();
	
	
	public Vorrat(Dominion dominion) {
		this.dominion = dominion;
		stapel.put(KUPFER, 60);
		stapel.put(SILBER, 40);
		stapel.put(GOLD, 30);
		stapel.put(ANWESEN, 24);
		stapel.put(HERZOGTUM, 12);
		stapel.put(PROVINZ, 12);
		stapel.put(FLUCH, 0);
	}


	public Vorrat(Dominion dominion, Karten karten) {
		this(dominion);
		add(karten, 10);
	}
	
	
	@Override public Dominion getInstance() {
		return dominion;
	}
	
	
	public void add(Karte karte, int dazu) {
		Integer anzahl = stapel.getOrDefault(karte, 0);
		stapel.put(karte, anzahl+dazu);
	}
	
	
	public void add(Karten karten, int dazu) {
		for (Karte karte : karten) {
			add(karte, dazu);
		}
	}


//	/** @return die {@link Karte}n im Spiel */
//	public Set<Karte> getKarten() {
//		return Collections.unmodifiableSet(stapel.keySet());
//	}


	/** @return Gibt es noch {@link Karte}(n)? */
	public boolean hat(Karte karte) {
		return stapel.get(karte)>0;
	}
	
	
	/** Verringert den Bestand an {@link Karte}n um Eins.
	 * @return die gezogene Karte */
	public Karte zieheKarte(Karte karte) throws EmptyDeckException {
		if (hat(karte)) {
			stapel.put(karte, stapel.get(karte)-1);
			return karte;
		} else {
			throw new EmptyDeckException("Kein "+karte.getName()+" mehr im Vorrat");
		}
	}


	public Karten getLeereStapel() {
		return stapel.entrySet().stream()
			.filter(e -> e.getValue()==0)
			.map(Map.Entry::getKey)
			.collect(Karten.COLLECT);
	}

	
	public Karten getVolleStapel() {
		return stapel.entrySet().stream()
			.filter(e -> e.getValue()>0)
			.map(Map.Entry::getKey)
			.collect(Karten.COLLECT);
	}

	
	@Override public String toString() {
		return stapel.toString();
	}


	public int getAnzahl(Karte karte) {
		return stapel.getOrDefault(karte, 0);
	}


	/** Die verfügbaren {@link Karte}n, die teuersten zuerst.
	 * @param condition notwendige Bedingung 
	 * @return {@link Karten}
	 */
	public Karten getKarten(Predicate<Karte> condition) {
		return stapel.entrySet().stream()
			.filter(e -> e.getValue()>0)
			.map(Map.Entry::getKey)
			.filter(condition::test)
			.sorted((a, b) -> dominion.getKosten(a)-dominion.getKosten(b))
			.collect(Karten.COLLECT);
	}
	

}
