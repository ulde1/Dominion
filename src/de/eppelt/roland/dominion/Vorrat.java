package de.eppelt.roland.dominion;


import static de.eppelt.roland.dominion.Karte.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import de.eppelt.roland.dominion.Spieler.EmptyDeckException;


/** Die Vorrats-{@link Karten} in der Tischmitte.
 * @author Roland M. Eppelt */
public class Vorrat implements DominionEase {
	
	
	public static final int[] ZEHN = new int[] { 10, 10, 10, 10, 10 };
	public static final int[] ACHTZWÖLF = new int[] { 8, 12, 12, 12, 12 };


	Dominion dominion;
	HashMap<Karte, Integer> stapel = new HashMap<>();
	int spieler = 0;
	
	
	public Vorrat(Dominion dominion) {
		this.dominion = dominion;
	}


	public Vorrat(Dominion dominion, Karten karten) {
		this(dominion);
	}
	
	
	@Override public Dominion getInstance() {
		return dominion;
	}
	
	
	public boolean isEmpty() {
		return stapel.isEmpty();
	}
	

	public synchronized void setKarten(Karten karten) {
		stapel.clear();
		putKarte(KUPFER);
		putKarte(SILBER);
		putKarte(GOLD);
		putKarte(ANWESEN);
		putKarte(HERZOGTUM);
		putKarte(PROVINZ);
		putKarte(FLUCH);
		for (Karte karte : karten) {
			putKarte(karte);
		}
	}
	
	
	private void putKarte(Karte karte) {
		stapel.put(karte, karte.getAnzahl(spieler));
	}
	

	public int getSpieler() {
		return spieler;
	}


	public synchronized void setSpieler(int spieler) {
		stapel.keySet().forEach(karte -> setSpieler(karte, this.spieler, spieler));
		this.spieler = spieler;
	}
	
	
	private void setSpieler(Karte karte, int alt, int neu) {
		stapel.put(karte, stapel.get(karte)+karte.getAnzahl(neu)-karte.getAnzahl(alt));
	}


	/** @return Gibt es noch {@link Karte}(n)? */
	public boolean hat(Karte karte) {
		return stapel.getOrDefault(karte, 0)>0;
	}
	
	
	/** Verringert den Bestand an {@link Karte}n um Eins.
	 * @return die gezogene Karte */
	public synchronized Karte zieheKarte(Karte karte) throws EmptyDeckException {
		if (hat(karte)) {
			stapel.put(karte, stapel.get(karte)-1);
			return karte;
		} else {
			throw new EmptyDeckException("Kein "+karte.getName()+" mehr im Vorrat");
		}
	}


	public Set<Karte> getAlleKarten() {
		return stapel.keySet();
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
			.sorted((a, b) -> dominion.getKosten(b)-dominion.getKosten(a))
			.collect(Karten.COLLECT);
	}


	@Override public String toString() {
		return stapel.toString();
	}


}
