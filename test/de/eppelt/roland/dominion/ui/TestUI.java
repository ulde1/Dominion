package de.eppelt.roland.dominion.ui;


import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.Nullable;

import de.eppelt.roland.dominion.Client;
import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Karten;
import de.tesd.collection.HashMap;
import de.tesd.util.KeyNotFoundException;
import de.tesd.util.Password;


public class TestUI extends UIStub {


	private HashMap<Object, Consumer<Handler>> buttons = new HashMap<>();
	private HashMap<Karten, String> anyKey = new HashMap<>();
	@SuppressWarnings("hiding") private TestHandler handler;
	private StringBuffer sb;


	public TestUI(Client client, StringBuffer sb) {
		super(client, new TestHandler(client));
		handler = (TestHandler) super.getHandler();
		this.sb = sb;
	}


	@Override public TestHandler getHandler() {
		return handler;
	}


	@Override public void headerHandkartenTitle(String name) {
		buttons.clear();
		sb.append("<h2>");
		sb.append(name);
		sb.append("</h2>\n");
	}


	@Override public void button(String text, char accessKey, boolean ajax, Consumer<Handler> onClick) {
		buttons.put(text, onClick);
	}


	@Override public void button(Karte karte, boolean ajax, BiConsumer<Handler, Karte> onClick) {
		buttons.put(karte, handler -> onClick.accept(handler, karte));
	}


	@Override public void oneKarte(Karten karten, BiConsumer<Handler, Karte> onClick) {
		for (Karte karte : karten) {
			button(karte, true, onClick);
		}
	}
	
	
	@Override public String any(Karten karten, @Nullable Karten selected) {
		String result = Password.generateNewPassword(13);
		anyKey.put(karten, result);
		return result;
	}
	
	
	public void click(String text) throws KeyNotFoundException {
		buttons.getOrX(text).accept(getHandler());
	}


	public void click(Karte karte) throws KeyNotFoundException {
		buttons.getOrX(karte).accept(getHandler());
	}
	
	
	public void select(Karten karten, int... indexe) throws KeyNotFoundException {
		handler.putIndex(anyKey.getMOrX(karten), indexe);
	}
	
	
	public void select(Karten karten, Karte... selected) throws KeyNotFoundException {
		int[] indexe = Arrays.stream(selected)
			.mapToInt(karten::firstIndexOf)
			.toArray();
		select(karten, indexe);
	}


	@Override public String toString() {
		return buttons.keySet().toString();
	}

}
