package de.tesd.net;


import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.jdt.annotation.Nullable;

import de.tesd.net.sun.HttpImpl;


/** Verbindung zu einer JavaScript-EventSource.
	 * Nachrichten können mit {@link EventStream#send(String, String) send} verschickt werden.
	 * EventStream hält die Verbindung durch regelmäßige keepalive-Nachrichten offen.
	 * Wenn nicht gesendet werden kann, wird das {@link HttpImpl} geschlossen. */
	public class EventStream {
		
		
		public static interface CloseListener {
			void closed(EventStream sender);
		}
		
		
		protected static Timer timer = new Timer("EventStreamKeepAlives");
		
		
		/** Sendet ein "keepalive", damit die Verbindung offen bleibt. */
		public class KeepAlive extends TimerTask {
			@Override public void run() {
				send("keepalive", null);
			}
		}
		
		
		protected String name;
		protected Http http;
		protected boolean isAlive = true;
		protected KeepAlive keepAlive = new KeepAlive();
		protected ArrayList<CloseListener> closeListeners = new ArrayList<>();
		
		
		/** Erzeugt einen {@link EventStream}. */
		public EventStream(String name, Http http) {
			this.name = name;
			this.http = http;
			timer.schedule(keepAlive, 30000, 30000); // alle 30s
			send("hello", null);
		}
		
		
		/** Name des {@link EventStream}s. */
		public String getName() {
			return name;
		}
		
		
		public InetSocketAddress getRemoteAddress() {
			return http.getRemoteAddress();
		}
		
		
		public void addCloseListener(CloseListener closeListener) {
			closeListeners.add(closeListener);
		}
		
		
		public void removeCloseListener(CloseListener closeListener) {
			closeListeners.remove(closeListener);
		}
		
		
		public void close() {
			http.close();
			keepAlive.cancel();
			isAlive = false;
			for (CloseListener closeListener : closeListeners) {
				closeListener.closed(this);
			}
		}
		
		
		/** Sendet event/data an den Client.
		 * @param event Bezeichnung des Events (eine Zeile), nicht null!
		 * @param data Zusätzliche Daten (mehrzeilig), kann null sein.
		 * @return Konnte der Event zugestellt werden? */
		@SuppressWarnings("resource")
		public synchronized boolean send(String event, @Nullable String data) {
			if (!isAlive())
				return false;
			Http.LOG.finer("event: "+event);
			try {
				OutputStream out = http.getResponseBody();
				out.write("event: ".getBytes());
				out.write(event.getBytes());
				out.write('\n');
				if (data!=null)
					for (String line : data.split("\n|\r|\r\n")) {
						out.write("data: ".getBytes());
						out.write(line.getBytes());
						out.write('\n');
					}
				else
					out.write("data: \n".getBytes());
				out.write('\n');
				out.flush();
				return true;
			} catch (IOException e) {
//				logWarning(e);
				Http.LOG.finer("EventStream closed: "+name+" ("+e.getMessage()+")");
				close();
				return false;
			}
		}
		
		
		/** @return Ist dieser EventStream bis jetzt fehlerfrei?
		 * Das wird beim Versand von Daten geprüft, z.B. keepalives. */
		public boolean isAlive() {
			if (!isAlive) {
				return false;
			}
			return isAlive;
		}
		
		
		@Override public String toString() {
			return name;
		}


	}