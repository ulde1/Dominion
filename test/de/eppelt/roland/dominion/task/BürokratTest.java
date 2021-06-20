package de.eppelt.roland.dominion.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.ui.TestUI;
import de.tesd.util.function.ThrowingRunnable;

class BürokratTest extends TäterTest {
	
	
	@BeforeEach @Override void setUp() throws Exception {
		super.setUp();
		setExpectedOpferTitle(BürokratOpfer.BÜROKRATEN_OPFER);
	}
	
	
	private void test(ThrowingRunnable<Exception> prepare, ThrowingRunnable<Exception> after) throws Exception {
		prepareVorratHandkartenAktion(Karte.BÜROKRAT);
		prepare.run();
		assertEquals(Bürokrat.class, spielerA.currentAufgabe().getClass());
		TestUI ui = show(clientA, getExpectedTitle());
		ui.click(Karte.SILBER);
		after.run();
	}
	

	@Test void testSpieler() throws Exception {
		int[] anzahl_Handkarten_Nachziehstapel = new int[] {0, 0};
		test(
			() -> {
				anzahl_Handkarten_Nachziehstapel[0] = spielerA.getHandkarten().size();
				anzahl_Handkarten_Nachziehstapel[1] = spielerA.getNachziehStapel().size();
			},
			() -> {
				assertEquals(Karte.SILBER, spielerA.getNachziehStapel().get(0), "Nachziehstapel Top");
				assertEquals(anzahl_Handkarten_Nachziehstapel[0], spielerA.getHandkarten().size(), "Anzahl Handkarten");
				assertEquals(anzahl_Handkarten_Nachziehstapel[1]+1, spielerA.getNachziehStapel().size(), "Anzahl Nachziehstapel");
			}
		);
	}
	
	
	@Test void testOpferGlück() throws Exception {
		int[] anzahl_Nachziehstapel = new int[] {0};
		test(
			() -> {
				setHandkarten(spielerB, Karte.KUPFER, Karte.KUPFER, Karte.KUPFER, Karte.KUPFER, Karte.KUPFER);
				setTopNachziehstapel(spielerB, Karte.KUPFER, Karte.KUPFER);
				anzahl_Nachziehstapel[0] = spielerB.getNachziehStapel().size();
			},
			() -> {
				TestUI ui = show(clientB, getExpectedOpferTitle());
				ui.click(BürokratOpfer.OK);
				assertEquals(anzahl_Nachziehstapel[0], spielerB.getNachziehStapel().size(), "Anzahl Nachziehstapel");
			}
		);
	}


	@Test void testOpferPech1() throws Exception {
		int[] anzahl_Nachziehstapel = new int[] {0};
		test(
			() -> {
				setHandkarten(spielerB, Karte.ANWESEN, Karte.ANWESEN, Karte.KUPFER, Karte.KUPFER, Karte.KUPFER);
				setTopNachziehstapel(spielerB, Karte.KUPFER, Karte.ANWESEN);
				anzahl_Nachziehstapel[0] = spielerB.getNachziehStapel().size();
			},
			() -> {
				TestUI ui = show(clientB, getExpectedOpferTitle());
				ui.click(Karte.ANWESEN);
				assertEquals(Karte.ANWESEN, spielerB.getNachziehStapel().get(0), "Nachziehstapel Top");
				assertEquals(anzahl_Nachziehstapel[0]+1, spielerB.getNachziehStapel().size(), "Anzahl Nachziehstapel");
			}
		);
	}


}
