package de.eppelt.roland.dominion.ui;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Logger;

import de.eppelt.roland.dominion.Client;
import de.eppelt.roland.dominion.ClientEase;
import de.tesd.collection.HashList;
import de.tesd.collection.HashMap;
import de.tesd.util.Loggers;
import de.tesd.util.Password;
import de.tesd.util.Ts;


public class HtmlHandler implements Handler, ClientEase, Loggers {


	public final static Logger LOG = Logger.getLogger(HtmlUI.class.getName());


	@Override public Logger logger() {
		return LOG;
	}


	protected Client client;
	private String token = "";
	@SuppressWarnings("unchecked") HashMap<String, Consumer<List<String>>>[] consumers = new HashMap[] {
		new HashMap<>(),
		new HashMap<>() };
	HashMap<String, Consumer<List<String>>> consumer = consumers[1];
	HashMap<String, int[]> index = new HashMap<>();


	public HtmlHandler(Client client) {
		this.client = client;
		nextToken();
	}


	public void nextToken() {
		token = Password.generateNewPassword(11);
	}


	public boolean isValidToken(HashList<String, String> map) {
		return token.equals(map.getFirstOrNull("token"));
	}


	public String getToken() {
		return token;
	}


	public void putPre(String name, Consumer<List<String>> consumer) {
		consumers[0].put(name, consumer);
	}


	public void put(String name, Consumer<List<String>> consumer) {
		consumers[1].put(name, consumer);
	}


	public void putIndex(String idName, int[] kartenIndexe) {
		index.put(idName, kartenIndexe);
	}


	@Override public int[] getIndex(String name) {
		int[] indexes = index.getOrDefault(name, HtmlUI.EMPTY);
		Arrays.sort(indexes);
		Ts.reverse(indexes);
		return indexes;
	}


	@Override public Client getClient() {
		return client;
	}


	public void setClient(Client client) {
		this.client = client;
	}


	@Override public void handle(HashList<String, String> map, Client client) throws IOException {
		String aufgabeName = getSpieler().currentAufgabe().getName();
		fine(() -> aufgabeName+" handle");
		if (isValidToken(map)) {
			fine(() -> aufgabeName+" Token ok");
			for (int i = 0; i<consumers.length; i++) {
				for (Map.Entry<String, Consumer<List<String>>> entry : consumers[i].entrySet()) {
					if (map.containsKey(entry.getKey())) {
						List<String> list = map.getOrEmpty(entry.getKey());
						int li = i;
						finer(() -> aufgabeName+" map"+li+" "+entry.getKey()+" -> "+list);
						entry.getValue().accept(list);
					}
				}
			}
		}
	}


	@Override public void updateOtherPlayers() {
		getInstance().updatePlayersExcept(getClient());
	}

}