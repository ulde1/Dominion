package de.eppelt.roland.dominion.task;


import de.eppelt.roland.dominion.Dran;
import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Karten;
import de.eppelt.roland.dominion.Spieler.EmptyDeckException;


/** +2 Geld; Jeder Mitspieler muss die oberste Karte seines Nachziehstapels entsorgen. Du wählst eine Karte, die das gleiche kostet, wie die entsorgte Karte. Diese Karte muss sich der Spieler nehmen. */
public class Trickser extends TäterAufgabe<TrickserOpfer> {
	
	
	public Trickser(Dran dran) {
		super(dran, TrickserOpfer::new);
	}
	
	
	@Override public void vorbereiten() {
		dran.addGeld(2);
		super.vorbereiten();
	}


	@Override protected void showHeader() {
		super.showHeader();
		sayln("Jeder Mitspieler hat die oberste Karte seines Nachziehstapels entsorgt. Entscheide du, welche gleich teure Karte sich die Spieler nehmen müssen.");
	}


	@Override protected void opferAnzeigen(TrickserOpfer opfer) {
		Karte entsorgt = opfer.getEntsorgt();
		if (entsorgt==null) {
			say(opfer.getOpfer().getName());
			sayln(" hat gar keine Karte mehr zum Nachziehen.");
		} else {
			Karten karten = vorrat().getKarten(k -> kosten(k)==kosten(entsorgt));
			if (karten.isEmpty()) {
				opfer.setOpferMessage("Nur gibt es gar keine Karten mehr für "+entsorgt.getKosten(getInstance())+" Münzen. Du gehst leider leer aus.");
				done(opfer);
			} else {
				say("Diese Karte hat ");
				say(opfer.getOpfer().getName());
				sayln(" entsorgt:");
				karte(entsorgt);
				ln();
				sayln("Welche Karte bekommt er statt dessen?");
				String täterMessage = opfer.getTäterMessage();
				if (täterMessage!=null) {
					sayln(täterMessage);
				}
				oneKarte(karten, (handler, karte) -> {
					try {
						opfer.setBekommen(handler.vorrat().zieheKarte(karte));
					} catch (EmptyDeckException e) {
						opfer.setTäterMessage("Hinweis: Der "+karte.getName()+"-Vorrat ist leer. Wähle eine andere Karte.");
					}
					done(opfer);
				});
			}
		}
	}

}
