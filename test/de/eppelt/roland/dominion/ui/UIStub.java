package de.eppelt.roland.dominion.ui;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.Nullable;

import de.eppelt.roland.dominion.Client;
import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Karten;
import de.eppelt.roland.dominion.Spieler;



public class UIStub implements UI {
	
	
	protected Client client;
	protected Handler handler;


	public UIStub(Client client, Handler handler) {
		super();
		this.client = client;
		this.handler = handler;
	}


	@Override public Client getClient() {
		return client;
	}


	@Override public Handler getHandler() {
		return handler;
	}


	@Override public void setClient(Client client) {
		this.client = client;
	}


	@Override public void setStringBuffer(StringBuffer sb) {
	}


	@Override public void say(String message) {
	}


	@Override public void say(int i) {
	}


	@Override public void ln() {
	}


	@Override public void br() {
	}


	@Override public void play(String url) {
	}


	@Override public void button(String text, char accessKey, boolean ajax, Consumer<Handler> onClick) {
	}


	@Override public void button(Karte karte, boolean ajax, BiConsumer<Handler, Karte> onClick) {
	}


	@Override public void karte(Karte karte) {
	}


	@Override public void karten(Karten karten, boolean vonOben) {
	}


	@Override public void oneKarte(Karten karten, BiConsumer<Handler, Karte> onClick) {
	}


	@Override public void oneIndex(Karten karten, BiConsumer<Handler, Integer> onClick) {
	}


	@Override public String any(Karten karten, @Nullable Karten selected) {
		return "";
	}


	@Override public void header() {
	}


	@Override public void headerHandkarten() {
	}


	@Override public void title(String name) {
	}


	@Override public void headerHandkartenTitle(String name) {
	}


	@Override public void handkarten(Spieler spieler) {
	}


	@Override public void mitspieler() {
	}


	@Override public void footer() {
	}


	@Override public void columnsStart() {
	}


	@Override public void nextColumn() {
	}


	@Override public void columnsEnd() {
	}

}
