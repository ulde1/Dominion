package de.eppelt.roland.dominion.task;


import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Spieler;
import de.eppelt.roland.dominion.task.SpionOpfer.Status;


/** +1 Karte, +1 Aktion; Alle Spieler (auch du) decken die oberste Karte ihres Nachziehstapels auf. Du entscheidest, wer seine Karte entsorgt und wer sie wieder auf seinen Nachziehstapel zurück tun muss. */
public class Spion extends TäterAufgabe<SpionOpfer> {
	
	
	public Spion(Spieler spieler) {
		super(spieler, SpionOpfer::new);
	}


	@Override protected void executeHeader() {
		super.executeHeader();
		sayln("Das sind von allen Spielern die obersten Karten des Nachziehstapels. Du entscheidest, wer seine Karte entsorgt und wer sie wieder auf seinen Nachziehstapel zurück tun muss.");
	}


	@Override protected void executeOpfer(SpionOpfer opfer) {
		Karte karte = opfer.getKarte();
		if (karte!=null) {
			karte(karte);
		} else {
			say(opfer.getName());
			sayln(" hat keine Karte mehr zum Nachziehen.");
		}
		ln();
		button("Entsorgen", ' ', true, handler -> {
			opfer.setStatus(Status.ENTSORGEN);
			done(opfer);
		});
		button("Zurück", ' ', true, handler -> {
			opfer.setStatus(Status.ZURÜCK);
			done(opfer);
		});
	}


}
