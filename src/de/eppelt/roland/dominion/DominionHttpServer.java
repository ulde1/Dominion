package de.eppelt.roland.dominion;


import java.io.IOException;
import java.util.logging.Logger;

import de.eppelt.roland.dominion.task.Aufgabe;
import de.eppelt.roland.dominion.ui.UI;
import de.eppelt.roland.game.HttpGame;
import de.eppelt.roland.game.HttpGameClientHandler;
import de.tesd.util.DailyLog;
import de.tesd.util.Loggers;


public class DominionHttpServer implements Loggers {


	public static final String VERSION = "Dominion 0.6.6";
	public final static Logger LOG = Logger.getLogger(DominionHttpServer.class.getName());
	@Override public Logger logger() { return LOG; }


	public static final String GAME_INFO = "<h2>"+VERSION+"</h2>\n"
		+ " © 2021 Roland M. Eppelt, <a href=\"https://ulde.de\" target=\"empty\">ULDE – Die Ulmer Datenbank-Experten</a><br/>\n"
		+ "<ul>\n"
		+ "<li><a href=\"https://github.com/ulde1/dominion\" target=\"empty\">Quelltext</a><br/>\n"
		+ "<li><a href=\"https://github.com/ulde1/Dominion/releases/latest\" target=\"empty\">Versionshinweise+Download</a><br/>\n"
		+ "<li><a href=\"http://wiki.dominionstrategy.com/index.php/Main_Page\" target= \"_blank\">Dominion Strategy Wiki</a></li>\n"
		+ "<li><a href=\"https://shardofhonor.github.io/dominion-card-generator/index.html\" target= \"_blank\">Dominion Card Generator</a></li>\n"
		+ "</ul>";
	private static final String CSS_LINK = "p/dominion.css";
	public static final String SOUND_PATH = "p/dmn/";

	
	public static HttpGameClientHandler<Dominion, Client, Spieler> dominionHandler = (map, http, client) -> {
		Dominion instance = client.getInstance();
		if (map.containsKey("update")) {
			http.sendResponse(client.gameHtml());
		} else if (map.containsKey("goodbye")) {
			client.close();
			instance.removeSpieler(client.getPlayer());
			http.sendResponse(instance.getGame().getLoginHtml());
			instance.updatePlayersExcept(client);
		} else if (map.containsKey("opengame")) {
			instance.openForNewPlayers();
		} else {
			Aufgabe aufgabe = client.getPlayer().currentAufgabe();
			UI ui = aufgabe.getUI();
			if (ui!=null) {
				ui.getHandler().handle(map, client);
			}
			instance.sendUpdatesNow();
		}
	};
	

		// ========== DominionHttpsServer ==========


	public static void main(String[] args) throws IOException {
		LOG.config(VERSION);
		new DailyLog().start();
		HttpGame<Dominion, Client, Spieler> game = new HttpGame<Dominion, Client, Spieler>("Dominion", Integer.parseInt(args.length>0 ? args[0] : "80"), 
			Dominion::new, Spieler::new, 
			Client::new, dominionHandler);
		game.addCssLink(CSS_LINK);
		game.addInfoHtml(GAME_INFO);
		game.start();
	}

}
