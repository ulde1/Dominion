package de.eppelt.roland.dominion.task;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Spieler.EmptyDeckException;
import de.eppelt.roland.dominion.ui.TestUI;
import de.tesd.util.KeyNotFoundException;


/** +1 Karte, +1 Aktion; Du darfst 2 Handkarten ablegen. Wenn du das tust: +$2. 1 Siegpunkt. */
class MühleTest extends TaskTest {


	@Test void testJa() throws KeyNotFoundException, EmptyDeckException {
		prepareVorratHandkarten(Karte.MÜHLE);
		int anzahlHandkarten = spielerA.getHandkarten().size();
		int geld = spielerA.getHandkartenGeld();
		aktion(Karte.MÜHLE);
		assertEquals(Mühle.class, spielerA.currentAufgabe().getClass());
		TestUI ui = show(client, getExpectedTitle());
		ui.select(spielerA.getHandkarten(), 0, 3);
		ui.click("Ablegen");
		assertEquals(anzahlHandkarten-2, spielerA.getHandkarten().size(), "Anzahl Handkarten");
		assertEquals(1, dran.getAktionen(), "Anzahl Aktionen");
		assertEquals(geld, spielerA.getHandkartenGeld(), "Handkarten-Geld");
		assertEquals(2, dran.getGeld(), "Dran-Geld");
		assertKartenKaufen();
	}


	@Test void testNein() throws KeyNotFoundException, EmptyDeckException {
		prepareVorratHandkarten(Karte.MÜHLE);
		int anzahlHandkarten = spielerA.getHandkarten().size();
		aktion(Karte.MÜHLE);
		assertEquals(Mühle.class, spielerA.currentAufgabe().getClass());
		TestUI ui = show(client, getExpectedTitle());
		ui.selectNone(spielerA.getHandkarten());
		ui.click("Ablegen");
		assertEquals(anzahlHandkarten, spielerA.getHandkarten().size(), "Anzahl Handkarten");
		assertEquals(1, dran.getAktionen(), "Anzahl Aktionen");
		assertEquals(0, dran.getGeld(), "Dran-Geld");
	}


}
