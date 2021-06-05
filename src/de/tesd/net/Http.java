package de.tesd.net;


import static java.net.HttpURLConnection.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.sun.net.httpserver.Headers;

import de.tesd.collection.HashList;
import de.tesd.util.Streams;
import de.tesd.util.Strings;


public interface Http {


	public static Logger LOG = Logger.getLogger(Http.class.getName());
	public static void log(Logger log, Level level, Throwable e) { log.log(level, e.getClass().getName()+": "+e.getMessage(), e); }
	public static void logInfo(Logger log, Throwable e) { log(log, Level.INFO, e); }
	public static void logWarning(Logger log, Throwable e) { log(log, Level.WARNING, e); }
	public static void logInfo(Throwable e) { logInfo(LOG, e); }
	public static void logWarning(Throwable e) { logWarning(LOG, e); }
	
	
	/** sammelt String[]s in eine Map */
	public final static Collector<String[], ?, Map<String, String>> TO_MAP = Collectors.toMap(l->l[0], l-> l.length>1 ? l[1] : "");
	
	
	public static String guessContentType(InputStream inputStream, @Nullable String fileName) {
		String result;
		try {
			result = URLConnection.guessContentTypeFromStream(inputStream);
		} catch (IOException e) {
			result = null;
			LOG.log(Level.FINE, e.getMessage(), e);
		}
		if (result==null && fileName!=null) {
			if (fileName.endsWith(".css")) { // ugly hacks for MSWindows
				result = "text/css";
			} else if (fileName.endsWith(".js")) {
				result = "application/javascript";
			} else if (fileName.endsWith(".ttf")) {
				result = "font/ttf";
			} else if (fileName.endsWith(".svg")) {
				result = "image/svg+xml";
			}
		} else if ("application/xml".equals(result) && fileName!=null && fileName.endsWith(".svg")) {
			result = "image/svg+xml";
		}
		if (result==null) {
			result = "application/octet-stream";
		}
		return result;
	}
	
	
	/** {@link Handler} schließt das {@link Http} nach Abarbeitung. */
	boolean isAutoClose();


	/** Soll {@link Handler} das {@link Http} nach Abarbeitung schließen? */
	void setAutoClose(boolean value);


	/** {@link Handler} soll das {@link Http} nach Abarbeitung offen lassen. */
	void noAutoClose();
	
	
		// Request


	String getRequestMethod();


	URI getRequestURI() throws URISyntaxException;
	
	
	default String getRequestUriOrErrorMessage() {
		try {
			return getRequestURI().toString();
		} catch (URISyntaxException e) {
			logWarning(e);
			return e.getMessage(); 
		}
	}


	@Nullable
	String getFirstRequestHeaderValue(String name);


	/**  @return Liste aller Cookies, d.h. {@link #getRequestHeaders()}.{@link Headers#get get("Cookie")} */
	String[] getCookies();


	/** Zerlegt {@link #getRequestURI()}.{@link URI#getQuery()} in key1=value1&key2=value=…-Paare.  Kann nicht mit gleichnamigen Keys umgehen!
	 * @return {@link Map} der Key/Value-Paare.
	 * @deprecation Besser {@link #getQueryList} verwenden. */
	@Deprecated
	default Map<String, String> getQueryMap() throws URISyntaxException {
		String query = getRequestURI().getQuery();
		if (query==null)
			query = "";
		return Arrays.asList(query.split("&")).stream().map(s -> s.split("=")).collect(TO_MAP);
	}


	/** Zerlegt {@link #getRequestURI()}.{@link URI#getQuery()} in key1=value1&key2=value=…-Paare.
	 * @return {@link HashList} der Key/Value-Paare. */
	default HashList<String, String> getQueryList() throws URISyntaxException {
		String query = getRequestURI().getQuery();
		if (query==null)
			query = "";
		return Arrays.asList(query.split("&")).stream().map(s -> s.split("=")).collect(HashList.FROM_STRING2);
	}


	InputStream getRequestBody() throws UnsupportedOperationException, IOException;


	@Nullable ContentType getRequestContentType();


	/** @return {@link #getRequestBody()} als String (charset wird beachtet) */
	String getRequestBodyAsString() throws IOException;


	/** Zerlegt {@link #getRequestBody()} in key1=value1&key2=value=…-Paare. Kann nicht mit gleichnamigen Keys umgehen!
	 * @return {@link Map} der Key/Value-Paare.
	 * @deprecation Besser {@link #getBodyList} verwenden. */
	@Deprecated
	default Map<String, String> getBodyMap() throws IOException {
		return Arrays.asList(getRequestBodyAsString().split("&")).stream()
			.map(s -> Strings.urlDecode(s).split("=", 2))
			.collect(TO_MAP);
	}
	
	
	/** Zerlegt {@link #getRequestBody()} in key1=value1&key2=value=…-Paare.
	 * @return {@link HashList} der Key/Value-Paare. */
	default HashList<String, String> getBodyList() throws IOException {
		return Arrays.asList(getRequestBodyAsString().split("&")).stream()
			.map(s -> Strings.urlDecode(s).split("=", 2))
			.collect(HashList.FROM_STRING2);
	}


//	/** @return Die Zusammenfassung aus {@link #getQueryMap()} und {@link #getBodyMap()}. */
//	Map<String, String> getQueryBodyMap() throws IOException, URISyntaxException;


	/** Liest den {@link #getRequestBody()} komplett und verwirft ihn. */
	default void consumeBody() throws IOException {
		try (InputStream requestBody = getRequestBody()) {
			Streams.copy(requestBody, Streams.nullOutputStream);
		}
	}
	
	
		// Response
	
	
	void addResponseHeader(String key, String value);	


	/** Verschickt eine {@link java.net.HttpURLConnection#HTTP_SEE_OTHER HTTP_SEE_OTHER}-Antwort.
	 * @param location Ziel der Umleitung */
	default void sendForward(String location) throws IOException {
		@SuppressWarnings("null") @NonNull String utf8Location = Strings.utf8Encode(location);
		addResponseHeader("location", utf8Location);
		sendResponse("<html><head><meta charset=\"UTF-8\" /><title>Weiterleitung</title></head><body>\n" + 
			"<h1>Weiterleitung</h1>\n" + 
			"<p>Bitte gehen Sie weiter auf <a href=\""+Strings.urlEncode(location)+"\">"+utf8Location+"</a>.</p>\n" + 
			"</body></html>", HTTP_SEE_OTHER);
	}
	
	
	/** Setzt den ResponseHeader auf returnCode und verschickt die HTML-Seite. */
	default void sendResponse(String html, int returnCode) throws IOException {
		LOG.fine(() -> "sendResponse("+returnCode+": "+Strings.limit(html, 240, "…")+")");
		addResponseHeader("Content-Type", "text/html; charset=UTF-8");
		byte[] bytes = html.getBytes("UTF-8");
		sendResponseHeaders(returnCode, bytes.length);
		setResponseBody(bytes);
		close();
	}
	
	
	/** Verschickt die HTML-Seite mit HTTP Status-Code 200: OK */
	default void sendResponse(String html) throws IOException {
		sendResponse(html, HTTP_OK);
	}
	
	
	/** Verschickt Status-Code mit leerem Body. */
	default void sendEmptyResponse(int returnCode) throws IOException {
		addResponseHeader("Content-Type", "text/html; charset=UTF-8");
		sendResponseHeaders(returnCode, -1);
		close();
	}
	
	
	/** Verschickt eine HTTP-Response als einfaches HTML-Dokument. */
	default void sendError(String title, String message, int returnCode) throws IOException {
		String sTitle = Strings.xml(title);
		String sMessage = Strings.xml(message).replaceAll("(&#10;|&#13;|&#13;&#10;)", "<br/>\n").replaceAll("&#0?9;", "&emsp;");
		String html = "<!DOCTYPE html>\n<html lang=\"de\">\n<head>\n<meta charset=\"UTF-8\" />\n<title>"+sTitle+"</title>\n</head>\n<body>\n<h1>"+sTitle+"</h1>\n<p>"+sMessage
			+"</p>\n<p><a href=\"/\">Zurück</a></p></body></html>";
		sendResponse(html, returnCode);
	}
	
	
	/** Versendet die Datei als HTTP-Response.
	 * @param inputStream zu versendender {@link InputStream}
	 * @param fileName Name für den Client, erzeugt Header "Content-Disposition: attachment; filename=…"
	 * @param lastModified {@link File#lastModified()}, erzeugt Header "Last-Modified:…", falls &gt;0
	 * @param cacheSeconds Sekunden, die der Client die Datei cachen soll, erzeugt Header "Expires:…"
	 * @param returnCode Z.B. {@link java.net.HttpURLConnection#HTTP_OK} (Das hier war ein Versuch, gleichzeitig {@link java.net.HttpURLConnection#HTTP_SEE_OTHER} 
	 * und ein Attachment zu verschicken. Der Browser hat aber das Attachment ignoriert. )
	 * @param headers weitere Response-Headers, jeweils key und value abwechselnd */
	void sendStream(InputStream inputStream, @Nullable String fileName, long lastModified, int cacheSeconds, int returnCode, String @Nullable[] headers) throws IOException;
	
	
	/** Versendet die Datei als HTTP-Response.
	 * @param file zu versendendes {@link File}
	 * @param fileName Name für den Client 
	 * @param cacheSeconds Sekunden, die der Client die Datei cachen soll*/
	default void sendFile(File file, @Nullable String fileName, int cacheSeconds) throws FileNotFoundException, IOException {
		try (FileInputStream fileInputStream = new FileInputStream(file)) {
			sendStream(fileInputStream, fileName, file.lastModified(), cacheSeconds, java.net.HttpURLConnection.HTTP_OK, null);
		}
	}
	
	
	boolean isClosed();


//	Headers getResponseHeaders();


//	HttpContext getHttpContext();


	void close();


//	OutputStream getResponseBody();
	
	
	OutputStream getResponseBody();
	
	
	void setResponseBody(byte[] bytes) throws IOException;


	void setResponseBody(InputStream inputStream) throws IOException;


	void setResponseBody(String message) throws UnsupportedEncodingException, IOException;


	void sendResponseHeaders(int returnCode, long responseLength) throws IOException;


	InetSocketAddress getRemoteAddress();
	

	/** Liefert Fehlermeldung 400. */
	default void badRequestError(String info) throws IOException {
		sendError("Fehler in der Anfrage", Strings.sentence("\n", true, "Ihre Anfrage kann wegen Fehlern nicht verarbeitet werden.", info), HTTP_BAD_REQUEST);		
	}
	
	
	/** Liefert Fehlermeldung 404. */
	default void pageNotFoundError(String path) throws IOException {
		sendError("Seite nicht gefunden", "Sorry, die Seite "+path+" gibt es nicht.", HTTP_NOT_FOUND);
	}
	
	
	/** Liefert Fehlermeldung 404. */
	default void pageNotFoundError() throws IOException {
		try {
			pageNotFoundError(getRequestURI().getPath());
		} catch (URISyntaxException e) {
			internalError(e);
		}
	}
	
	
	/** Liefert Fehlermeldung 405. */
	default void wrongMethod() throws IOException {
		sendError("Falsche Methode", "Sorry, die Methode "+getRequestMethod()+" wird leider nicht unterstützt.", HTTP_BAD_METHOD);
	}
	
	
	/** Liefert Fehlermeldung 500. */
	default void internalError(Throwable t) throws IOException {
		sendError("Interner Fehler", t.getClass().getName()+": "+t.getMessage(), HTTP_INTERNAL_ERROR);
	}


}