package de.eppelt.roland.dominion.task;

import org.eclipse.jdt.annotation.Nullable;

import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Spieler;
import de.eppelt.roland.dominion.Spieler.EmptyDeckException;


/** Jeder Mitspieler muss die oberste Karte seines Nachziehstapels entsorgen. Du wählst eine Karte, die das gleiche kostet, wie die entsorgte Karte. Diese Karte muss sich der Spieler nehmen. */
public class TrickserOpfer extends OpferAufgabeImpl {
	
	
	protected @Nullable Karte entsorgt;
	protected @Nullable Karte bekommen;
	private @Nullable String täterMessage;
	private @Nullable String opferMessage;


	public TrickserOpfer(Spieler täter, Spieler opfer) {
		super(täter, opfer);
		setName("Trickser-Opfer");
	}
	
	
	public @Nullable Karte getEntsorgt() {
		return entsorgt;
	}


	@SuppressWarnings("null")
	public void setBekommen(Karte bekommen) {
		this.bekommen = bekommen;
		getOpfer().spielerHat(entsorgt.getName()+" entsorgt und "+bekommen.getName()+" bekommen.");
		getOpfer().updateMe();
	}


	public @Nullable String getOpferMessage() {
		return opferMessage;
	}
	

	public void setOpferMessage(String message) {
		opferMessage = message;
		getOpfer().updateMe();
	}


	public @Nullable String getTäterMessage() {
		return täterMessage;
	}
	
	
	public void setTäterMessage(@Nullable String message) {
		täterMessage = message;
	}


	@Override public void vorbereiten() {
		try {
			Karte karte = opfer.zieheKarte(false);
			trash().legeAb(karte);
			entsorgt = karte;
		} catch (EmptyDeckException e) {
			entsorgt = null;
		}
		super.vorbereiten();
	}
	

	@SuppressWarnings("null")
	@Override public boolean anzeigen() {
		headerHandkartenTitle();
		if (entsorgt==null) {
			sayln("Du kannst die oberste Karte deines Nachziehstapels gar nicht entsorgen, weil dein Nachziehstapel leer ist.");
			button("OK", 'o', true, handler -> done());
		} else if (bekommen==null) {
			sayln("Du musst die oberste Karte deines Nachziehstapels entsorgen:");
			karte(entsorgt);
			ln();
			sayln("Dafür bekommst du eine andere Karte für "+kosten(entsorgt)+" Münzen.");
		} else if (getOpferMessage()!=null) {
			sayln("Du hast die oberste Karte deines Nachziehstapels entsorgt:");
			karte(entsorgt);
			ln();
			sayln(getOpferMessage());
			button("Das ist jetzt echt ärgerlich!", 'd', true, handler -> done());
		} else {
			sayln("Du hast die oberste Karte deines Nachziehstapels entsorgt:");
			karte(entsorgt);
			ln();
			sayln("Dafür hast du ");
			button(bekommen, true, (handler, karte) -> {
				handler.seite().legeAb(bekommen);
				done();
			});
			ln();
			sayln("bekommen.");
			button("OK", 'o', true, handler -> {
				handler.seite().legeAb(bekommen);
				done();
			});
		}
		return true;
	}


}
