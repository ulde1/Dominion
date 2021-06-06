package de.eppelt.roland.dominion;


import java.util.ArrayList;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.Nullable;

import de.eppelt.roland.dominion.task.AktionAusführen;
import de.eppelt.roland.dominion.task.OpferAufgabe;
import de.eppelt.roland.dominion.task.WähleVorrat;
import de.eppelt.roland.game.HttpGame;
import de.eppelt.roland.game.HttpGameInstance;
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
		} else {
			vorrat.add(Karte.FLUCH, 10);
		}
	}
	
	
	public void removeSpieler(Spieler spieler) {
		spielers.remove(spieler);
		vorrat.add(Karte.FLUCH, -10);
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
		return zuEnde() ? null : dran;
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
		if (zuEnde()) {
			dran = null;
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


	/** @return Ist das {@link Dominion} zu Ende? */
	public boolean zuEnde() {
		Vorrat vorrat = getVorrat();
		return !vorrat.hat(Karte.PROVINZ) || vorrat.getLeereStapel().size()>=3; 
	}
	
	
//	/** Dieses {@link Dominion} in der Konsole spielen. */
//	public void consoleSpielen() throws NumberFormatException, IOException {
//		while (!zuEnde()) {
//			if (dran!=null) {
//				consoleSpielerAktion(dran);
//			}
//			derNächsteBitte();
//		}
//	}


//	/** Eine Runde für diesen {@link Spieler} auf der Konsole spielen. */
//	public void consoleSpielerAktion(Dran dran) throws NumberFormatException, IOException {
//		Spieler s = dran.getSpieler();
//		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
//		System.out.println("Spieler "+s+" ist dran.");
//		System.out.println("Deine Handkarten: "+s.getHandkarten());
//		// 1. Aktion ausspielen
//		Karten möglicheAktionen = s.möglicheAktionen();
//		while (dran.getAktionen()>0&&!möglicheAktionen.isEmpty()) {
//			System.out.println("Du hast folgende Aktionen: "+möglicheAktionen);
//			System.out.println("Welche Aktionskarte willst du ausspielen?");
//			int karteNr = Integer.parseInt(in.readLine());
//			if (karteNr==0) {
//				break;
//			}
//			Aktion aktion = möglicheAktionen.get(karteNr-1).getAktion();
//			assert aktion!=null : "aktion==null";
//			aktion.ausführen(this);
//			möglicheAktionen.list.remove(karteNr-1);
//			dran.addAktionen(-1);
//		}
//		// 2. Kaufen
//		int geld = s.geld()+dran.getGeld();
//		while (dran.getKäufe()>0) {
//			System.out.println("Du hast "+geld+" Münzen.");
//			Karten kaufbareKarten = getVorrat().getKarten().stream().filter(karte -> getVorrat().hat(karte)).filter(karte -> karte.getKosten()<=geld).collect(Karten.COLLECT);
//			System.out.println("Du kannst kaufen: "+kaufbareKarten);
//			System.out.println("Welche Karte willst du kaufen?");
//			int karteNr = Integer.parseInt(in.readLine());
//			if (karteNr==0) {
//				break;
//			}
//			Karte gekaufteKarte = kaufbareKarten.get(karteNr-1);
//			getVorrat().zieheKarte(gekaufteKarte);
//			s.getAblageStapel().legeAb(gekaufteKarte);
//		}
//		// 3. Aufräumen
//		s.handkartenAblegen();
//		s.handkartenAuffüllen();
//	}


	@Override public String toString() {
		return spielers.stream()
			.map(Spieler::getName)
			.collect(Collectors.joining(", "));
	}


}