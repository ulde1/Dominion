package de.eppelt.roland.dominion;

import java.util.function.Function;
import java.util.function.Supplier;

import org.eclipse.jdt.annotation.Nullable;

import de.eppelt.roland.dominion.action.Aktion;
import de.eppelt.roland.dominion.action.AufgabeAktion;
import de.eppelt.roland.dominion.action.Bridge;
import de.eppelt.roland.dominion.action.Burggraben;
import de.eppelt.roland.dominion.action.Diplomatin;
import de.eppelt.roland.dominion.action.DranAufgabeAktion;
import de.eppelt.roland.dominion.action.Einfach;
import de.eppelt.roland.dominion.action.Hexe;
import de.eppelt.roland.dominion.action.Händlerin;
import de.eppelt.roland.dominion.action.MaskeradeAktion;
import de.eppelt.roland.dominion.action.Miliz;
import de.eppelt.roland.dominion.action.Ratsversammlung;
import de.eppelt.roland.dominion.action.Reaktion;
import de.eppelt.roland.dominion.action.ShantyTown;
import de.eppelt.roland.dominion.action.Verschwörer;
import de.eppelt.roland.dominion.task.Abenteurer;
import de.eppelt.roland.dominion.task.Anbau;
import de.eppelt.roland.dominion.task.Aufgabe;
import de.eppelt.roland.dominion.task.Austausch;
import de.eppelt.roland.dominion.task.Banditin;
import de.eppelt.roland.dominion.task.Baron;
import de.eppelt.roland.dominion.task.Bergwerk;
import de.eppelt.roland.dominion.task.Bibliothek;
import de.eppelt.roland.dominion.task.Burghof;
import de.eppelt.roland.dominion.task.Bürokrat;
import de.eppelt.roland.dominion.task.Dieb;
import de.eppelt.roland.dominion.task.Eisenhütte;
import de.eppelt.roland.dominion.task.Festmahl;
import de.eppelt.roland.dominion.task.Geheimgang;
import de.eppelt.roland.dominion.task.Geldverleiher;
import de.eppelt.roland.dominion.task.Handelsposten;
import de.eppelt.roland.dominion.task.HandkartenAblegenEntsorgen;
import de.eppelt.roland.dominion.task.HandkartenAblegenEntsorgen.Verwendung;
import de.eppelt.roland.dominion.task.HandkartenAblegenEntsorgen.Zähle;
import de.eppelt.roland.dominion.task.Handlanger;
import de.eppelt.roland.dominion.task.Herumtreiberin;
import de.eppelt.roland.dominion.task.Höflinge;
import de.eppelt.roland.dominion.task.Kanzler;
import de.eppelt.roland.dominion.task.Keller;
import de.eppelt.roland.dominion.task.Kerkermeister;
import de.eppelt.roland.dominion.task.Lakai;
import de.eppelt.roland.dominion.task.Mine;
import de.eppelt.roland.dominion.task.Mühle;
import de.eppelt.roland.dominion.task.Nobles;
import de.eppelt.roland.dominion.task.OpferAufgabe;
import de.eppelt.roland.dominion.task.Patrouille;
import de.eppelt.roland.dominion.task.Spion;
import de.eppelt.roland.dominion.task.Thronsaal;
import de.eppelt.roland.dominion.task.Torwächterin;
import de.eppelt.roland.dominion.task.Trickser;
import de.eppelt.roland.dominion.task.Töpferei;
import de.eppelt.roland.dominion.task.Umbau;
import de.eppelt.roland.dominion.task.Vasall;
import de.eppelt.roland.dominion.task.Verwalter;
import de.eppelt.roland.dominion.task.Vorbotin;
import de.eppelt.roland.dominion.task.Werkstatt;
import de.eppelt.roland.dominion.task.Wilddiebin;
import de.eppelt.roland.dominion.task.Wunschbrunnen;


/** Eine Spielkarte mit {@link #getName()}, {@link #getAktion()}, {@link #getPreis()}, {@link #getImage()} und {@link #getPunkte()}.
 * @author Roland M. Eppelt 
 * @see http://wiki.dominionstrategy.com/index.php/Dominion_(Base_Set) */
public enum Karte {
		// Basiskarten
	KUPFER(0, 1, 0, "Copper", new int[] { 46, 39, 32, 25, 18 }), // zzgl. Startkarten der Spieler = 60
	SILBER(3, 2, 0, "Silver", 40),
	GOLD(6, 3, 0, "Gold", 30),
	ANWESEN(2, 0, 1, "Estate", 20), // zzgl. Startkarten der Spieler
	HERZOGTUM(5, 0, 3, "Duchy"),
	PROVINZ(8, 0, 6, "Province", new int[] { 8, 12, 12, 15, 18 }),
	FLUCH(0, 0, -1, "Curse", new int[] { 10, 20, 30, 40, 50 }),
		// Dominion 2. Edition
	BURGGRABEN(2, 0, 0, "Moat", new Burggraben()), 
	KAPELLE(2, 0, 0, "Chapel", () -> new HandkartenAblegenEntsorgen(Verwendung.ENTSORGEN, Zähle.BISZU, 4)),
	KELLER(2, 0, 0, "Cellar", Keller::new), 
	DORF(3, 0, 0, "Village", new Einfach(1, 2, 0, 0)),
	HÄNDLERIN(3, 0, 0, "Merchant", new Händlerin()),
	VASALL(3, 0, 0, "Vassal", Vasall::new),
	VORBOTIN(3, 0, 0, "Harbinger", Vorbotin::new),
	WERKSTATT(3, 0, 0, "Workshop", Werkstatt::new), 
	BÜROKRAT(4, 0, 0, "Bureaucrat", Bürokrat::new),
	GÄRTEN(4, 0, 1, "Gardens"),
	MILIZ(4, 0, 0, "Militia", new Miliz()),
	GELDVERLEIHER(4, 0, 0, "Moneylender", Geldverleiher::new),
	SCHMIEDE(4, 0, 0, "Smithy", new Einfach(3, 0, 0, 0)),
	WILDDIEBIN(4, 0, 0, "Poacher", Wilddiebin::new),
	THRONSAAL(4, 0, 0, "Throne_Room", Thronsaal::new),
	UMBAU(4, 0, 0, "Remodel", Umbau::new), 
	BANDITIN(5, 0, 0, "Bandit", Banditin::new),
	BIBLIOTHEK(5, 0, 0, "Library", Bibliothek::new),
	HEXE(5, 0, 0, "Witch", new Hexe()),
	JAHRMARKT(5, 0, 0, "Festival", new Einfach(0, 2, 1, 2)), 
	LABORATORIUM(5, 0, 0, "Laboratory", new Einfach(2, 1, 0, 0)), 
	MARKT(5, 0, 0, "Market", new Einfach(1, 1, 1, 1)), 
	MINE(5, 0, 0, "Mine", Mine::new),
	RATSVERSAMMLUNG(5, 0, 0, "Council_Room", new Ratsversammlung()),
	TORWÄCHTERIN(5, 0, 0, "Sentry", Torwächterin::new),
	TÖPFEREI(6, 0, 0, "Artisan", Töpferei::new),
		// Dominion 1. Edition
	KANZLER(3, 0, 0, "Chancellor", Kanzler::new),
	HOLZFÄLLER(3, 0, 0, "Woodcutter", new Einfach(0, 0, 1, 2)), 
	FESTMAHL(4, 0, 0, "Feast", Festmahl::new),
	SPION(4, 0, 0, "Spy", Spion::new),
	DIEB(4, 0, 0, "Thief", Dieb::new), 
	ABENTEURER(6, 0, 0, "Adventurer", Abenteurer::new),
		// Intrige 2. Edition
	BURGHOF(2, 0, 0, "Courtyard", Burghof::new),
	HERUMTREIBERIN(2, 0, 0, "Lurker", Herumtreiberin::new),
	HANDLANGER(2, 0, 0, "Pawn", Handlanger::new),
	MASKERADE(3, 0, 0, "Masquerade", new MaskeradeAktion()),
	ARMENVIERTEL(3, 0, 0, "Shanty_Town", new ShantyTown()),
	VERWALTER(3, 0, 0, "Steward", Verwalter::new),
	TRICKSER(3, 0, 0, "Swindler", Trickser::new),
	WUNSCHBRUNNEN(3, 0, 0, "Wishing_Well", Wunschbrunnen::new),
	BARON(4, 0, 0, "Baron", Baron::new),
	BRÜCKE(4, 0, 0, "Bridge", new Bridge()),
	VERSCHWÖRER(4, 0, 0, "Conspirator", new Verschwörer()),
	DIPLOMATIN(4, 0, 0, "Diplomat", new Diplomatin()),
	EISENHÜTTE(4, 0, 0, "Ironworks", Eisenhütte::new),
	MÜHLE(4, 0, 1, "Mill", Mühle::new),
	BERGWERK(4, 0, 0, "Mining_Village", Bergwerk::new),
	GEHEIMGANG(4, 0, 0, "Secret_Passage", Geheimgang::new),
	HÖFLINGE(5, 0, 0, "Courtier", Höflinge::new),
	HERZOG(5, 1, 0, "Duke", new int[] { 8, 12, 12, 12, 12 }),
	LAKAI(5, 0, 0, "Minion", Lakai::new),
	PATROUILLE(5, 0, 0, "Patrol", Patrouille::new),
	AUSTAUSCH(5, 0, 0, "Replace", Austausch::new),
	KERKERMEISTER(5, 0, 0, "Torturer", Kerkermeister::new),
	HANDELSPOSTEN(5, 0, 0, "Trading_Post", Handelsposten::new),
	ANBAU(5, 0, 0, "Upgrade", Anbau::new),
	HAREM(6, 2, 2, "Harem"),
	ADELIGE(6, 0, 2, "Nobles", Nobles::new)
	
	/*,
		// Promokarten
	PRINZ(8, 0, 0, "Prince", null), 
	SAUNA(4, 0, 0, "Sauna", null), 
	EISLOCH(5, 0, 0, "Avanto", null)
	*/; 
	
	
	/** Kosten der {@link Karte} in Geldmünzen */
	int kosten;
	/** Wert in Geldmünzen */
	int wert;
	/** Anzahl der Siegpunkte der {@link Karte} */
	int punkte;
	/** Aussehen der {@link Karte} */
	String image;
	/** {@link Aktion} der {@link Karte} */
	@Nullable Aktion aktion;
	/** Anzahl der Karten je nach Spielerzahl 2..6 */
	int[] anzahl;


	/** Erzeugt eine {@link Karte}. */
	private Karte(int kosten, int wert, int punkte, String image, @Nullable Aktion aktion, int[] anzahl) {
		this.aktion = aktion;
		this.kosten = kosten;
		this.wert = wert;
		this.punkte = punkte;
		this.image = image;
		this.anzahl = anzahl;
		if (aktion!=null) {
			aktion.setName(getName());
		}
	}


	/** Erzeugt eine {@link Karte}. */
	private Karte(int kosten, int wert, int punkte, String image, int[] anzahl) {
		this(kosten, wert, punkte, image, (Aktion) null, anzahl);
	}
	
	
	/** Erzeugt eine {@link Karte}. */
	private Karte(int kosten, int wert, int punkte, String image, Supplier<Aufgabe> aufgabeSupplier, int[] anzahl) {
		this(kosten, wert, punkte, image, new AufgabeAktion(aufgabeSupplier), anzahl);
	}


	/** Erzeugt eine {@link Karte}. */
	private Karte(int kosten, int wert, int punkte, String image, Function<Dran, Aufgabe> dranAufgabeSupplier, int[] anzahl) {
		this(kosten, wert, punkte, image, new DranAufgabeAktion(dranAufgabeSupplier), anzahl);
	}


	/** Erzeugt eine {@link Karte}. */
	private Karte(int kosten, int wert, int punkte, String image, @Nullable Aktion aktion) {
		this(kosten, wert, punkte, image, aktion, punkte==0 ? Vorrat.ZEHN : Vorrat.ACHTZWÖLF);
	}
	
	
	/** Erzeugt eine {@link Karte}. */
	private Karte(int kosten, int wert, int punkte, String image) {
		this(kosten, wert, punkte, image, (Aktion) null, punkte==0 ? Vorrat.ZEHN : Vorrat.ACHTZWÖLF);
	}
	
	
	/** Erzeugt eine {@link Karte}. */
	private Karte(int kosten, int wert, int punkte, String image, Supplier<Aufgabe> aufgabeSupplier) {
		this(kosten, wert, punkte, image, new AufgabeAktion(aufgabeSupplier), punkte==0 ? Vorrat.ZEHN : Vorrat.ACHTZWÖLF);
	}


	/** Erzeugt eine {@link Karte}. */
	private Karte(int kosten, int wert, int punkte, String image, Function<Dran, Aufgabe> dranAufgabeSupplier) {
		this(kosten, wert, punkte, image, new DranAufgabeAktion(dranAufgabeSupplier), punkte==0 ? Vorrat.ZEHN : Vorrat.ACHTZWÖLF);
	}


	/** Erzeugt eine {@link Karte}. */
	private Karte(int kosten, int wert, int punkte, String image, int anzahl) {
		this(kosten, wert, punkte, image, (Aktion) null, new int[] { anzahl, anzahl, anzahl, anzahl, anzahl });
	}
	
	
	/** @return Name der {@link Karte} */
	public String getName() {
		return name().substring(0, 1)+name().substring(1).toLowerCase();
	}


	/** @return Kosten der {@link Karte} in Geldmünzen
	 * @deprecated Besser {@link #getKosten(Dominion)}*/
	@Deprecated public int getKosten() {
		return kosten;
	}
	
	
	/** @return Kosten der {@link Karte} in Geldmünzen */
	public int getKosten(Dominion dominion) {
		return dominion.getKosten(this);
	}


	/** @return Wert der {@link Karte} in Geldmünzen */
	public int getWert() {
		return wert;
	}
	
	
	public boolean isGeld() {
		return getWert()>0;
	}
	
	
	/** @return Anzahl der Siegpunkte der {@link Karte} */
	public int getPunkte(Spieler spieler) {
		if (this==GÄRTEN) {
			return spieler.countKarten()/10;
		} else if (this==HERZOG) {
			return (int) spieler.alleKarten().filter(k -> k==Karte.HERZOGTUM).count();
		} else {
			return punkte;
		}
	}
	
	
	public boolean hatPunkte() {
		return punkte>0;
	}
	
	
	/** @return Aussehen der {@link Karte} */
	public String getImage() {
		return "<img src=\"p/dmn/"+image+".jpg\" alt=\""+getName()+"\" />";
//		return getName();
	}


	/** @return {@link Aktion} der {@link Karte} */
	public @Nullable Aktion getAktion() {
		return aktion;
	}
	
	
	public boolean isAktion() {
		return aktion!=null;
	}
	
	
	public boolean isMöglicheReaktion(OpferAufgabe aufgabe) {
		return aktion instanceof Reaktion && ((Reaktion) aktion).reaktionMöglich(aufgabe);
	}
	
	
	/** @return Wie oft gehört diese Karte bei dieser Anzahl Mitspieler in den Vorrat? */
	public int getAnzahl(int spieler) {
		return spieler<2 ? anzahl[0] : spieler>6 ? anzahl[5] : anzahl[spieler-2];
	}


	public void show(StringBuffer sb) {
		sb.append(getImage());
	}


	@Override public String toString() {
		return getName();
	}
	
	
	public static int countKartenTypen(Karte karte) {
		int result = 0;
		if (karte.hatPunkte()) {
			result++;
		}
		if (karte.isAktion()) {
			result++;
		}
		if (karte.getAktion() instanceof Reaktion) { // ist Reaktion immer eine Aktion?
			result++;
		}
		if (karte.getWert()>0) {
			result++;
		}
		return result;
	}


}
