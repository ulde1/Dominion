package de.eppelt.roland.dominion.task;

public class BanditinPech extends AufgabeImpl {


	@Override public boolean execute() {
		headerHandkartenTitle();
		sayln("Es gibt leider kein Gold mehr im Vorrat, das du nachziehen könntest. Schade.");
		sayln("Aber die anderen Mitspieler müssen trotzdem eine ihrer beiden obersten Geldkarten entsorgen! Ha!");
		button("OK", 'o', true, client -> done());
		return true;
	}


}