package de.eppelt.roland.dominion.ui;


import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.logging.Logger;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import de.eppelt.roland.dominion.Client;
import de.eppelt.roland.dominion.DominionHttpServer;
import de.eppelt.roland.dominion.Dran;
import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Karten;
import de.eppelt.roland.dominion.Spieler;
import de.eppelt.roland.game.HttpGameClient;
import de.tesd.collection.Lists;
import de.tesd.util.Loggers;
import de.tesd.util.Password;


public class HtmlUI implements UI, Loggers {


	public static final Logger LOG = Logger.getLogger(HtmlUI.class.getName());


	@Override public Logger logger() {
		return LOG;
	}


	static final int[] EMPTY = new int[0];

	boolean p = false;
	Client client;
	StringBuffer sb;
	HtmlHandler handler;
	boolean hasInputField = false;


	public HtmlUI(Client client, StringBuffer sb) {
		this.client = client;
		this.sb = sb;
		handler = new HtmlHandler(client);
	}


	@Override public void setClient(Client client) {
		this.client = client;
		handler.setClient(client);
	}


	@Override public Handler getHandler() {
		return handler;
	}


	@Override public void setStringBuffer(StringBuffer sb) {
		this.sb = sb;
	}


	@Override public Client getClient() {
		return client;
	}


	@Override public void header() {
		hasInputField = false;
		sb.append("<script src=\"p/dominion.js\"></script>\n");
		sb.append("<form action=\"\" method=\"post\">\n");
		sb.append("<input type=\"hidden\" id=\"session\" name=\"session\" value=\"");
		sb.append(client.getSession());
		sb.append("\" />\n");
		sb.append("<input type=\"hidden\" id=\"token\" name=\"token\" value=\"");
		sb.append(handler.getToken());
		sb.append("\" />\n");
		sb.append("<h1>Dominion (");
		sb.append(getSpieler().getName());
		sb.append(")</h1>\n");
		status();
	}


	@Override public void headerHandkarten() {
		header();
		handkarten(getSpieler());
	}


	@Override public void title(String name) {
		ln();
		sb.append("<h2>");
		sb.append(name);
		sb.append("</h2>\n");
	}


	@Override public void headerHandkartenTitle(String name) {
		headerHandkarten();
		title(name);
	}


	public void status() {
		ln();
		say(getSpieler().getStatus());
		br();
		say(getInstance().getLog());
		br();
		Dran dran = getDran();
		if (dran!=null && dran.getSpieler()!=getSpieler()) {
			Spieler spieler = dran.getSpieler();
			say(spieler.getName());
			say(" tut gerade ");
			say(spieler.currentAufgabe().getName());
			br();
		}
		say(getInstance().endeStatus());
		ln();
	}


	@Override public void karten(Karten karten, boolean vonOben) {
		if (vonOben) {
			for (Karte karte : karten) {
				sb.append(karte.getImage());
				sb.append("\n");
			}
		} else {
			for (int i = karten.size()-1; i>=0; i--) {
				sb.append(karten.get(i).getImage());
				sb.append("\n");
			}
		}
	}


	@Override public void handkarten(Spieler spieler) {
		ln();
		sb.append("<h2>");
		if (getSpieler().equals(spieler)) {
			sb.append("Meine");
		} else {
			sb.append(spieler.getNamens());
		}
		sb.append(" Handkarten</h2>\n<p class=\"handkarten\">");
		karten(spieler.getHandkarten(), false);
		sb.append("</p>\n");
	}


	protected void ensureP() {
		if (!p) {
			sb.append("<p>");
			p = true;
		}
	}


	@Override public void say(String message) {
		ensureP();
		sb.append(message);
	}


	@Override public void say(int i) {
		ensureP();
		sb.append(i);
	}


	@Override public void ln() {
		if (p) {
			sb.append("</p>\n");
			p = false;
		}
	}
	
	
	@Override public void br() {
		sb.append("<br />\n");
	}
	
	
	@Override public void play(String url) {
		sb.append("<audio src=\"");
		sb.append(url);
		sb.append("\" autoplay=\"true\"></audio>\n");
	}


	@Override public void button(String text, char accessKey, boolean ajax, Consumer<Handler> onClick) {
		ensureP();
		String idName = Password.generateNewPassword(7);
		Client.button(sb, idName, accessKey, text, ajax);
		handler.put(idName, l -> onClick.accept(getHandler()));
	}


	@Override public void button(Karte karte, boolean ajax, BiConsumer<Handler, Karte> onClick) {
		ensureP();
		String idName = Password.generateNewPassword(7);
		Client.button(sb, idName, karte.getImage(), 0);
		handler.put(idName, l -> onClick.accept(getHandler(), karte));
	}


	@Override public void karte(Karte karte) {
		ensureP();
		sb.append(karte.getImage());
	}


	@Override public void oneKarte(Karten karten, BiConsumer<Handler, Karte> onClick) {
		ensureP();
		String idName = Password.generateNewPassword(7);
		for (int i = karten.size()-1; i>=0; i--) {
			Client.button(sb, idName, karten.get(i).getImage(), i);
			sb.append("\n");
		}
		handler.put(idName, list -> onClick.accept(getHandler(), karte(karten, list)));
	}


	@Override public void oneIndex(Karten karten, BiConsumer<Handler, Integer> onClick) {
		ensureP();
		String idName = Password.generateNewPassword(7);
		for (int i = karten.size()-1; i>=0; i--) {
			Client.button(sb, idName, karten.get(i).getImage(), i);
			sb.append("\n");
		}
		handler.put(idName, list -> onClick.accept(getHandler(), Integer.parseInt(Lists.firstOr(list, "-1"))));
	}


	private static Karte karte(Karten karten, List<String> list) {
		return karten.get(Integer.parseInt(Lists.firstOr(list, "-1")));
	}


	@Override public @NonNull String any(Karten karten, @Nullable Karten selected) {
		hasInputField = true;
		ensureP();
		String idName = Password.generateNewPassword(7);
		for (int i = karten.size()-1; i>=0; i--) {
			sb.append("<input type=\"checkbox\" name=\"");
			sb.append(idName);
			sb.append("\" value=\"");
			sb.append(i);
			if (selected!=null&&selected.enthält(karten.get(i))) {
				sb.append("\" checked=\"true");
			}
			sb.append("\" id=\"");
			sb.append(idName);
			sb.append(i);
			sb.append("\">\n<label for=\"");
			sb.append(idName);
			sb.append(i);
			sb.append("\">");
			sb.append(karten.get(i).getImage());
			sb.append("</label>\n");
		}
		handler.putPre(idName, list -> handler.putIndex(idName, kartenIndexe(karten, list)));
		return idName;
	}


	private static int[] kartenIndexe(Karten karten, List<String> list) {
		return list.stream().mapToInt(Integer::parseInt).sorted().toArray();
	}


	boolean column = false;


	@Override public void columnsStart() {
		ln();
		sb.append("<table><tr>\n");
		column = false;
	}


	@Override public void nextColumn() {
		if (column) {
			sb.append("</td>\n");
		}
		sb.append("<td>");
		column = true;
	}


	@Override public void columnsEnd() {
		if (column) {
			sb.append("</td>\n");
		}
		sb.append("</tr></table>\n");
	}


	@Override public void mitspieler() {
		ln();
		sb.append("<h2>Mitspieler</h2><p>");
		boolean first = true;
		for (Spieler spieler : client.getInstance().getSpieler()) {
			if (spieler!=client.getPlayer()) {
				if (first) {
					first = false;
				} else {
					sb.append("<br />\n");
				}
				say(spieler.getName());
				say(": ");
				say(spieler.getStatus());
			}
		}
		ln();
	}
	
	
	public void sayVorrat() {
		title("Vorrat");
		Karten karten = vorrat().getKarten(karte -> true);
		for (int i = 0; i<karten.size(); i++) {
			Karte karte = karten.get(i);
			if (i%5==0) {
				ln();
			} else {
				say(", ");
			}
			say(vorrat().getAnzahl(karte));
			say("x ");
			say(karte.getName());
		}
		ln();
	}


	public void kontrolle() {
		getInstance().checkAlleKartenImSpiel(this);
//		sayVorrat();
	}
	
	
	public void footerButtons() {
		ln();
		sb.append("<p id=\"Schluss\">");
		sb.append("<button type=\"submit\" id=\"update\" name=\"update\" accesskey=\"u\">akt<u>u</u>alisieren</button>\n");
		if (!getInstance().isOpenForNewPlayers()) {
			HttpGameClient.button(sb, "opengame", 'ö', "Spiel nochmal für neue Mitspieler öffnen", true);
		}
		sb.append("<button type=\"submit\" id=\"goodbye\" name=\"goodbye\" accesskey=\"v\">Spiel <u>v</u>erlassen</button>\n");
		sb.append("</p>\n");
		sb.append("</form>\n");
	}
	
	
	public void about() {
		ln();
		sb.append(DominionHttpServer.GAME_INFO);
	}


	@Override public void footer() {
		kontrolle();
		mitspieler();
		footerButtons();
		about();
		client.setHasInputField(hasInputField);
	}

}
