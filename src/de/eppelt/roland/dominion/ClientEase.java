package de.eppelt.roland.dominion;

public interface ClientEase extends SpielerEase {


	Client getClient();


	@Override default Spieler getSpieler() {
		return getClient().getPlayer();
	}


	@Override default public Dominion getInstance() {
		return getClient().getInstance();
	}


}
