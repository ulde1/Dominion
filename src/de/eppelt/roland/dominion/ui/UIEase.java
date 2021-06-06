package de.eppelt.roland.dominion.ui;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.Nullable;

import de.eppelt.roland.dominion.Client;
import de.eppelt.roland.dominion.ClientEase;
import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Karten;
import de.eppelt.roland.dominion.Spieler;

public interface UIEase extends ClientEase {
	
	
	UI getUI();
	
	
	default void say(String message) {
		getUI().say(message);
	}


	default void say(int i) {
		getUI().say(i);
	}


	default void ln() {
		getUI().ln();
	}
	
	
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
	
	
	default void play(String url) {
		getUI().play(url);
	}


	/**
	 * @param text
	 * @param accessKey nur {@link Character#isLetterOrDigit(char)} wird berücksichtigt
	 * @param ajax
	 * @param onClick
	 */
	default void button(String text, char accessKey, boolean ajax, Consumer<Handler> onClick) {
		getUI().button(text, accessKey, ajax, onClick);
	}	
	
	
	default void button(Karte karte, boolean ajax, BiConsumer<Handler, Karte> onClick) {
		getUI().button(karte, ajax, onClick);
	}


	default void karte(Karte karte) {
		getUI().karte(karte);
	}


	default void karten(Karten karten, boolean vonOben) {
		getUI().karten(karten, vonOben);
	}


	default void oneKarte(Karten karten, BiConsumer<Handler, Karte> onClick) {
		getUI().oneKarte(karten, onClick);
	}
	
	
	default void oneIndex(Karten karten, BiConsumer<Handler, Integer> onClick) {
		getUI().oneIndex(karten, onClick);
	}

	
	
	default String any(Karten karten, @Nullable Karten selected) {
		return getUI().any(karten, selected);
	}


	default void header() {
		getUI().header();
	}


	default void headerHandkarten() {
		getUI().headerHandkarten();
	}


	default void title(String name) {
		getUI().title(name);
	}


	default void headerHandkartenTitle(String name) {
		getUI().headerHandkartenTitle(name);
	}


	default void handkarten(Spieler spieler) {
		getUI().handkarten(spieler);
	}


	default void mitspieler() {
		getUI().mitspieler();
	}


	/** {@link {@link #appendMitspieler(StringBuffer, Client) Mitspieler} und {@link #appendFooterButtons(StringBuffer, Client) FooterButtons} */
	default void footer() {
		getUI().footer();
	}


	default void columnsStart() {
		getUI().columnsStart();
	}


	default void nextColumn() {
		getUI().nextColumn();
	}


	default void columnsEnd() {
		getUI().columnsEnd();
	}


}
