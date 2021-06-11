package de.eppelt.roland.dominion.task;


import de.eppelt.roland.dominion.Spieler;
import de.eppelt.roland.dominion.ui.UI;
import de.eppelt.roland.dominion.ui.UIEase;


public interface Aufgabe extends UIEase {


	String getName();


	void setName(String name);
	
	
	void setSpieler(Spieler spieler);


	/** Wird genau einmal aufgerufen, bevor die Aufgabe aktiviert wird. Zu dieser Zeit ist {@link #getUI()} noch nicht gesetzt, aber {@link #getSpieler()} und {@link #getInstance()}. */
	void vorbereiten();


	@Override UI getUI();


	void setUI(UI ui);


	/** Zeigt die Aufgabe an und kann sie mit {@link #done()} abschließen. Kann oft aufgerufen werden und sollte keine Änderungen am Status vornehmen.
	 * @return Aufgabe abgeschlossen? Dann auch {@link #done()}! Sonst Endlosschleife…
	 */
	boolean anzeigen();
	
	
	/** erledigt diese Aufgabe. Gut wäre, wenn die Folgeaufgabe schon auf dem Aufgaben-Stack liegt, damit keine anderen Aufgaben dazwischen grätschen. */
	void done();


}