package de.eppelt.roland.game;



public interface GameClient<INSTANCE extends GameInstance<INSTANCE, CLIENT, PLAYER>, CLIENT extends GameClient<INSTANCE, CLIENT, PLAYER>, PLAYER extends GamePlayer<INSTANCE, CLIENT, PLAYER>> {

	
	PLAYER getPlayer();
	
	
	default INSTANCE getInstance() {
		return getPlayer().getInstance();
	}


	default Game<INSTANCE, CLIENT, PLAYER> getGame() {
		return getPlayer().getGame();
	}


}
