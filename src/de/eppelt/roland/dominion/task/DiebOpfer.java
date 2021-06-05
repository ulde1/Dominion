package de.eppelt.roland.dominion.task;


import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Karten;
import de.eppelt.roland.dominion.Spieler;


/** Jeder Mitspieler deckt die obersten zwei Karten seines Nachziehstapels auf. 
 * Haben die Mitspieler eine oder mehrere Geldkarten aufgedeckt, 
 * muss jeder eine davon (nach deiner Wahl) entsorgen. 
 * Du darfst eine beliebige Zahl der entsorgten Karten nehmen.
 * Alle übrigen aufgedeckten Karten legen die Spieler bei sich ab.  */
public class DiebOpfer extends OpferAufgabeImpl {

	
	public static enum Status { KEIN1, KEIN2, KEIN12, ABLIEFERN1, ABLIEFERN2, ENTSORGEN1, ENTSORGEN2, ABLEGEN }
	

	final static Status[] INIT = new Status[] { Status.ABLEGEN, Status.KEIN1, Status.KEIN2, Status.KEIN12 }; 
	
	Karten karten;
	Status status;
	
	
	public DiebOpfer(Spieler täter, Spieler opfer) {
		super(täter, opfer);
		this.karten = opfer.zieheKartenKarten(2);
		this.status = INIT[(karten.get(0).isGeld() ? 1 : 0)+(karten.get(1).isGeld() ? 2 : 0)];
		if (status==Status.KEIN12 && karten.get(0)==karten.get(1)) {
			status = Status.KEIN1;
		}
		setName("Dieb-Opfer");
		if (opfer==täter) {
			setOpferstatus(Opferstatus.UNGESCHÜTZT);
		}
	}


	public Karten getKarten() {
		return karten;
	}
	

	public Status getStatus() {
		return status;
	}
	

	public void setStatus(Status status) {
		this.status = status;
		getOpfer().updateMe();
	}


	@Override public void setOpferstatus(Opferstatus opferstatus) {
		super.setOpferstatus(opferstatus);
		if (opferstatus==Opferstatus.GESCHÜTZT) {
			getOpfer().getNachziehStapel().legeAlleAbVon(karten);
		}
	}


	private void abliefern(int abliefernIndex, int ablegenIndex) {
		play("BadLuck.mp3");
		Karte abliefernKarte = karten.get(abliefernIndex);
		say("Du musst dein ");
		say(abliefernKarte.getName());
		say(" an ");
		say(getTäter().getName());
		sayln(" abliefern.");
		karten(karten);
		ln();
		button(karten.size()==1 ? "Abliefern" : abliefernKarte.getName()+" an "+getTäter().getName()+" abliefern, "+karten.get(ablegenIndex).getName()+" bei dir ablegen", 'a', true, h -> {
			getTäter().getNachziehStapel().legeAb(abliefernKarte);
			if (karten.size()>2) {
				h.ablage().legeAb(karten.get(ablegenIndex));
			}
			getTäter().updateMe();
			done();
		});
		ln();
	}
	

	private void entsorgen(int entsorgenIndex, int ablegenIndex) {
		play("BadLuck.mp3");
		Karte entsorgenKarte = karten.get(entsorgenIndex);
		say("Du musst dein ");
		say(entsorgenKarte.getName());
		sayln(" entsorgen.");
		karten(karten);
		ln();
		button(karten.size()==1 ? "Entsorgen" : entsorgenKarte.getName()+" entsorgen, "+karten.get(ablegenIndex).getName()+" ablegen", 'e', true, handler -> {
			handler.trash().legeAb(entsorgenKarte);
			if (karten.size()>2) {
				handler.ablage().legeAb(karten.get(ablegenIndex));
			}
			done();
		});
		ln();
	}


	@Override public boolean execute() {
		if (getTäter()==getOpfer()) {
			done();
			return false;
		} else {
			headerHandkartenTitle(getName());
			switch (status) {
				case KEIN1: case KEIN2: case KEIN12:
					sayln("Du musst deine beiden obersten Karten vom Nachziehstapel vorzeigen:");
					karten(karten);
					return true;
				case ABLIEFERN1:
					abliefern(0, 1);
					return true;
				case ABLIEFERN2: 
					abliefern(1, 0);
					return true;
				case ENTSORGEN1:
					entsorgen(0, 1);
					return true;
				case ENTSORGEN2: 
					entsorgen(1, 0);
					return true;
				case ABLEGEN: 
					play("Luck.mp3");
					sayln("Glück gehabt! Du darfst die beiden obersten Karten vom Nachziehstapel einfach ablegen, weil keine Geldkarte darunter war.");
					karten(karten);
					ln();
					button("Ablegen", 'a', true, h -> {
						h.ablage().legeAlleAbVon(karten);
						done();
					});
					return true;
				default:
					return false;
			}
		}
	}


}
