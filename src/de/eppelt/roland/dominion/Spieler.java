package de.eppelt.roland.dominion;

import java.util.ArrayDeque;
import java.util.function.BiFunction;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.Nullable;

import de.eppelt.roland.dominion.task.Aufgabe;
import de.eppelt.roland.dominion.task.EndeAufgabe;
import de.eppelt.roland.dominion.task.KeineAufgabe;
import de.eppelt.roland.dominion.task.OpferAufgabe;
import de.eppelt.roland.dominion.task.Schutz;
import de.eppelt.roland.game.GamePlayer;
import de.tesd.util.Loggers;
import de.tesd.util.O;
import de.tesd.util.Streams;


/** Mensch, der bei einer Dominion-Runde mitspielt mit {@link #getName()}. */
public class Spieler implements GamePlayer<Dominion, Client, Spieler>, DominionEase, Loggers {

	
	public final static Logger LOG = Logger.getLogger(Spieler.class.getName());
	@Override public Logger logger() { return LOG; }
	
	
	public static class EmptyDeckException extends Exception {
		private static final long serialVersionUID = 1L;
		public EmptyDeckException() { super(); }
		public EmptyDeckException(String message) { super(message); }
	}
	

	Dominion dominion;
	/** Name des {@link Spieler}s */
	String name, namens;
	/** Der Nachziehstapel des {@link Spieler} */
	Karten nachziehStapel = new Karten();
	/** Die Handkarten des {@link Spieler} */
	Karten handkarten = new Karten();
	/** Die zur Seite gelegten Karten; werden beim nächsten Aufräumen abgelegt. */
	Karten seite = new Karten();
	/** Der Ablagestapel des {@link Spieler} */
	Karten ablage = new Karten();
	/** Die Aufgaben des {@link Spieler}s */
	ArrayDeque<Aufgabe> aufgaben = new ArrayDeque<>();


	/** Erzeugt einen {@link Spieler} */
	public Spieler(Dominion dominion, String name) {
		this.dominion = dominion;
		this.name = name.trim();
		this.namens = this.name+(name.matches(".*[szßSZ]$") ? "&apos;" : "s");
		for (int i = 0; i<3; i++) {
			nachziehStapel.legeAb(Karte.ANWESEN);
		}
		for (int i = 0; i<7; i++) {
			nachziehStapel.legeAb(Karte.KUPFER);
		}
		nachziehStapel.mischen();
		try {
			handkartenAuffüllen();
		} catch (EmptyDeckException e) {
			warning(e);
		}
		dominion.addSpieler(this);
	}


	/** @return Name des {@link Spieler}s */
	@Override public String getName() {
		return name;
	}
	
	
	public String getNamens() {
		return namens;
	}


	@Override public Dominion getInstance() {
		return dominion;
	}
	
	
	/** @return Der Nachziehstapel des {@link Spieler}s */
	public Karten getNachziehStapel() {
		return nachziehStapel;
	}


	/** @return Die Hand-{@link Karten} des {@link Spieler}s */
	public Karten getHandkarten() {
		return handkarten;
	}
	
	
	/** @return Die zur Seite gelegten Karten; werden beim nächsten Aufräumen abgelegt. */
	public Karten getSeite() {
		return seite;
	}


	/** @return Der {@link Karten Ablagestapel} des {@link Spieler}s */
	public Karten getAblageStapel() {
		return ablage;
	}
	
	
	public void putAufgabe(Aufgabe aufgabe) {
		aufgaben.addLast(aufgabe);
		if (aufgaben.size()==1) {
			updateMe();
		}
	}
	
	
	public void nextAufgabe(Aufgabe aufgabe) {
		@Nullable Aufgabe first = aufgaben.pollFirst();
		aufgaben.addFirst(aufgabe);
		if (first!=null) {
			aufgaben.addFirst(first);
		}
		if (aufgaben.size()==1) {
			updateMe();
		}
	}


	public void sofortAufgabeOhneUpdate(Aufgabe aufgabe) {
		aufgaben.addFirst(aufgabe);
	}
	
	
	public void sofortAufgabe(Aufgabe aufgabe) {
		aufgaben.addFirst(aufgabe);
		if (O.nn(getDran(), Dran::getSpieler)==this) {
			updateOtherPlayers();
		} else {
			updateMe();
		}
	}
	
	
	public void spielerHat(String message) {
		getInstance().logEintrag(getName()+" hat "+message);
	}
	
	
	public void updateMe() {
		getInstance().stream()
			.filter(c -> c.getPlayer()==this)
			.forEach(Client::sendHtml);
	}
	
	
	public void play(String url) {
		getInstance().stream()
			.filter(c -> c.getPlayer()==this)
			.forEach(c -> c.playAudio(DominionHttpServer.SOUND_PATH+url));
	}


	public Aufgabe currentAufgabe() {
		Aufgabe result = getInstance().zuEnde() ? new EndeAufgabe() : O.or(aufgaben.peek(), KeineAufgabe::new);
		return result;
	}
	
	
	public void aufgabeErledigt(Aufgabe aufgabe) {
		boolean first = aufgaben.peekFirst().equals(aufgabe);
		aufgaben.remove(aufgabe);
		if (first && O.nn(getInstance().getDran(), Dran::getSpieler)==this) {
			updateOtherPlayers();
		}
	}
	
	
	public void updateOtherPlayers() {
		getInstance().updatePlayersExcept(this);
		getInstance().logDominion();
	}


	/** @return Die Anzahl der in den {@link #getHandkarten()} verfügbare Münzen. */
	public int geld() {
		return getHandkarten().stream()
			.mapToInt(Karte::getWert)
			.sum();
	}
	
	
	public Karten möglicheAktionen() {
		return getHandkarten().stream()
			.filter(Karte::isAktion)
			.collect(Karten.COLLECT); 
	}


	/** Zieht eine Karte vom {@link #nachziehStapel} nach 
	 * @param aufDieHand zieht die Karte auf die Hand. Sonst ist der Aufrufer für die Karte zuständig. */
	public Karte zieheKarte(boolean aufDieHand) throws EmptyDeckException  {
		if (nachziehStapel.isEmpty()) {
			fine(() -> "Nachziehstapel Neu");			
			nachziehStapel.legeAlleAbVon(getAblageStapel());
			nachziehStapel.mischen();
		}
		if (nachziehStapel.isEmpty()) {
			throw new EmptyDeckException("Ablage- und Nachziehstapel sind leer");
		}
		Karte karte = nachziehStapel.ziehe();
		if (aufDieHand) {
			getHandkarten().legeAb(karte);
		}
		return karte;
	}


	/** Ziehl {@link Karte}n nach.
	 * @param anzahl Anzahl der {@link Karte}n, die vom {@link #nachziehStapel} auf die Hand nachgezogen werden.
	 * @see #zieheKarte(boolean)
	 */
	public void zieheKarten(int anzahl) {
		for (int i = 0; i<anzahl; i++) {
			try {
				zieheKarte(true);
			} catch (EmptyDeckException e) {
				// Manchmal sind die Zeiten hart.
			}
		}
	}
	
	
	public Karten zieheKartenKarten(int anzahl) {
		Karten karten = new Karten();
		for (int i = 0; i<anzahl; i++) {
			try {
				karten.legeAb(zieheKarte(false));
			} catch (EmptyDeckException e) {
				// Wenn weg, dann weg.
			}
		}
		return karten;
	}


	/** Füllt die {@link #handkarten} vom {@link #nachziehStapel} auf, so dass mindestens 5 {@link Karte}n enthalten sind. 
	 * @throws EmptyDeckException */
	public void handkartenAuffüllen() throws EmptyDeckException {
		zieheKarten(5-getHandkarten().list.size());
	}
	
	
	public void handkartenAblegen() {
		getAblageStapel().legeAlleAbVon(getHandkarten());
	}
	
	
	public Stream<Spieler> alleAnderenSpieler() {
		return dominion.stream()
			.map(Client::getPlayer)
			.distinct()
			.filter(p -> !p.equals(this));
	}


	public void angriff(BiFunction<Spieler, Spieler, OpferAufgabe> supplier) {
		alleAnderenSpieler()
			.forEach(p -> p.putAufgabe(new Schutz(supplier.apply(this, p))));
	}


	@Override public String toString() {
		return getName();
	}
	
	
	public int countKarten() {
		return nachziehStapel.size()+handkarten.size()+seite.size()+ablage.size();
	}


	public String getStatus() {
		Dran dran = dominion.getDran();
		String status = getPunkte()+(getPunkte()==1 ? " Punkt, " :" Punkte, ")+countKarten()+" Karten";
		status = status + " (N:"+nachziehStapel.size()+", H:"+handkarten.size()+", S:"+seite.size()+", A:"+ablage.size()+")";
		if (dran==null) {
			return status;
		} else if (this.equals(dran.getSpieler())) {
			return status+". Du bist dran.";
		} else {
			return status+". Warte auf "+dran.getSpieler().getName()+"."; 
		}
	}


	public int getPunkte() {
		return alleKarten()
			.mapToInt(k -> k.getPunkte(this))
			.sum();
	}


	public Stream<Karte> alleKarten() {
		return Streams.concat(getNachziehStapel().stream(), getHandkarten().stream(), getSeite().stream(), getAblageStapel().stream());
	}


}
