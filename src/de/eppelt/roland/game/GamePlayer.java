package de.eppelt.roland.game;



public interface GamePlayer<INSTANCE extends GameInstance<INSTANCE, CLIENT, PLAYER>, CLIENT extends GameClient<INSTANCE, CLIENT, PLAYER>, PLAYER extends GamePlayer<INSTANCE, CLIENT, PLAYER>> {

	
	String getName();


	INSTANCE getInstance();
	
	
	default Game<INSTANCE, CLIENT, PLAYER> getGame() {
		return getInstance().getGame();
	}
	
	
}
