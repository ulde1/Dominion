package de.eppelt.roland.game;


import java.io.IOException;

import de.tesd.collection.HashList;
import de.tesd.net.Http;


@FunctionalInterface
public interface HttpGameClientHandler<INSTANCE extends HttpGameInstance<INSTANCE, CLIENT, PLAYER>, CLIENT extends HttpGameClient<INSTANCE, CLIENT, PLAYER>, PLAYER extends GamePlayer<INSTANCE, CLIENT, PLAYER>>  {


	void handle(HashList<String, String> map, Http http, CLIENT client) throws IOException;


}