package de.eppelt.roland.dominion.task;


import de.eppelt.roland.dominion.ui.UI;
import de.eppelt.roland.dominion.ui.UIEase;


public interface Aufgabe extends UIEase {


	String getName();


	void setName(String name);


	@Override UI getUI();


	void setUI(UI ui);


	/**
	 * @return Aufgabe abgeschlossen? Dann auch {@link #done()}! Sonst Endlosschleife…
	 */
	boolean execute();
	
	
	void done();


}