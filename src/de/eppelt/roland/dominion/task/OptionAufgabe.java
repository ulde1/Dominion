package de.eppelt.roland.dominion.task;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.Nullable;

import de.eppelt.roland.dominion.Karte;
import de.eppelt.roland.dominion.Spieler.EmptyDeckException;
import de.eppelt.roland.dominion.ui.Handler;
import de.tesd.util.Strings;

public class OptionAufgabe extends AufgabeImpl {
	
	
	public static enum Option {
		KARTE("+1 Karte", 'r', handler -> handler.zieheKarten(1)), 
		AKTION("+1 Aktion", 'a', handler -> handler.addAktionen(1)), 
		KAUF("+1 Kauf", 'k', handler -> handler.addKäufe(1)), 
		GELD1("+1 Geldmünze", 'g', handler -> handler.addGeld(1)), 
		GELD3("+3 Geldmünzen", 'g', handler -> handler.addGeld(3)), 
		GOLD("+1 Gold", 'o', handler -> {
			try {
				handler.seite().legeAb(handler.vorrat().zieheKarte(Karte.GOLD));
			} catch (EmptyDeckException e) {
				// Pech halt.
			}
		}); 
		
		
		String text;
		char accessKey;
		Consumer<Handler> onClick;
		
		
		private Option(String text, char accessKey, Consumer<Handler> onClick) {
			this.text = text;
			this.accessKey = accessKey;
			this.onClick = onClick;
		}
		
		
	}
	

	int anzahl;
	int count = 0;
	@Nullable String message = null;
	List<Option> verfügbar; 
	
	
	public OptionAufgabe(int anzahl, Option... optionen) {
		this.anzahl = anzahl;
		verfügbar = Arrays.stream(optionen).collect(Collectors.toList());
	}
	
	
	@Override public boolean execute() {
		if (anzahl<=0) {
			if (message!=null) {
				spielerHat(message);
			}
			done();
			return false;
		} else {
			headerHandkartenTitle(getName());
			if (anzahl>1) {
				say("Wähle ");
				say(anzahl);
				sayln(" verschiedene:");
			} else {
				sayln("Wähle aus:");
			}
			for (Option option : verfügbar) {
				button(option.text, option.accessKey, true, handler -> {
					option.onClick.accept(handler);
					verfügbar.remove(option);
					message = Strings.sentence(" und ", true, message, option.text);
					anzahl--;
				});
				ln();
			}
			return true;
		}
	}

}
