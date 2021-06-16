package de.eppelt.roland.dominion.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.ui.TestUI;
import de.tesd.util.KeyNotFoundException;

public class OptionTest extends TaskTest {


	private int anzahlHandkarten;
	private int aktionen;
	private int käufe;
	private int geld;
	
	
	public void testOptions(Runnable... tests) {
		anzahlHandkarten = spielerA.getHandkarten().size();
		aktionen = dran.getAktionen();
		käufe = dran.getKäufe();
		geld = dran.getGeld();
		for (Runnable test : tests) {
			test.run();
		}
		assertKartenKaufen();
	}
	

	public void wähleKarte() {
		TestUI ui = show(client, getExpectedTitle());
		try {
			ui.click(OptionAufgabe.Option.KARTE.text);
		} catch (KeyNotFoundException e) {
			fail(e);
		}
		assertEquals(anzahlHandkarten+1, spielerA.getHandkarten().size());
	}

	
	public void wähleAktion() {
		TestUI ui = show(client, getExpectedTitle());
		try {
			ui.click(OptionAufgabe.Option.AKTION.text);
		} catch (KeyNotFoundException e) {
			fail(e);
		}
		assertEquals(aktionen+1, dran.getAktionen());
	}

	
	public void wähleKauf() {
		TestUI ui = show(client, getExpectedTitle());
		try {
			ui.click(OptionAufgabe.Option.KAUF.text);
		} catch (KeyNotFoundException e) {
			fail(e);
		}
		assertEquals(käufe+1, dran.getKäufe());
	}

	
	public void wähleGeld1()  {
		TestUI ui = show(client, getExpectedTitle());
		try {
			ui.click(OptionAufgabe.Option.GELD1.text);
		} catch (KeyNotFoundException e) {
			fail(e);
		}
		assertEquals(geld+1, dran.getGeld());
	}

	
	public void wähleGeld3()  {
		TestUI ui = show(client, getExpectedTitle());
		try {
			ui.click(OptionAufgabe.Option.GELD3.text);
		} catch (KeyNotFoundException e) {
			fail(e);
		}
		assertEquals(geld+3, dran.getGeld());
	}


	public void wähleGold()  {
		TestUI ui = show(client, getExpectedTitle());
		try {
			ui.click(OptionAufgabe.Option.GOLD.text);
		} catch (KeyNotFoundException e) {
			fail(e);
		}
		assertEquals(spielerA.getSeite().get(0), Karte.GOLD);
	}


}
