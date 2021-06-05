package de.eppelt.roland.dominion.task;


import java.util.function.Supplier;
import java.util.logging.Logger;

import org.eclipse.jdt.annotation.Nullable;

import de.eppelt.roland.dominion.Client;
import de.eppelt.roland.dominion.DominionHttpServer;
import de.eppelt.roland.dominion.ui.UI;
import de.tesd.util.Loggers;
import de.tesd.util.O;


public abstract class AufgabeImpl implements Aufgabe, Loggers {
	
	
	public final static Logger LOG = Logger.getLogger(AufgabeImpl.class.getName());
	@Override public Logger logger() { return LOG; }
	
	
	private String name = getClass().getSimpleName();
	protected @Nullable UI ui = null;
	
	
	@Override public Client getClient() {
		return getUI().getClient();
	}
	
	
	boolean played = false;
	
	
	@Override public void play(String url) {
		if (!played) {
			getUI().play(DominionHttpServer.SOUND_PATH+url);
			played = true;
		}
	}


	public void fine(String msg) {
		Loggers.super.fine(() -> this.toString()+": "+msg);
	}


	@Override public void fine(Supplier<String> msg) {
		Loggers.super.fine(() -> this.toString()+": "+msg.get());
	}
	
	
	@SuppressWarnings("null")
	@Override public UI getUI() {
		return ui;
	}
	
	
	@Override public void setUI(UI ui) {
		this.ui = ui;
	}
	

	@Override public String getName() {
		return name;
	}


	@Override public void setName(String name) {
		this.name = name;
	}
	
	
	@Override public void done() {
		O.nnc(getUI(), ui -> ui.getSpieler().aufgabeErledigt(this));
	}
	
	
	@SuppressWarnings("null")
	public void headerHandkartenTitle() {
		ui.headerHandkartenTitle(getName());
	}


	@Override public String toString() {
		return getName();
	}


}