package de.eppelt.roland.dominion.task;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Spieler.EmptyDeckException;
import de.eppelt.roland.dominion.ui.TestUI;
import de.tesd.util.KeyNotFoundException;


/** Wähle eins: +3 Karten oder +2 Aktionen */
class AdeligeTest extends TaskTest {


	@Test void testKarten() throws KeyNotFoundException, EmptyDeckException {
		prepareVorratHandkartenAktion(Karte.ADELIGE);
		assertEquals(Adelige.class, spielerA.currentAufgabe().getClass());
		int anzahlHandkarten = spielerA.getHandkarten().size();
		TestUI ui = show(clientA, getExpectedTitle());
		ui.click("+3 Karten");
		assertEquals(anzahlHandkarten+3, spielerA.getHandkarten().size());
		assertKartenKaufen();
	}


	@Test void testAktionen() throws KeyNotFoundException, EmptyDeckException {
		prepareVorratHandkarten(Karte.ADELIGE, Karte.BANDITIN);
		aktion(Karte.ADELIGE);
		assertEquals(Adelige.class, spielerA.currentAufgabe().getClass());
		int aktionen = dran.getAktionen();
		TestUI ui = show(clientA, getExpectedTitle());
		ui.click("+2 Aktionen");
		assertEquals(aktionen+2, dran.getAktionen());
		aktion(Karte.BANDITIN);
	}


}
