package de.eppelt.roland.dominion.task;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.InetSocketAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;

import de.eppelt.roland.dominion.Client;
import de.eppelt.roland.dominion.Dominion;
import de.eppelt.roland.dominion.DominionHttpServer;
import de.eppelt.roland.dominion.Dran;
import de.eppelt.roland.dominion.Edition;
import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Spieler;
import de.eppelt.roland.dominion.Spieler.EmptyDeckException;
import de.eppelt.roland.dominion.ui.TestUI;
import de.eppelt.roland.game.HttpGame;
import de.tesd.collection.HashList;
import de.tesd.util.KeyNotFoundException;

public class TaskTest {


	public static final Pattern TITLE_PATTERN = Pattern.compile(".*<h2>([^<]*)</h2>.*", Pattern.DOTALL);
	
	
	protected HttpGame<Dominion, Client, Spieler> game;
	protected Dominion dominion;
	protected Spieler spielerA;
	protected Client client;
	protected Dran dran;
	
	
	private String expectedTitle;


	@SuppressWarnings("null")
	public TaskTest() {
		expectedTitle = this.getClass().getSimpleName().replaceAll("Test$", "");
	}
	

	public String getExpectedTitle() {
		return expectedTitle;
	}

	
	public void setExpectedTitle(String expectedTitle) {
		this.expectedTitle = expectedTitle;
	}


	@BeforeEach void setUp() throws Exception {
		game = new HttpGame<Dominion, Client, Spieler>("Dominion", -1, 
			Dominion::new, Spieler::new, 
			Client::new, DominionHttpServer.dominionHandler);
		dominion = new Dominion(game);
		spielerA = new Spieler(dominion, "A");
		client = new Client(new InetSocketAddress(0), spielerA);
		client.setUISupplier(sb -> new TestUI(client, sb));
	}


	public void prepareVorratHandkartenAktion(Karte karte) throws KeyNotFoundException, EmptyDeckException {
		prepareVorratHandkarten(karte);
		aktion(karte);
	}


	public void prepareVorratHandkarten(Karte... karten) throws KeyNotFoundException, EmptyDeckException {
		TestUI ui = show(client, "Vorrat auswählen");
		ui.click("selber zusammenstellen");
		ui = show(client, "Vorrat auswählen");
		ui.click("Bearbeiten");
		ui = show(client, "Vorrat auswählen");
		HashList<Edition, Karte> editionsKarten = new HashList<>();
		for (Karte karte : karten) {
			if (!Edition.BASIS.getKartenset().getKarten().enthält(karte)) {
				editionsKarten.add(Edition.of(karte), karte);
			}
		}
		for (Edition edition : editionsKarten.keySet()) {
			ui.select(edition.getKartenset().getKarten(), editionsKarten.getOrEmpty(edition).toArray(Karte[]::new));
		}
		ui.click("Fertig");
		ui = show(client, "Vorrat auswählen");
		ui.click("Dieses Kartenset auswählen");
		zieheHandkarten(karten);
	}
	
	
	@SuppressWarnings("null")
	public void zieheHandkarten(Karte[] karten) throws EmptyDeckException {
		for (Karte karte : karten) {
			spielerA.getHandkarten().legeAb(dominion.getVorrat().zieheKarte(karte));
		}
		dran = dominion.getDran();
		assertNotNull(dran);
	}


	public void aktion(Karte karte) throws KeyNotFoundException {
		TestUI ui = show(client, "Aktion(en) ausführen");
		ui.click(karte);
	}


	public TestUI show(Client client, String expectedTitle) {
		String html = client.gameHtml();
		Matcher matcher = TITLE_PATTERN.matcher(html);
		if (matcher.matches()) {
			assertEquals(expectedTitle, matcher.group(1));
		} else {
			fail("Kein Title im html: "+html);
		}
		return (TestUI) client.getPlayer().currentAufgabe().getUI();		
	}
	
	
	public void assertKartenKaufen() {
		show(client, "Karten kaufen");
		assertEquals(KartenKaufen.class, spielerA.currentAufgabe().getClass());
		KartenKaufen kaufen = (KartenKaufen) spielerA.currentAufgabe();
		assertEquals(dran.getKäufe(), kaufen.käufe, "Käufe");
		assertEquals(spielerA.getHandkartenGeld()+dran.getGeld(), kaufen.geld, "Geld");
	}


}
