package de.eppelt.roland.dominion;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.Nullable;

import de.eppelt.roland.dominion.task.AktionAusführen;
import de.eppelt.roland.dominion.task.OpferAufgabe;
import de.eppelt.roland.dominion.task.WähleVorrat;
import de.eppelt.roland.dominion.ui.UI;
import de.eppelt.roland.game.HttpGame;
import de.eppelt.roland.game.HttpGameInstance;
import de.tesd.collection.HashMap;
import de.tesd.util.Loggers;
import de.tesd.util.O;


/** Das Dominion-Spiel mit #getMitte
 * @author Roland M. Eppelt */
public class Dominion extends HttpGameInstance<Dominion, Client, Spieler> implements Loggers {
	
	
	public final static Logger LOG = Logger.getLogger(Dominion.class.getName());
	@Override public Logger logger() {  return LOG; }


	Vorrat vorrat = new Vorrat(this);
	/** Liste aller {@link Spieler} */
	ArrayList<Spieler> spielers = new ArrayList<>();
	/** Index des {@link #spielers}, der an der Reihe ist. */
	int dranIndex = -1;
	@Nullable Dran dran = null;
	String log = "";
	Karten trash = new Karten();
	protected Function<Karte, Integer> kosten = this::getOriginalKosten;
	HashSet<Spieler> needsUpdate = new HashSet<>();
	
	
	/** Erzeugt ein {@link Dominion}. */
	public Dominion(HttpGame<Dominion, Client, Spieler> game) {
		super(game);
		openForNewPlayers();
	}


	public void setVorrat(Vorrat vorrat) {
		this.vorrat = vorrat;
	}


	public void addSpieler(Spieler s) {
		spielers.add(s);
		if (spielers.size()==1) {
			derNächsteBitte();
			spielers.get(0).sofortAufgabe(new WähleVorrat());
			logEintrag(spielers.get(0).getName()+" ist erster Spieler und sucht gerade das Kartenset aus.");
		}
		getVorrat().setSpieler(spielers.size());
	}
	
	
	public void removeSpieler(Spieler spieler) {
		spielers.remove(spieler);
		getVorrat().setSpieler(spielers.size());
	}
	
	
	public void start(Karten karten) {
		vorrat.setKarten(karten);
		derNächsteBitte();
	}

	
	/** @return Der {@link Vorrat} dieses {@link Dominion}s. */
	public Vorrat getVorrat() {
		return vorrat;
	}
	
	
	@Override public @Nullable Spieler getActive() {
		return O.nn(getDran(), Dran::getSpieler);
	}
	
	
	public ArrayList<Spieler> getSpieler() {
		return spielers;
	}
	
	
	@SuppressWarnings("deprecation")
	public int getOriginalKosten(Karte karte) {
		return karte.getKosten();
	}


	public int getKosten(Karte karte) {
		return kosten.apply(karte);
	}
	
	
	public Function<Karte, Integer> getKosten() {
		return kosten;
	}
	
	
	public void setKosten(Function<Karte, Integer> kosten) {
		this.kosten = kosten;
	}
	
	
	/** @return {@link Spieler}, der an der Reihe ist */
	public @Nullable Dran getDran() {
		return dran;
	}
	
	
	public void dran(Consumer<Dran> consumer) {
		Dran dran = getDran();
		if (dran!=null) {
			consumer.accept(dran);
		}
	}
	
	
	public void addAktionen(int aktionen) {
		dran(d -> d.addAktionen(aktionen));
	}
	
	
	public void addGeld(int geld) {
		dran(d -> d.addGeld(geld));
	}
	
	
	public void addKäufe(int käufe) {
		dran(d -> d.addKäufe(käufe));
	}
	
	
	public Karten getTrash() {
		return trash;
	}
	
	
	public void angriff(BiFunction<Spieler, Spieler, OpferAufgabe> supplier) {
		dran(d -> d.getSpieler().angriff(supplier));
	}


	/** {@link #getDran()} wechselt zum nächsten Spieler. */
	public void derNächsteBitte() {
		if (letzterZug()) {
			dran = null;
			updateAllPlayers();
		} else {
			dranIndex = (dranIndex+1)%spielers.size();
			Dran neuDran = new Dran(spielers.get(dranIndex));
			dran = neuDran;
			Spieler spieler = neuDran.getSpieler();
			kosten = this::getOriginalKosten;
			spieler.putAufgabe(new AktionAusführen(neuDran));
			spieler.updateOtherPlayers();
		}
	}
	
	
	public void needsUpdate(Spieler spieler) {
		needsUpdate.add(spieler);
	}
	
	
	public void sendUpdatesNow() {
		// Wiederholen bis wirklich kein Update mehr ansteht
		while (!needsUpdate.isEmpty()) {
			HashSet<Spieler> temp = needsUpdate;
			needsUpdate = new HashSet<>();
			stream()
				.filter(client -> temp.contains(client.getPlayer()))
				.forEach(Client::updateNow);
		}
	}
	
	
	public String endeStatus() {
		int provinzen = vorrat.getAnzahl(Karte.PROVINZ);
		int leereStapel = vorrat.getLeereStapel().size();
		return "noch " + provinzen + (provinzen==1 ? " Provinz, " : " Provinzen, ")
			+ leereStapel + (leereStapel==1 ? " leerer Stapel" : " leere Stapel")
			+ (leereStapel>0 ? ": "+vorrat.getLeereStapel().stream().map(Karte::getName).collect(Collectors.joining(", ")) : "");
	}


	public void logEintrag(String message) {
		config(() -> message);
		log = message;
	}


	public String getLog() {
		return log;
	}
	
	
	public void logDominion() {
		finer(() -> "Vorrat: "+vorrat.toString());
		Dran dran = getDran();
		if (dran!=null) {
			finer(() -> "Dran: "+dran.toString());
			finer(() -> "N: "+dran.getSpieler().getNachziehStapel().toString());
			finer(() -> "H: "+dran.getSpieler().getHandkarten().toString());
			finer(() -> "S: "+dran.getSpieler().getSeite().toString());
			finer(() -> "A: "+dran.getSpieler().getAblageStapel().toString());
		}
	}
	
	
	/** @return sind wir in/nach dem letzten Zug, weil {@link Dominion} gleich/schon zu Ende ist?*/
	public boolean letzterZug() {
		Vorrat vorrat = getVorrat();
		return !vorrat.hat(Karte.PROVINZ) || vorrat.getLeereStapel().size()>=3; 
	}


	/** @return Ist das {@link Dominion} zu Ende? */
	public boolean zuEnde() {
		return !vorrat.isEmpty() && letzterZug() && dran==null;
	}
	
	
	public void checkAlleKartenImSpiel(UI ui) {
		Set<Karte> alleKarten = getVorrat().getAlleKarten();
			// Zählen
		HashMap<Karte, Integer> anzahl = new HashMap<>();
		for (Karte karte : alleKarten) {
			anzahl.put(karte, anzahl.getOr(karte, 0)+getVorrat().getAnzahl(karte));
		}
		for (Spieler spieler : spielers) {
			spieler.alleKarten().forEach(karte -> anzahl.put(karte, anzahl.getOr(karte, 0)+1));
			anzahl.put(Karte.KUPFER, anzahl.getOr(Karte.KUPFER, 0)-7);
			anzahl.put(Karte.ANWESEN, anzahl.getOr(Karte.ANWESEN, 0)-3);
		}
		getTrash().forEach(karte -> anzahl.put(karte, anzahl.getOr(karte, 0)+1));
			// Prüfen
		boolean first = true;
		for (Karte karte : Karte.values()) {
			int ist = anzahl.getOr(karte, 0);
			int soll = alleKarten.contains(karte) ? karte.getAnzahl(getVorrat().getSpieler()) : 0;
			if (ist!=soll) {
				if (first) {
					ui.title("Kartenanzahl stimmt nicht");
					first = false;
				}
				ui.say("Es gibt ");
				ui.say(ist);
				ui.say("x ");
				ui.say(karte.getName());
				ui.say(" statt ");
				ui.say(soll);
				ui.sayln("x");
			}
		}
	}



	@Override public String toString() {
		return spielers.stream()
			.map(Spieler::getName)
			.collect(Collectors.joining(", "));
	}


}