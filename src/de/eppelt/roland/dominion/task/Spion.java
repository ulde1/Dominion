package de.eppelt.roland.dominion.task;


import de.eppelt.roland.dominion.Dran;
import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.task.SpionOpfer.Status;


/** +1 Karte, +1 Aktion; Alle Spieler (auch du) decken die oberste Karte ihres Nachziehstapels auf. Du entscheidest, wer seine Karte entsorgt und wer sie wieder auf seinen Nachziehstapel zurück tun muss. */
public class Spion extends TäterAufgabe<SpionOpfer> {
	
	
	public Spion(Dran dran) {
		super(dran, SpionOpfer::new);
	}
	
	
	@Override public void vorbereiten() {
		getDran().zieheKarten(1);
		getDran().addAktionen(1);
		super.vorbereiten();
	}


	@Override protected void showHeader() {
		super.showHeader();
		sayln("Das sind von allen Spielern die obersten Karten des Nachziehstapels. Du entscheidest, wer seine Karte entsorgt und wer sie wieder auf seinen Nachziehstapel zurück tun muss.");
	}


	@Override protected void opferAnzeigen(SpionOpfer opfer) {
		Karte karte = opfer.getKarte();
		if (karte!=null) {
			karte(karte);
			ln();
			button("Entsorgen", ' ', true, handler -> {
				opfer.setStatus(Status.ENTSORGEN);
				done(opfer);
			});
		} else {
			say(opfer.getOpfer().getName());
			sayln(" hat keine Karte mehr zum Nachziehen.");
		}
		button("Zurück", ' ', true, handler -> {
			opfer.setStatus(Status.ZURÜCK);
			done(opfer);
		});
	}


}
