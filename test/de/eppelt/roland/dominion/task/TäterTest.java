package de.eppelt.roland.dominion.task;

import java.net.InetSocketAddress;

import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.BeforeEach;

import de.eppelt.roland.dominion.Client;
import de.eppelt.roland.dominion.Spieler;
import de.eppelt.roland.dominion.ui.TestUI;

public class TäterTest extends TaskTest {
	
	
	protected Spieler spielerB;
	protected Client clientB;
	
	
	private @Nullable String expectedOpferTitle;


	@SuppressWarnings("null")
	public TäterTest() {
		super();
	}

	
	@BeforeEach @Override void setUp() throws Exception {
		super.setUp();
		spielerB = new Spieler(dominion, "B");
		clientB = new Client(new InetSocketAddress(0), spielerB);
		clientB.setUISupplier(sb -> new TestUI(clientB, sb));
	}
	

	public String getExpectedOpferTitle() {
		return expectedOpferTitle!=null ? expectedOpferTitle : getExpectedTitle()+"-Opfer";
	}


	public void setExpectedOpferTitle(String expectedOpferTitle) {
		this.expectedOpferTitle = expectedOpferTitle;
	}
	
}
