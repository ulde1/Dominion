package de.eppelt.roland.dominion;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.tesd.collection.HashMap;


/** Ein Stapel von {@link Karte}n. */
public class Karten implements Iterable<Karte>{
	
	
	public static final Collector<Karte, Karten, Karten> COLLECT = new Collector<Karte, Karten, Karten>() {

		@Override public Supplier<Karten> supplier() {
			return Karten::new;
		}

		@Override public BiConsumer<Karten, Karte> accumulator() {
			return Karten::legeAb;
		}

		@Override public BinaryOperator<Karten> combiner() {
			return (a, b) -> { a.legeAlleAbVon(b); return a; };			
		}

		@Override public Function<Karten, Karten> finisher() {
			return i -> i;
		}

		@SuppressWarnings("null")
		@Override public Set<Characteristics> characteristics() {
			return Collections.emptySet();
		}
	
	};


	public static final Collector<Karte, HashMap<Karte, Integer>, String> KURZ = new Collector<Karte, HashMap<Karte, Integer>, String>() {

		@Override public Supplier<HashMap<Karte, Integer>> supplier() {
			return HashMap::new;
		}

		@Override public BiConsumer<HashMap<Karte, Integer>, Karte> accumulator() {
			return (m, k) -> m.put(k, m.getOrDefault(k, 0)+1);
		}

		@Override public BinaryOperator<HashMap<Karte, Integer>> combiner() {
			return (a, b) -> {
				b.forEach((k, n) -> a.put(k, a.getOrDefault(k, 0)+1));
				return a;
			};
		}

		@SuppressWarnings("deprecation")
		@Override public Function<HashMap<Karte, Integer>, String> finisher() {
			return m -> m.entrySet().stream()
				.sorted((a, b) -> b.getKey().getKosten()-a.getKey().getKosten())
				.map(e -> e.getValue()==1 ? e.getKey().getName() : e.getValue()+"x "+e.getKey().getName())
				.collect(Collectors.joining(", "));
		}

		@SuppressWarnings("null")
		@Override public Set<Characteristics> characteristics() {
			return Collections.emptySet();
		}
		
	};


	public static final Karten EMPTY = new Karten();
	
	
	public static Karten vonBis(Karte von, Karte bis) {
		Karten karten = new Karten();
		for (int i = von.ordinal(); i<= bis.ordinal(); i++) {
			karten.append(Karte.values()[i]);
		}
		return karten;
	}
	
	
		// ========== Karten ==========


	ArrayList<Karte> list = new ArrayList<>();
	
	
	/** Erzeugt {@link Karten} mit anzahl mal {@link Karte}. 
	 * @param karte {@link Karte}, die anzahl mal auf {@link #list} liegen soll
	 * @param anzahl Anzahl der {@link Karte}n auf diesem {@link Karten} */
	public Karten(Karte karte, int anzahl) {
		for (int i = 0; i<anzahl; i++) {
			list.add(karte);
		}
	}


	/** Erzeugt leere {@link Karten}. */
	public Karten() {
	}
	
	
	public Karten(Karte... karten) {
		for (Karte karte : karten) {
			list.add(karte);
		}
	}


	/** @return Sind die {@link Karten} leer? */
	public boolean isEmpty() {
		return list.isEmpty();
	}


	/** Leert die {@link Karten}. */
	public void clear() {
		list.clear();
	}
	
	
	public int size() {
		return list.size();
	}

	
	/** @param index Oberste Karte hat index 0 */
	public Karte get(int index) {
		return list.get(index);
	}
	
	
	public boolean enthält(Karte karte) {
		return stream()
			.anyMatch(k -> k==karte);
	}
	
	
	/** @return erster Index oder -1 */
	public int firstIndexOf(Karte karte) {
		return list.indexOf(karte);
	}


	/** @return Die {@link Karten} als {@link List}. */
	public List<Karte> asList() {
		return Collections.unmodifiableList(list);
	}
	
	
	/** Mischt den {@link Karten}. */
	public void mischen() {
		Collections.shuffle(list);
	}
	
	/** @return {@link Stream} der {@link Karten} */
	public Stream<Karte> stream() {
		return list.stream();
	}


	/** Legt die {@link Karte} oben (index=0) auf die {@link Karten}.
	 * @param karte {@link Karte}, die auf den {@link Karten} gelegt wird. */
	public void legeAb(Karte karte) {
		list.add(0, karte);
	}


	/** Legt den QuellStapel komplett oben auf dem {@link #list} ab. Der Quell-Stapel ist danach leer.   
	 * @param quellStapel {@link Karten}, der abgelegt werden soll. */
	public void legeAlleAbVon(Karten quellStapel) {
		list.addAll(0, quellStapel.asList());
		quellStapel.clear();
	}
	
	
	public void insert(Integer index, Karte karte) {
		list.add(index, karte);
	}


	/** Legt die Karte ganz unten in den Stapel */
	public void append(Karte karte) {
		list.add(karte);
	}


	/** Zieht die oberste {@link Karte} von den {@link Karten}. */
	public Karte ziehe() {
		return list.remove(0);
	}


	public Karte ziehe(int index) {
		return list.remove(index);
	}
	
	
	public void entferne(Karte karte) {
		list.remove(karte);
	}


	public void entferne(int index) {
		list.remove(index);
	}


	@Override public Iterator<Karte> iterator() {
		return list.iterator();
	}


	@Override public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i<list.size(); i++) {
			sb.append(i+1);
			sb.append(":");
			sb.append(list.get(i).getName());
			if (i<list.size()-1) {
				sb.append(", ");
			}
		}
		return sb.toString();
	}


	public Karten reverse() {
		Collections.reverse(list);
		return this;
	}


}
