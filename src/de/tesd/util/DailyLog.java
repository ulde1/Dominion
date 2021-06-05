package de.tesd.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.logging.Logger;


/** Loggt täglich das Datum. Benutzung einfach mit
 * new {@link #DailyLog()}.{@link #start()} */
public class DailyLog extends Thread {
	

	public final static long SecondsPerDay = 24*60*60;
	public final static Logger log = Logger.getLogger(DailyLog.class.getName());
	
	
	protected boolean running;
	@SuppressWarnings("null") protected LocalDateTime nextDay;
	
	
	public static LocalDateTime beginOfDay(LocalDateTime instant) {
		return instant.minus(instant.getLong(ChronoField.SECOND_OF_DAY), ChronoUnit.SECONDS);
	}
	
	
	@Override public void run() {
		running = true;
		nextDay = beginOfDay(LocalDateTime.now());
		while (running) {
			LocalDateTime now = LocalDateTime.now();
			while (running && now.isBefore(nextDay)) { 
				long seconds = Duration.between(now, nextDay).get(ChronoUnit.SECONDS);
				long nanos = Duration.between(now, nextDay).get(ChronoUnit.NANOS);
				long millis = Math.max(1L, 1000L*seconds+nanos/1000000L);
				synchronized(this) {
					try { wait(millis);
					} catch (InterruptedException e) {
					}
				}
				now = LocalDateTime.now();
			}
				// Ein neuer Tag!
			if (running) {
				log.info("TODAY="+DateTimeFormatter.ISO_LOCAL_DATE.format(nextDay));
				nextDay = nextDay.plus(1, ChronoUnit.DAYS);
			}
		}
	}
	
	
	public void stopLog() {
		running = false;
	}
	

}
