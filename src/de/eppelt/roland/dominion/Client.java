package de.eppelt.roland.dominion;


import java.net.InetSocketAddress;
import java.util.function.Function;
import java.util.logging.Logger;

import de.eppelt.roland.dominion.task.Aufgabe;
import de.eppelt.roland.dominion.ui.HtmlUI;
import de.eppelt.roland.dominion.ui.UI;
import de.eppelt.roland.game.HttpGameClient;


public class Client extends HttpGameClient<Dominion, Client, Spieler> implements DominionEase {
	
	
	@SuppressWarnings("hiding")
	public final static Logger LOG = Logger.getLogger(Client.class.getName());
	private Function<StringBuffer, UI>  uiSupplier = sb -> new HtmlUI(this, sb);
	
	
	public Client(InetSocketAddress address, Spieler player) {
		super(address, player);
	}
	

	public void setUISupplier(Function<StringBuffer, UI> uiSupplier) {
		this.uiSupplier = uiSupplier;
	}
	
	
	public void updateNow() {
		super.sendHtml();
	}
	
	
	/** Sendet KEIN Update, sondern merkt isch nur den Update-Wunsch. Gesendet wird erst mit {@link #updateNow()} */
	@Override public int sendHtml() {
		getInstance().needsUpdate(getPlayer());
		return 0;
	}
	
	
	@Override protected void appendBody(StringBuffer sb) {
		Spieler player = getPlayer();
		UI ui = uiSupplier.apply(sb);
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