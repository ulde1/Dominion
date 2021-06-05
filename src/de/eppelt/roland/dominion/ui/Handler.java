package de.eppelt.roland.dominion.ui;


import java.io.IOException;

import de.eppelt.roland.dominion.Client;
import de.eppelt.roland.dominion.ClientEase;
import de.tesd.collection.HashList;


public interface Handler extends ClientEase {


	void handle(HashList<String, String> map, Client client) throws IOException;


	/** @return die gewählten Karten-Indizes in absteigender Reihenfolge */
	int[] getIndex(String indexKey);


	void updateOtherPlayers();

	
}