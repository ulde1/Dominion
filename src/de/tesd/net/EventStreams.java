package de.tesd.net;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.Nullable;


/** Eine Menge von {@link EventStream}s, an die mit {@link EventStreams#sendEvent(InetSocketAddress, String, String) sendEvent}
 * Nachrichten geschickt werden können. Nicht zugestellte Nachrichten werden gespeichert und dem nächsten {@link EventStream} geschickt.*/
public class EventStreams {
	
	
	public static class Event {
		@Nullable InetSocketAddress source;
		@Nullable String streamName;
		String event;
		@Nullable String data;
		boolean broadcast;
		public Event(@Nullable InetSocketAddress source, @Nullable String streamName, String event, @Nullable String data, boolean broadcast) {
			this.source = source;
			this.streamName = streamName;
			this.event = event;
			this.data = data;
			this.broadcast = broadcast;
		}
	}
	
	
	protected HashSet<EventStream> eventStreams = new HashSet<>();
	protected Queue<EventStreams.Event> savedEvents = new LinkedList<EventStreams.Event>(); 
	
	
	protected int sendEvent(EventStreams.Event event) {
		return sendEvent(event.source, event.streamName, event.event, event.data, event.broadcast);
	}
	
	
	/** Sendet event/data an alle {@link EventStream}s. Bei Fehlern wird der entsprechende {@link EventStream} entfernt.
	 * @param source Absender; an diese Adresse soll das Event nicht gesendet werden. Kann null sein.
	 * @param streamName Empfänger-Stream oder null für alle
	 * @param event Bezeichnung des Events (eine Zeile), nicht null!
	 * @param data Zusätzliche Daten (mehrzeilig), kann null sein.
	 * @param broadcast An alle {@link EventStream}s versenden und nicht wiederholen 
	 *	(sonst: nur an den ersten erreichbaren Empfänger schicken und wiederholen, bis der Event zugestellt wurde)
	 * @return Anzahl der Empfänger, denen der Event zugestellt werden konnte. */
	public synchronized int sendEvent(@Nullable InetSocketAddress source, @Nullable String streamName, String event, @Nullable String data, boolean broadcast) {
		Http.LOG.finer(() -> "send("+event+", [data])");
		Http.LOG.finest(() -> "send("+event+", "+data+")");
		int result = 0;
		Stream<EventStream> stream = eventStreams.stream();
		if (streamName!=null)
			stream = stream.filter(eventStream -> streamName.equals(eventStream.getName()));
		for (EventStream eventStream : stream.toArray(EventStream[]::new))
			if (!eventStream.http.getRemoteAddress().equals(source))
				if (eventStream.send(event, data)) {
					result++;
					if (!broadcast)
						break;
				} else
					eventStreams.remove(eventStream);
		if (!broadcast && result<=0)
			savedEvents.add(new Event(source, streamName, event, data, broadcast));
		return result;
	}
	
	
	/** Nimmt einen neuen {@link EventStream} auf. Noch nicht zugestellte {@link Event}s werden verschickt. */
	public synchronized void register(EventStream eventStream) {
		eventStreams.add(eventStream);
		Queue<EventStreams.Event> events = savedEvents;
		savedEvents = new LinkedList<EventStreams.Event>();
		events.forEach(e -> sendEvent(e));
	}
	
	
	public void remove(EventStream eventStream) {
		eventStreams.remove(eventStream);
	}
	
	
	public void clear() {
		eventStreams.clear();
		savedEvents.clear();
	}
	
	
	/** @return Gibt es einen {@link EventStream} mit {@link EventStream#isAlive()}? */
	public boolean isAlive() {
		return eventStreams.stream().anyMatch(EventStream::isAlive);
	}
	
	
	public Stream<EventStream> stream() {
		return eventStreams.stream();
	}
	
	
	@Override public String toString() {
		return eventStreams.toString();
	}


}