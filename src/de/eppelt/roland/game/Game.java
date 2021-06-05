package de.eppelt.roland.game;


import java.util.stream.Stream;


public interface Game<INSTANCE extends GameInstance<INSTANCE, CLIENT, PLAYER>, CLIENT extends GameClient<INSTANCE, CLIENT, PLAYER>, PLAYER extends GamePlayer<INSTANCE, CLIENT, PLAYER>> {


	void put(String session, CLIENT client);


	Stream<? extends GameClient<INSTANCE, CLIENT, PLAYER>> stream();


}
