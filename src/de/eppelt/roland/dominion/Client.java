package de.eppelt.roland.dominion;


import java.net.InetSocketAddress;
import java.util.logging.Logger;

import de.eppelt.roland.dominion.task.Aufgabe;
import de.eppelt.roland.dominion.ui.HtmlUI;
import de.eppelt.roland.game.HttpGameClient;


public class Client extends HttpGameClient<Dominion, Client, Spieler> implements DominionEase {
	
	
	@SuppressWarnings("hiding")
	public final static Logger LOG = Logger.getLogger(Client.class.getName());
	
	
	public Client(InetSocketAddress address, Spieler player) {
		super(address, player);
	}


	@Override protected void appendBody(StringBuffer sb) {
		Spieler player = getPlayer();
		HtmlUI ui = new HtmlUI(this, sb);
		Aufgabe aufgabe;
		do {
			aufgabe = player.currentAufgabe();
			Aufgabe logAufgabe = aufgabe;
			fine(() -> "appendBody: "+player.getName()+": "+logAufgabe.getName());
			aufgabe.setUI(ui);
		} while (!aufgabe.anzeigen());
		ui.footer();
	}
	
	
}