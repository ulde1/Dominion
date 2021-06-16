package de.eppelt.roland.dominion.task;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Spieler.EmptyDeckException;
import de.eppelt.roland.dominion.ui.TestUI;
import de.tesd.util.KeyNotFoundException;



/** Decke eine Handkarte auf. Für jeden Kartentyp (Aktion, Angriff ...), den sie hat, wähle eine andere Option: +1
 * Aktion / +1 Kauf / +$3 oder nimm ein Gold vom Vorrat. */
class HöflingeTest extends OptionTest {


	@Test void testGold() throws EmptyDeckException, KeyNotFoundException {
		prepareVorratHandkartenAktion(Karte.HÖFLINGE);
		assertEquals(Höflinge.class, spielerA.currentAufgabe().getClass());
		TestUI ui = show(client, getExpectedTitle());
		ui.click(spielerA.getHandkarten().get(0));
		testOptions(this::wähleGold);
	}


	@Test void testKaufGeld3() throws EmptyDeckException, KeyNotFoundException {
		prepareVorratHandkarten(Karte.HÖFLINGE, Karte.DIPLOMATIN);
		aktion(Karte.HÖFLINGE);
		assertEquals(Höflinge.class, spielerA.currentAufgabe().getClass());
		TestUI ui = show(client, getExpectedTitle());
		ui.click(Karte.DIPLOMATIN);
		testOptions(this::wähleKauf, this::wähleGeld3);
	}

}
