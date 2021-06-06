package de.eppelt.roland.dominion.task;

import java.util.List;

import org.eclipse.jdt.annotation.Nullable;

import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Spieler;


/** Alle Spieler (auch du selbst) müssen gleichzeitig eine Karte aus ihrer Hand an ihren linken Nachbarn weiter geben. Danach darfst du eine Karte aus deiner Hand entsorgen. */
public class Maskerade extends AufgabeImpl {
	
	
	Spieler spieler;
	List<Maskerade> gebend, wartend;
	Maskerade linkerNachbar, rechterNachbar;
	@Nullable Karte bekommen = null;
	
	
	@SuppressWarnings("null")
	public Maskerade(Spieler spieler, List<Maskerade> gebend, List<Maskerade> wartend) {
		this.spieler = spieler;
		this.gebend = gebend;
		this.wartend = wartend;
	}
	
	
	public void setIndex(int index) {
		linkerNachbar = gebend.get((index+1) % gebend.size());
		rechterNachbar = gebend.get((index+gebend.size()-1) % gebend.size());
	}
	

	@Override public Spieler getSpieler() {
		return spieler;
	}
	
	
	public void setBekommen(Karte bekommen) {
		this.bekommen = bekommen;
	}


	public String spielersNamen() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i<gebend.size(); i++) {
			if (i>0) {
				if (i==gebend.size()-1) {
					sb.append(" und ");
				} else {
					sb.append(", ");
				}
			}
			sb.append(gebend.get(i).getSpieler().getName());
		}
		return sb.toString();
	}

	
	@SuppressWarnings("null")
	@Override public boolean anzeigen() {
		headerHandkartenTitle(getName());
		if (gebend.contains(this)) {
			say("Welche Karte gibst du an ");
			say(linkerNachbar.getSpieler().getName());
			sayln(" weiter?");
			oneKarte(handkarten(), (handler, karte) -> {
				handler.handkarten().entferne(karte);
				linkerNachbar.setBekommen(karte);
				gebend.remove(this);
				wartend.stream()
					.map(Maskerade::getSpieler)
					.forEach(Spieler::updateMe);
				wartend.add(this);
			});
		} else if (!gebend.isEmpty()) {
			say("Warte noch auf ");
			say(spielersNamen());
			sayln(".");
		} else {
			sayln("Du hast diese Karte von ");
			say(rechterNachbar.getSpieler().getName());
			sayln(" bekommen:");
			button(bekommen, true, (handler, karte) -> {
				nachziehStapel().legeAb(bekommen);
				done();
			});
			ln();
			button("Ablegen", 'a', true, handler -> {
				nachziehStapel().legeAb(bekommen);
				done();
			});
			ln();
		}
		return true;
		
	}


}
