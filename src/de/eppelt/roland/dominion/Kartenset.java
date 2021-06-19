package de.eppelt.roland.dominion;

import static de.eppelt.roland.dominion.Karte.*;

import org.eclipse.jdt.annotation.Nullable;

public class Kartenset {


	public static final Kartenset[] RECOMMENDED = new Kartenset[] {
		new Kartenset("Erstes Spiel", "Dominion Edition 2", KELLER, MARKT, HÄNDLERIN, MILIZ, MINE, BURGGRABEN, UMBAU, SCHMIEDE, DORF, WERKSTATT),
		new Kartenset("Größenverzerrung", TÖPFEREI, BANDITIN, BÜROKRAT, KAPELLE, JAHRMARKT, GÄRTEN, TORWÄCHTERIN, THRONSAAL, HEXE, WERKSTATT),
		new Kartenset("Deck Top", TÖPFEREI, BÜROKRAT, RATSVERSAMMLUNG, JAHRMARKT, VORBOTIN, LABORATORIUM, GELDVERLEIHER, TORWÄCHTERIN, VASALL, DORF),
		new Kartenset("Fingerfertigkeit", KELLER, RATSVERSAMMLUNG, JAHRMARKT, GÄRTEN, BIBLIOTHEK, VORBOTIN, MILIZ, WILDDIEBIN, SCHMIEDE, THRONSAAL),
		new Kartenset("Verbesserungen", TÖPFEREI, KELLER, MARKT, HÄNDLERIN, MINE, BURGGRABEN, GELDVERLEIHER, WILDDIEBIN, UMBAU, HEXE),
		new Kartenset("Silber & Gold", BANDITIN, BÜROKRAT, KAPELLE, VORBOTIN, LABORATORIUM, HÄNDLERIN, MINE, GELDVERLEIHER, THRONSAAL, VASALL),
		new Kartenset("Siegestanz", "Intrige Edition 2", BARON, HÖFLINGE, HERZOG, HAREM, EISENHÜTTE, MASKERADE, MÜHLE, ADELIGE, PATROUILLE, AUSTAUSCH),
		new Kartenset("Dicke Luft", VERSCHWÖRER, EISENHÜTTE, HERUMTREIBERIN, HANDLANGER, BERGWERK, GEHEIMGANG, VERWALTER, TRICKSER, KERKERMEISTER, HANDELSPOSTEN),
		new Kartenset("Alles Gute!", BARON, VERSCHWÖRER, BURGHOF, DIPLOMATIN, HERZOG, GEHEIMGANG, ARMENVIERTEL, KERKERMEISTER, ANBAU, WUNSCHBRUNNEN),
		new Kartenset("Untergebene", "Intrige & Dominion", KELLER, JAHRMARKT, BIBLIOTHEK, TORWÄCHTERIN, VASALL, HÖFLINGE, DIPLOMATIN, LAKAI, ADELIGE, HANDLANGER),
		new Kartenset("Großer Plan", TÖPFEREI, RATSVERSAMMLUNG, MARKT, MILIZ, WERKSTATT, BRÜCKE, MÜHLE, BERGWERK, PATROUILLE, ARMENVIERTEL),
		new Kartenset("Rückbau", BANDITIN, MINE, UMBAU, THRONSAAL, DORF, DIPLOMATIN, HAREM, HERUMTREIBERIN, AUSTAUSCH, TRICKSER)
	};

	
	protected String name;
	protected @Nullable String titel;
	protected Karten karten;


	public Kartenset(String name, @Nullable String titel, Karten karten) {
		super();
		this.name = name;
		this.titel = titel;
		this.karten = karten;
	}


	public Kartenset(String name, Karten karten) {
		this(name, null, karten);
	}


	public Kartenset(String name, @Nullable String titel, Karte... karten) {
		this(name, titel, new Karten(karten));
	}


	public Kartenset(String name, Karte... karten) {
		this(name, null, karten);
	}


	public String getName() {
		return name;
	}
	
	
	public @Nullable String getTitel() {
		return titel;
	}


	public Karten getKarten() {
		return karten;
	}


	public void setKarten(Karten karten) {
		this.karten = karten;
	}


}
