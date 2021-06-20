package de.eppelt.roland.dominion.task;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Spieler.EmptyDeckException;
import de.eppelt.roland.dominion.ui.TestUI;
import de.tesd.util.KeyNotFoundException;



/** Beliebig viele Handkarten ablegen und nachziehen; +1 Aktion */
class KellerTest extends TaskTest {


	@Test void test() throws KeyNotFoundException, EmptyDeckException {
		prepareVorratHandkartenAktion(Karte.KELLER);
		assertEquals(Keller.class, spielerA.currentAufgabe().getClass());
		int anzahlHandkarten = spielerA.getHandkarten().size();
		TestUI ui = show(clientA, getExpectedTitle());
		ui.select(spielerA.getHandkarten(), 0, 1, 2);
		ui.click("Ablegen");
		assertEquals(anzahlHandkarten, spielerA.getHandkarten().size(), "Anzahl Handkarten");
		assertKartenKaufen();
	}
	

}
