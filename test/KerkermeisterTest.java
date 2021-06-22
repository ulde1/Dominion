import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Karten.KarteNotFoundException;
import de.eppelt.roland.dominion.Spieler.EmptyDeckException;
import de.eppelt.roland.dominion.task.Kerkermeister;
import de.eppelt.roland.dominion.task.TäterTest;
import de.eppelt.roland.dominion.ui.TestUI;
import de.tesd.util.KeyNotFoundException;



class KerkermeisterTest extends TäterTest {


	@Test void testHand0() throws KeyNotFoundException, EmptyDeckException, KarteNotFoundException {
		prepareVorratHandkarten(Karte.KERKERMEISTER);
		setHandkarten(spielerB);
		aktion(Karte.KERKERMEISTER);
		assertEquals(Kerkermeister.class, spielerA.currentAufgabe().getClass());
		TestUI ui = show(clientB, getExpectedOpferTitle());
		ui.click("0 Karten ablegen");
	}


	@Test void testHand1() throws KeyNotFoundException, EmptyDeckException, KarteNotFoundException {
		prepareVorratHandkarten(Karte.KERKERMEISTER);
		setHandkarten(spielerB, Karte.KUPFER);
		aktion(Karte.KERKERMEISTER);
		assertEquals(Kerkermeister.class, spielerA.currentAufgabe().getClass());
		TestUI ui = show(clientB, getExpectedOpferTitle());
		ui.click("1 Karte ablegen");
	}


}
