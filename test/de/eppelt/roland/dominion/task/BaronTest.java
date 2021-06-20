package de.eppelt.roland.dominion.task;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Spieler.EmptyDeckException;
import de.eppelt.roland.dominion.ui.TestUI;
import de.tesd.util.KeyNotFoundException;


/** +1 Kauf; Du darfst ein Anwesen ablegen. Wenn du das machst: +4. Ansonsten: Nimm dir ein Anwesen. */
class BaronTest extends TaskTest {


	@Test void testMitAnwesenAnnehmen() throws KeyNotFoundException, EmptyDeckException {
		prepareVorratHandkarten(Karte.BARON, Karte.ANWESEN);
		aktion(Karte.BARON);
		assertEquals(Baron.class, spielerA.currentAufgabe().getClass());
		int anzahlHandkarten = spielerA.getHandkarten().size();
		int geld = spielerA.getHandkartenGeld();
		TestUI ui = show(clientA, getExpectedTitle());
		ui.click("Ablegen und 4 Münzen bekommen");
		assertEquals(anzahlHandkarten-1, spielerA.getHandkarten().size(), "Anzahl Handkarten");
		assertEquals(geld, spielerA.getHandkartenGeld());
		assertEquals(4, dran.getGeld());
		assertEquals(2, spielerA.getSeite().size(), "Seite-Größe");
		assertKartenKaufen();
	}


	@Test void testMitAnwesenAblehnen() throws KeyNotFoundException, EmptyDeckException {
		prepareVorratHandkarten(Karte.BARON, Karte.ANWESEN);
		aktion(Karte.BARON);
		assertEquals(Baron.class, spielerA.currentAufgabe().getClass());
		int anzahlHandkarten = spielerA.getHandkarten().size();
		int geld = spielerA.getHandkartenGeld();
		TestUI ui = show(clientA, getExpectedTitle());
		ui.click("Ein Anwesen nehmen");
		assertEquals(anzahlHandkarten, spielerA.getHandkarten().size(), "Anzahl Handkarten");
		assertEquals(geld, spielerA.getHandkartenGeld(), "Handkarten-Geld");
		assertEquals(0, dran.getGeld(), "Dran-Geld");
		assertEquals(Karte.ANWESEN, spielerA.getSeite().get(0), "Seite");
		assertEquals(2, spielerA.getSeite().size(), "Seite-Größe");
		assertKartenKaufen();
	}


	@Test void testOhneAnwesen() throws KeyNotFoundException, EmptyDeckException {
		prepareVorratHandkarten(Karte.BARON);
		while (spielerA.getHandkarten().enthält(Karte.ANWESEN)) {
			spielerA.getHandkarten().entferne(Karte.ANWESEN);
			// zwecks Test auf den Müll
			dominion.getTrash().legeAb(Karte.ANWESEN);
		}
		aktion(Karte.BARON);
		assertEquals(Baron.class, spielerA.currentAufgabe().getClass());
		int anzahlHandkarten = spielerA.getHandkarten().size();
		int geld = spielerA.getHandkartenGeld();
		TestUI ui = show(clientA, getExpectedTitle());
		ui.click("Ein Anwesen nehmen");
		assertEquals(anzahlHandkarten, spielerA.getHandkarten().size(), "Anzahl Handkarten");
		assertEquals(geld, spielerA.getHandkartenGeld(), "Handkarten-Geld");
		assertEquals(0, dran.getGeld(), "Dran-Geld");
		assertEquals(Karte.ANWESEN, spielerA.getSeite().get(0), "Seite");
		assertEquals(2, spielerA.getSeite().size(), "Seite-Größe");
		assertKartenKaufen();
	}


}
