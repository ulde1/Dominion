package de.eppelt.roland.dominion.task;

import de.eppelt.roland.dominion.Dran;
import de.eppelt.roland.dominion.task.DiebOpfer.Status;


/** Jeder Mitspieler deckt die obersten zwei Karten seines Nachziehstapels auf. 
 * Haben die Mitspieler eine oder mehrere Geldkarten aufgedeckt, 
 * muss jeder eine davon (nach deiner Wahl) entsorgen. 
 * Du darfst eine beliebige Zahl der entsorgten Karten nehmen.
 * Alle übrigen aufgedeckten Karten legen die Spieler bei sich ab.  */
public class Dieb extends TäterAufgabe<DiebOpfer> {

	
	public Dieb(Dran dran) {
		super(dran, DiebOpfer::new);
	}

	
	@Override protected void showHeader() {
		super.showHeader();
		sayln("Das sind von allen Spielern die obersten zwei Karten des Nachziehstapels. Du entscheidest, welche Geldkarte entsorgt oder dir gegeben werden muss.");
	}


	@Override protected void opferAnzeigen(DiebOpfer opfer) {
		karten(opfer.getKarten(), true);
		ln();
		switch (opfer.getStatus()) {
			case KEIN1:
				button(opfer.getKarten().get(0).getName()+" entsorgen", 'e', true, handler -> {
					opfer.setStatus(Status.ENTSORGEN1);
					done(opfer);
				});
				button(opfer.getKarten().get(0).getName()+" nehmen", 'n', true, handler -> {
					opfer.setStatus(Status.ABLIEFERN1);
					done(opfer);
				});
				break;
			case KEIN2:
				button(opfer.getKarten().get(1).getName()+" entsorgen", 'e', true, handler -> {
					opfer.setStatus(Status.ENTSORGEN2);
					done(opfer);
				});
				button(opfer.getKarten().get(1).getName()+" nehmen", 'n', true, handler -> {
					opfer.setStatus(Status.ABLIEFERN2);
					done(opfer);
				});
				break;
			case KEIN12:
				button(opfer.getKarten().get(0).getName()+" entsorgen", 'e', true, handler -> {
					opfer.setStatus(Status.ENTSORGEN1);
					done(opfer);
				});
				button(opfer.getKarten().get(1).getName()+" entsorgen", 'e', true, handler -> {
					opfer.setStatus(Status.ENTSORGEN2);
					done(opfer);
				});
				ln();
				button(opfer.getKarten().get(0).getName()+" nehmen", 'n', true, handler -> {
					opfer.setStatus(Status.ABLIEFERN1);
					done(opfer);
				});
				button(opfer.getKarten().get(1).getName()+" nehmen", 'n', true, handler -> {
					opfer.setStatus(Status.ABLIEFERN2);
					done(opfer);
				});
				break;
			case ABLEGEN:
				button("ablegen", 'a', true, handler -> {
					done(opfer);
				});
				break;
			default:
				break;
		}
	}


}
