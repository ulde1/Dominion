package de.eppelt.roland.dominion.ui;


import java.io.IOException;

import de.eppelt.roland.dominion.Client;
import de.tesd.collection.HashList;
import de.tesd.collection.HashMap;


public class TestHandler implements Handler {


	HashMap<String, int[]> index = new HashMap<>();
	private Client client;


	public TestHandler(Client client) {
		this.client = client;
	}


	@Override public Client getClient() {
		return client;
	}


	@Override public void handle(HashList<String, String> map, Client client) throws IOException {
	}


	@Override public int[] getIndex(String indexKey) {
		return index.getOrDefault(indexKey, HtmlUI.EMPTY);
	}


	public void putIndex(String idName, int[] kartenIndexe) {
		index.put(idName, kartenIndexe);
	}


	@Override public void updateOtherPlayers() {
	}
	

}
