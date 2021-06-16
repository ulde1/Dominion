package de.eppelt.roland.dominion.task;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Spieler.EmptyDeckException;
import de.tesd.util.KeyNotFoundException;


class HandlangerTest extends OptionTest {
	
	
	@Test void testZG() throws EmptyDeckException, KeyNotFoundException {
		prepareVorratHandkartenAktion(Karte.HANDLANGER);
		assertEquals(Handlanger.class, spielerA.currentAufgabe().getClass());
		testOptions(this::wähleKarte, this::wähleGeld1);
	}
	
	
	@Test void testAK() throws EmptyDeckException, KeyNotFoundException {
		prepareVorratHandkartenAktion(Karte.HANDLANGER);
		assertEquals(Handlanger.class, spielerA.currentAufgabe().getClass());
		testOptions(this::wähleAktion, this::wähleKauf);
	}
	
	
}
