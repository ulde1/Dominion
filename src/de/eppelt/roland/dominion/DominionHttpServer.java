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


	public final static Logger LOG = Logger.getLogger(DominionHttpServer.class.getName());
	@Override public Logger logger() { return LOG; }


	private static final String GAME_INFO = "<h2>Info</h2>\n"
		+ "<ul>\n"
		+ "<li><a href=\"http://wiki.dominionstrategy.com/index.php/Main_Page\" target= \"_blank\">Dominion Strategy Wiki</a></li>\n"
		+ "</ul>";
	private static final String CSS_LINK = "p/dominion.css";
	public static final String SOUND_PATH = "p/dmn/";

	
	static HttpGameClientHandler<Dominion, Client, Spieler> dominionHandler = (map, http, client) -> {
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
		}
	};
	

		// ========== DominionHttpsServer ==========


	public static void main(String[] args) throws IOException {
		LOG.config("Dominion 0.6");
		new DailyLog().start();
		HttpGame<Dominion, Client, Spieler> game = new HttpGame<Dominion, Client, Spieler>("Dominion", Integer.parseInt(args[0]), 
			Dominion::new, Spieler::new, 
			Client::new, dominionHandler);
		game.addCssLink(CSS_LINK);
		game.addInfoHtml(GAME_INFO);
		game.start();
	}

}
