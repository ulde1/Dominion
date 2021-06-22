package de.eppelt.roland.dominion.task;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Karten.KarteNotFoundException;
import de.eppelt.roland.dominion.Spieler.EmptyDeckException;
import de.eppelt.roland.dominion.ui.TestUI;
import de.tesd.util.KeyNotFoundException;



class LakaiTest extends TäterTest {


	@Test void testFriede() throws KeyNotFoundException, EmptyDeckException, KarteNotFoundException {
		prepareVorratHandkartenAktion(Karte.LAKAI);
		setHandkarten(spielerB, Karte.ANWESEN, Karte.ANWESEN, Karte.ANWESEN, Karte.KUPFER);
		assertEquals(Lakai.class, spielerA.currentAufgabe().getClass());
		TestUI ui = show(clientA, getExpectedTitle());
		TestUI oui = show(clientB, getExpectedOpferTitle());
		assertTrue(oui.getButtons().isEmpty(), "Wartend, also kein Button");
		ui.click(Lakai.PLUS_2_GELDMÜNZEN);
		assertEquals(2, dran.getGeld());
		oui = show(clientB, getExpectedOpferTitle());
		oui.click(LakaiOpfer.FRIEDE_BUTTON_TEXT);
	}


	@Test void testOpferGlück() throws KeyNotFoundException, EmptyDeckException, KarteNotFoundException {
		prepareVorratHandkarten(Karte.LAKAI);
		setHandkarten(spielerA, Karte.ANWESEN, Karte.ANWESEN, Karte.ANWESEN, Karte.KUPFER, Karte.LAKAI);
		setHandkarten(spielerB, Karte.ANWESEN, Karte.ANWESEN, Karte.ANWESEN, Karte.KUPFER);
		aktion(Karte.LAKAI);
		assertEquals(Lakai.class, spielerA.currentAufgabe().getClass());
		TestUI ui = show(clientA, getExpectedTitle());
		ui.click(Lakai.HANDKARTEN_ABLEGEN_UND_4_NEUE_KARTEN_NACHZIEHEN);
		assertEquals(4, spielerA.getHandkarten().size(), "Genau 4 Handkarten nachgezogen");
		assertFalse(spielerA.getHandkarten().enthält(Karte.ANWESEN), "Kein Anwesen nachgezogen");
		TestUI oui = show(clientB, getExpectedOpferTitle());
		oui.click(LakaiOpfer.GLÜCK_BUTTON_TEXT);
	}


	@Test void testOpferHand5() throws KeyNotFoundException, EmptyDeckException, KarteNotFoundException {
		prepareVorratHandkarten(Karte.LAKAI);
		setHandkarten(spielerA, Karte.KUPFER, Karte.KUPFER, Karte.KUPFER, Karte.KUPFER, Karte.KUPFER, Karte.LAKAI);
		setHandkarten(spielerB, Karte.ANWESEN, Karte.ANWESEN, Karte.ANWESEN, Karte.KUPFER, Karte.KUPFER);
		aktion(Karte.LAKAI);
		assertEquals(Lakai.class, spielerA.currentAufgabe().getClass());
		TestUI ui = show(clientA, getExpectedTitle());
		ui.click(Lakai.HANDKARTEN_ABLEGEN_UND_4_NEUE_KARTEN_NACHZIEHEN);
		assertEquals(4, spielerA.getHandkarten().size(), "Genau 4 Handkarten nachgezogen");
		assertTrue(spielerA.getHandkarten().enthält(Karte.ANWESEN), "Muss Anwesen nachgezogen haben");
		TestUI oui = show(clientB, getExpectedOpferTitle());
		oui.click(LakaiOpfer.PECH_BUTTON_TEXT);
	}


}
