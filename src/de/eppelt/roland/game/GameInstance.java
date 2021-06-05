package de.eppelt.roland.game;

import org.eclipse.jdt.annotation.Nullable;

public interface GameInstance<INSTANCE extends GameInstance<INSTANCE, CLIENT, PLAYER>, CLIENT extends GameClient<INSTANCE, CLIENT, PLAYER>, PLAYER extends GamePlayer<INSTANCE, CLIENT, PLAYER>> {
	
	
	Game<INSTANCE, CLIENT, PLAYER> getGame();


	@Nullable PLAYER getActive();


}
