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
	
	
	/** @return alle {@link OPFER} */
	public List<OPFER> getOpfers() {
		return opfers;
	}
	
	
	/** Erzeugt und aktiviert alle {@link OPFER}.
	 * {@inheritDoc} */
	@Override public void vorbereiten() {
		opfers.forEach(o -> o.getOpfer().putAufgabe(new Schutz(o)));
		super.vorbereiten();
	}
	
	
	protected void done(OPFER opfer) {
		opfers.remove(opfer);
	}
	
	
	/** Hier kann zusätzlich eine Meldung vor dem ersten {@link OPFER} ausgegeben werden.
	 * Standard: {@link #headerHandkartenTitle()}. */
	protected void showHeader() {
		headerHandkartenTitle(getName());
	}

	
	/** Ausgabe für ein Opfer erzeugen. 
	 * Für abgeschlossene {@link OPFER} bitte {@link #done(OpferAufgabe)} aufrufen.
	 * @param opfer {@link OPFER}
	 */
	protected abstract void opferAnzeigen(OPFER opfer);

	
	/** Zeigt <ol><li>{@link #showHeader()} (falls es {@link OPFER} gibt) und</li><li>für alle {@link #getOpfers()} die {@link #opferAnzeigen(OpferAufgabe)}</li></ol> an. 
	 * @return gibt es {@link OPFER}?*/
	@Override public boolean anzeigen() {
		if (opfers.size()==0) {
			done();
			return false;
		} else {
			showHeader();
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
