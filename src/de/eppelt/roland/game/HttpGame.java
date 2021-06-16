package de.eppelt.roland.game;

import static java.net.HttpURLConnection.HTTP_NOT_IMPLEMENTED;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.Nullable;

import com.sun.net.httpserver.HttpServer;

import de.tesd.collection.HashList;
import de.tesd.collection.TemporaryHashMap;
import de.tesd.net.EventStream;
import de.tesd.net.FilesHandler;
import de.tesd.net.Handler;
import de.tesd.net.Http;
import de.tesd.net.sun.SunHandler;
import de.tesd.util.Loggers;
import de.tesd.util.O;
import de.tesd.util.Strings;


public class HttpGame<INSTANCE extends HttpGameInstance<INSTANCE, CLIENT, PLAYER>, CLIENT extends HttpGameClient<INSTANCE, CLIENT, PLAYER>, PLAYER extends GamePlayer<INSTANCE, CLIENT, PLAYER>> implements Game<INSTANCE, CLIENT, PLAYER>, Loggers {


	public final static Logger LOG = Logger.getLogger(HttpGame.class.getName());
	@Override public Logger logger() { return LOG; }
	
	
	public static @Nullable String getCookie(Http http, String name) {
		return O.nn(O.orNull(Arrays.stream(http.getCookies()).filter(s -> s.startsWith(name + "=")).findFirst()),
				s -> s.substring(name.length()+1));
	}
	
	public static void setCookie(Http http, String name, String value) {
		http.addResponseHeader("Set-Cookie", name+"=" +value+ ";HttpOnly");
	}
	
	
	public static void unsetCookie(Http http, String name) {
		http.addResponseHeader("Set-Cookie", name+"=deleted;HttpOnly; expires=Thu, 01 Jan 1970 00:00:00 GMT");
	}
	
	
	public class HTMLHandler extends Handler {


		public HTMLHandler(String path) {
			super(path);
		}
		
		
		void handle(Http http, HashList<String, String> map) throws IOException {
//			Http.LOG.finest(http.getRequestBodyAsString());
			String session = map.getFirstOrNull("session");
			@Nullable CLIENT client = clients.getOrNull(session);
			if (client==null) {
				String name = O.nn(O.nullable(map.getFirstOrNull("name")), Strings::html);
				if (name==null) {
					http.sendResponse(getLoginHtml());
				} else if (openInstances.isEmpty()) {
					INSTANCE instance = instanceSupplier.apply(HttpGame.this);
					PLAYER player = playerSupplier.apply(instance, name);
					client = clientSupplier.apply(http.getRemoteAddress(), player);
				} else {
					String instanceID = O.nn(O.nullable(map.getFirstOrNull("instance")), Strings::html);
					if (instanceID==null) {
						http.sendResponse(gamesHtml(name));
					} else if ("New Instance".equals(instanceID)) {
						INSTANCE instance = instanceSupplier.apply(HttpGame.this);
						PLAYER player = playerSupplier.apply(instance, name);
						client = clientSupplier.apply(http.getRemoteAddress(), player);
					} else {
						INSTANCE instance = O.or(openInstances.getOrNull(instanceID), () -> instanceSupplier.apply(HttpGame.this));
						@Nullable PLAYER player = O.orNull(instance.stream()
								.map(CLIENT::getPlayer)
								.filter(p -> name.equals(p.getName()))
								.findFirst());
						if (player==null) { // Neuer Spieler
							player = playerSupplier.apply(instance, name);
						}
						client = clientSupplier.apply(http.getRemoteAddress(), player);
					}
				}
			}
				// hier ist entweder client!=null oder http schon beantwortet
			if (client!=null) {
				playerHandler.handle(map, http, client);
				if (!http.isClosed()) {
					if (map.containsKey("ajax")) {
						http.sendResponse(client.gameBodyJSON());
					} else {
						http.sendResponse(client.gameHtml());
					}
				}
			}
		}
		
		
		@Override protected void get(Http http) throws Exception {
			http.consumeBody();
			handle(http, http.getQueryList());
		}


		@Override protected void post(Http http) throws Exception {
			String contentType = http.getFirstRequestHeaderValue("Content-Type");
			if (contentType==null || !contentType.startsWith("application/x-www-form-urlencoded")) {
				http.sendError("Falscher ContentType", "Falscher ContentType \"" + contentType + "\".",
						HTTP_NOT_IMPLEMENTED);
			} else {
				handle(http, http.getBodyList());
			}
		}

		
	}


	public class EventStreamHandler extends de.tesd.net.EventStreamHandler {


		public EventStreamHandler(String root) {
			super(root);
			setGenerator((path, http) -> { 
				EventStream result = new EventStream(getStreamName(path, http), http);
				try {
					String session = http.getQueryList().getFirstOrNull("session");
					HttpGameClient<INSTANCE, CLIENT, PLAYER> client = clients.getOrNull(session);
					if (client!=null) {
						client.eventStreams.clear();
						client.eventStreams.register(result);
					}
				} catch (URISyntaxException e) {
					info(e);
				}
				return result;
			});
		}


	}
	

	String gameName;
	String headerHtml = "";
	String infoHtml = "";
	TemporaryHashMap<@Nullable String, INSTANCE> openInstances = new TemporaryHashMap<>(600000); // 10 Minuten
	TemporaryHashMap<@Nullable String, CLIENT> clients = new TemporaryHashMap<>(3600000); // 1 Stunde
	Function<HttpGame<INSTANCE, CLIENT, PLAYER>, INSTANCE> instanceSupplier;
	BiFunction<INSTANCE, String, PLAYER> playerSupplier;
	BiFunction<InetSocketAddress, PLAYER, CLIENT> clientSupplier;
	HttpGameClientHandler<INSTANCE, CLIENT, PLAYER> playerHandler;
	HttpServer httpServer;
	
	
	
	@SuppressWarnings("null")
	public HttpGame(String gameName, int port, Function<HttpGame<INSTANCE, CLIENT, PLAYER>, INSTANCE> instanceSupplier, 
			BiFunction<INSTANCE, String, PLAYER> playerSupplier, 
			BiFunction<InetSocketAddress, PLAYER, CLIENT> clientSupplier,
			HttpGameClientHandler<INSTANCE, CLIENT, PLAYER> playerHandler) throws IOException {
		this.gameName = gameName;
		this.instanceSupplier = instanceSupplier;
		this.playerSupplier = playerSupplier;
		this.clientSupplier = clientSupplier;
		this.playerHandler = playerHandler;
		if (port>=0) {
			httpServer = HttpServer.create(new InetSocketAddress(port), -1);
			new SunHandler(new HTMLHandler("/")).bindTo(httpServer);
			new SunHandler(new EventStreamHandler("/e/")).bindTo(httpServer);
			new SunHandler(new FilesHandler("/p/", "p/")).bindTo(httpServer);
			httpServer.setExecutor(null); // creates a default executor
		}
	}
	
	
	public void addCssLink(String cssLink) {
		headerHtml = headerHtml+"<link rel=\"stylesheet\" href=\""+cssLink+"\"/>\n";
	}
	
	
	public void addInfoHtml(String html) {
		infoHtml = infoHtml+html+"\n";
	}
	
	
	public void start() {
		httpServer.start();
	}
	
	
	public void appendHead(StringBuffer sb) {
		sb.append("<!DOCTYPE html>\n");
		sb.append("<html lang=\"de\">\n<head>\n");
		sb.append("<meta charset=\"utf-8\" />\n");
		sb.append("<title>");
		sb.append(gameName);
		sb.append("</title>\n");
		sb.append(headerHtml);
		sb.append("</head>\n");
	}
	
	public String getLoginHtml() {
		StringBuffer sb = new StringBuffer();
		appendHead(sb);
		sb.append("<body>\n<h1>");
		sb.append(gameName);
		sb.append("</h1>"
			+ "<form method=\"post\" action=\"?\">\n"
			+ "<table><tr>"
			+ "<td>Name: <input type=\"text\" name=\"name\" autofocus /></td></tr><tr>"
			+ "<td style=\"text-align: center\"><button type=\"submit\" accesskey=\"m\"><u>M</u>itspielen!</button></td></tr></table></form>\n");
		sb.append(infoHtml);
		sb.append("\n</body>\n</html>");
		return sb.toString();
	}


	public String gamesHtml(String name) {
		StringBuffer sb = new StringBuffer();
		appendHead(sb);
		sb.append("<body>\n");
		sb.append("<h1>Welches Spiel?</h1>\n");
		sb.append("<form method=\"post\" action=\"?\">\n<input type=\"hidden\" id=\"name\" name=\"name\" value=\"");
		sb.append(name);
		sb.append("\" />\n<p>Bitte wähle dein Spiel aus:</p>\n");
		sb.append("<button type=\"submit\" name=\"instance\" value=\"New Instance\" autofocus accesskey=\"n\"><u>N</u>eues Spiel</button><br />\n");
		openInstances.valueStream().forEach(instance -> {
			sb.append("<button type=\"submit\" name=\"instance\" value=\"");
			sb.append(instance.getID());
			sb.append("\" >");
			sb.append(instance);
			sb.append("</button><br />\n");
		});
		sb.append("</form>\n");
		sb.append("</body>\n</html>");
		return sb.toString();
	}
	
	
	public void openInstanceForNewPlayers(INSTANCE  instance) {
		openInstances.put(instance.getID(), instance);
	}
	
	
	public boolean isInstanceOpenForNewPlayers(INSTANCE instance) {
		return openInstances.containsValue(instance);
	}
	

	public void closeInstanceForNewPlayers(INSTANCE instance) {
		openInstances.remove(instance.getID());
	}

	@Override public Stream<CLIENT> stream() {
		return clients.valueStream();
	}


	@Override public void put(String session, CLIENT client) {
		clients.put(session, client);		
	}

	public void remove(String session) {
		clients.remove(session);
	}

}