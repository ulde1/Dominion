package de.eppelt.roland.dominion.task;


import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import de.eppelt.roland.dominion.Dran;
import de.eppelt.roland.dominion.Spieler;


/** Ein {@link AufgabeImpl} für Täter von {@link OpferAufgabe}n.
 * @param <OPFER> Die {@link OpferAufgabe}
 * @author Roland M. Eppelt
 */
public abstract class TäterAufgabe<OPFER extends OpferAufgabe> extends DranAufgabeImpl {

	
	protected List<OPFER> opfers;
	
	
	/** Erzeugt eine {@link TäterAufgabe}. 
	 * @param täter {@link Spieler}
	 * @param opferSupplier Erzeugt {@link OpferAufgabe}n
	 */
	public TäterAufgabe(Dran täter, BiFunction<Spieler, Spieler, OPFER> opferSupplier) {
		super(täter);
		opfers = getSpieler().alleAnderenSpieler()
			.map(opfer -> opferSupplier.apply(getSpieler(), opfer))
			.collect(Collectors.toList());
	}
	
	
	@Override public void vorbereiten() {
		opfers.forEach(o -> o.getOpfer().putAufgabe(new Schutz(o)));
		super.vorbereiten();
	}
	
	
	protected void done(OPFER opfer) {
		opfers.remove(opfer);
	}
	
	
	/** Hier kann zusätzlich eine Meldung am Anfang vor den einzelnen {@link OPFER}n ausgegeben werden. */
	protected void executeHeader() {
		headerHandkartenTitle(getName());
	}

	
	/** Ausgabe für ein Opfer erzeugen. 
	 * Für abgeschlossene {@link OPFER} bitte {@link #done(OpferAufgabe)} aufrufen.
	 * @param opfer {@link OPFER}
	 */
	protected abstract void opferAnzeigen(OPFER opfer);

	
	@Override public boolean anzeigen() {
		if (opfers.size()==0) {
			done();
			return false;
		} else {
			executeHeader();
			for (OPFER opfer : opfers) {
				title(opfer.getOpfer().getName());
				if (opfer.isUngeschütztElseSay(getUI())) {
					opferAnzeigen(opfer);
					ln();
				}
			}
			if (opfers.stream().allMatch(OpferAufgabe::isGeschützt)) {
				button("Aber das nächste Mal!", 'a', true, handler -> done());
			}
			return true;
		}
	}


}
