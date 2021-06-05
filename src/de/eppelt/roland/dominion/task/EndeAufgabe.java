package de.eppelt.roland.dominion.task;

public class EndeAufgabe extends AufgabeImpl {


	@Override public boolean execute() {
		header();
		title("Ende");
		sayln("Das Spiel ist zu Ende.");
		handkarten(getSpieler());
		return true;
	}


	@Override public String getName() {
		return "nichts mehr";
	}

}