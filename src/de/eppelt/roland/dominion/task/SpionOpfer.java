package de.eppelt.roland.dominion.task;

import org.eclipse.jdt.annotation.Nullable;

import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Spieler;
import de.eppelt.roland.dominion.Spieler.EmptyDeckException;


/** Alle Spieler (auch du) decken die oberste Karte ihres Nachziehstapels auf. Du entscheidest, wer seine Karte entsorgt und wer sie wieder auf seinen Nachziehstapel zurück tun muss */
public class SpionOpfer extends OpferAufgabeImpl {

	
	public static enum Status { KEIN, ZURÜCK, ENTSORGEN } 
	
	
	@Nullable Karte karte;
	Status status;
	
	
	
	@SuppressWarnings("null")
	public SpionOpfer(Spieler täter, Spieler opfer) {
		super(täter, opfer);
		setName("Spion-Opfer von "+täter.getName());
	}
	
	
	@Override public void vorbereiten() {
		try {
			karte = opfer.zieheKarte(false);
		} catch (EmptyDeckException e) {
			// Hat man dann Glück gehabt?
			karte = null;
		}
		this.status = Status.KEIN;
		super.vorbereiten();
	}
	
	
	public @Nullable Karte getKarte() {
		return karte;
	}
	

	public Status getStatus() {
		return status;
	}
	

	public void setStatus(Status status) {
		this.status = status;
		getOpfer().updateMe();
	}
	
	
	@SuppressWarnings("null")
	@Override public void setOpferstatus(Opferstatus opferstatus) {
		super.setOpferstatus(opferstatus);
		if (karte!=null && opferstatus==Opferstatus.GESCHÜTZT) {
			getOpfer().getNachziehStapel().legeAb(karte);
		}
	}


	@SuppressWarnings("null")
	@Override public boolean anzeigen() {
		if (karte==null) {
			headerHandkartenTitle(getName());
			sayln("Welch Glück! Keine Karte mehr zum Nachziehen da. Oder ist das eher: Pech?");
			done();
			return true;
		} else if (getOpfer()==getTäter()) {
			switch (getStatus()) {
				case KEIN:
					warning("Status KEIN, obwohl Täter==Opfer");
					break;
				case ZURÜCK:
					getOpfer().getNachziehStapel().legeAb(karte);
					break;
				case ENTSORGEN: default:
					break;
			}
			done();
			return false;
		} else {
			headerHandkartenTitle(getName());
			switch (status) {
				case KEIN:
					sayln("Du musst deine oberste Karte vom Nachziehstapel vorzeigen:");
					karte(karte);
					return true;
				case ZURÜCK:
					play("BadLuck.mp3");
					sayln("Du musst deine oberste Karte vom Nachziehstapel zurücklegen:");
					karte(karte);
					ln();
					button("Zurück", 'z', true, handler -> {
						handler.nachziehStapel().legeAb(karte);
						done();
					});
					return true;
				case ENTSORGEN:
					play("BadLuck.mp3");
					sayln("Du musst deine oberste Karte vom Nachziehstapel entsorgen:");
					karte(karte);
					ln();
					button("Entsorgen", 'e', true, handler -> {
						handler.trash().legeAb(karte);
						done();
					});
					return true;
				default:
					return false;
			}
		}
	}


}
