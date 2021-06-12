package de.eppelt.roland.dominion.ui;


import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.Nullable;

import de.eppelt.roland.dominion.Client;
import de.eppelt.roland.dominion.ClientEase;
import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Karten;
import de.eppelt.roland.dominion.Spieler;


public interface UI extends ClientEase {
	
	
	Handler getHandler();
	
	
	void setClient(Client client);


	void setStringBuffer(StringBuffer sb);


	void say(String message);


	void say(int i);


	void ln();
	
	
	default void sayln(String message) {
		say(message);
		ln();
	}
	
	
	default void lnsay(String message) {
		ln();
		say(message);
	}
	
	
	default void lnsayln(String message) {
		ln();
		say(message);
		ln();
	}
	
	
	void br();
	
	
	void play(String url);


	/**
	 * @param text
	 * @param accessKey nur {@link Character#isLetterOrDigit(char)} wird berücksichtigt
	 * @param ajax
	 * @param onClick
	 */
	void button(String text, char accessKey, boolean ajax, Consumer<Handler> onClick);
	
	
	void button(Karte karte, boolean ajax, BiConsumer<Handler, Karte> onClick);


	void karte(Karte karte);


	void karten(Karten karten, boolean vonOben);


	void oneKarte(Karten karten, BiConsumer<Handler, Karte> onClick);
	
	
	void oneIndex(Karten karten, BiConsumer<Handler, Integer> onClick);


	String any(Karten karten, @Nullable Karten selected);


	void header();


	void headerHandkarten();


	void title(String name);


	void headerHandkartenTitle(String name);


	void handkarten(Spieler spieler);


	void mitspieler();


	/** {@link {@link #appendMitspieler(StringBuffer, Client) Mitspieler} und {@link #appendFooterButtons(StringBuffer, Client) FooterButtons} */
	void footer();


	void columnsStart();


	void nextColumn();


	void columnsEnd();


}
