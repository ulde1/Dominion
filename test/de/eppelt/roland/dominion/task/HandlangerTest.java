package de.eppelt.roland.dominion.task;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Spieler.EmptyDeckException;
import de.eppelt.roland.dominion.ui.TestUI;
import de.tesd.util.KeyNotFoundException;


class HandlangerTest extends TaskTest {
	
	
	private int anzahlHandkarten;
	private int aktionen;
	private int käufe;
	private int geld;


	@Test void testZG() throws EmptyDeckException, KeyNotFoundException {
		test(this::wähleKarte, this::wähleGeld);
	}
	
	
	@Test void testAK() throws EmptyDeckException, KeyNotFoundException {
		test(this::wähleAktion, this::wähleKauf);
	}
	
	
	private void test(Runnable test1, Runnable test2) throws KeyNotFoundException, EmptyDeckException {
		starteMitAktion(Karte.HANDLANGER);
		assertEquals(Handlanger.class, spielerA.currentAufgabe().getClass());
		anzahlHandkarten = spielerA.getHandkarten().size();
		aktionen = dran.getAktionen();
		käufe = dran.getKäufe();
		geld = dran.getGeld();
		test1.run();
		test2.run();
			// Karten kaufen
		show(client, "Karten kaufen");
		assertEquals(KartenKaufen.class, spielerA.currentAufgabe().getClass());
		KartenKaufen kaufen = (KartenKaufen) spielerA.currentAufgabe();
		assertEquals(spielerA.geld()+dran.getGeld(), kaufen.geld, "Geld");
	}
	

	private void wähleKarte() {
		TestUI ui = show(client, "Handlanger");
		try {
			ui.click(OptionAufgabe.Option.KARTE.text);
		} catch (KeyNotFoundException e) {
			fail(e);
		}
		assertEquals(anzahlHandkarten+1, spielerA.getHandkarten().size());
	}

	
	private void wähleAktion() {
		TestUI ui = show(client, "Handlanger");
		try {
			ui.click(OptionAufgabe.Option.AKTION.text);
		} catch (KeyNotFoundException e) {
			fail(e);
		}
		assertEquals(aktionen+1, dran.getAktionen());
	}

	
	private void wähleKauf() {
		TestUI ui = show(client, "Handlanger");
		try {
			ui.click(OptionAufgabe.Option.KAUF.text);
		} catch (KeyNotFoundException e) {
			fail(e);
		}
		assertEquals(käufe+1, dran.getKäufe());
	}

	
	private void wähleGeld()  {
		TestUI ui = show(client, "Handlanger");
		try {
			ui.click(OptionAufgabe.Option.GELD1.text);
		} catch (KeyNotFoundException e) {
			fail(e);
		}
		assertEquals(geld+1, dran.getGeld());
	}

	
}
