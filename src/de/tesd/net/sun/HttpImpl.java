package de.tesd.net.sun;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.sun.net.httpserver.HttpExchange;

import de.tesd.net.ContentType;
import de.tesd.net.Http;
import de.tesd.util.Dates;
import de.tesd.util.O;
import de.tesd.util.Streams;


/** Http ist ein erweitertes {@link HttpExchange} */
public class HttpImpl implements Http {


	/** Zur Kontrolle, dass alle Https auch wieder geschlossen werden. */
	private static final HashSet<HttpImpl> https  = new HashSet<HttpImpl>() {
		private static final long serialVersionUID = 7807411119256918482L;
		@Override public synchronized boolean add(HttpImpl http) {
			LOG.finest(() -> "open "+http+" ("+size()+")");
			return super.add(http);
		}
		@Override public synchronized boolean remove(@Nullable Object o) {
			boolean result = super.remove(o);
			LOG.finest(() -> "close "+o+" ("+size()+")");
			return result;
		};
	};
	
	
	static {
			// Warning für Https, die länger als 2 Minuten offen sind und keine EventStreams sind. 
		new Timer("CheckStillOpenHttps").schedule(new TimerTask() {
			@Override public void run() {
				try {
					long deadline = System.currentTimeMillis()-120000L;
					synchronized (https) {
						for (HttpImpl http : https) {
							if (http.creation<deadline && !http.getRequestUriOrErrorMessage().startsWith("/eventstream/")) {
								LOG.config("Http still open: "+http);
							}
						}
					}
				} catch (Throwable t) {
					LOG.log(Level.WARNING, t.getMessage(), t);
				}
			}
		}, 60000, 60000); // 1x pro Minute
	}
	
	
		// ========== Collector für Logging ==========
	
	
	
	public static final Collector<Map.Entry<String, List<String>>, StringBuffer, String> joinHeaders = new Collector<Map.Entry<String, List<String>>, StringBuffer, String> () {
		@Override public Supplier<StringBuffer> supplier() { return StringBuffer::new; }
		@Override public BiConsumer<StringBuffer, Entry<String, List<String>>> accumulator() {
			return (sb, e) -> {
				sb.append(e.getKey());
				sb.append(": ");
				sb.append(e.getValue().stream().collect(Collectors.joining(", ")));
				sb.append("\n");
			};
		}
		@Override public BinaryOperator<StringBuffer> combiner() { return StringBuffer::append; }
		@Override public Function<StringBuffer, String> finisher() { return sb -> {
				sb.delete(sb.length()-1, sb.length());
				return sb.toString();
			};
		}
		@SuppressWarnings("null")
		@Override public Set<Characteristics> characteristics() { return Collections.emptySet(); }
	};
	
	
		
	
		// ========== Http ==========
	
	
	protected final HttpExchange httpExchange;
	private long creation;
	protected boolean autoClose = true, closed = false;
	/** Der schon gelesene requestBody
	 * @see #getRequestBodyAsString()
	 * @see #getRequestBody() */
	protected @Nullable String requestBodyString = null;
	
	
	/** Erzeugt ein {@link HttpImpl} aus dem {@link HttpExchange}. */
	public HttpImpl(HttpExchange httpExchange) {
		this.httpExchange = httpExchange;
		this.creation = System.currentTimeMillis();
		https.add(this);
			// URI
		LOG.fine(() -> toString());
			// Headers
		LOG.finer(() -> httpExchange.getRequestHeaders().entrySet().stream().collect(joinHeaders));
			// Body
		LOG.finest(() -> {
			try {
				return getRequestBodyAsString();
			} catch (IOException e) {
				Http.logWarning(e);
				return e.getMessage();
			}
		});
	}
	
	
	@Override public boolean isAutoClose() { return autoClose; }
	@Override public void setAutoClose(boolean value) { autoClose = value; }
	@Override public void noAutoClose() { autoClose = false; }


		// ========== Http: Request ==========
	
	
	@Override public String getRequestMethod() { return httpExchange.getRequestMethod(); }
	
	
	@Override public URI getRequestURI() { return httpExchange.getRequestURI(); }
	
	
	@Override public @Nullable String getFirstRequestHeaderValue(String name) {
		return httpExchange.getRequestHeaders().getFirst(name);
	}
	
	
	@Override public @Nullable ContentType getRequestContentType() {
		String contentTypeHeader = getFirstRequestHeaderValue("Content-Type");
		if (contentTypeHeader==null)
			return null;
		String[] parts = contentTypeHeader.split(";\\s*");
		String mimeType = parts[0];
		String charSet = O.orNull(Arrays.stream(parts)
			.filter(p -> p.toLowerCase().startsWith("charset"))
			.map(p -> p.split("\\s*=\\s*"))
			.filter(kv -> kv.length>1)
			.map(kv -> kv[1])
			.findFirst());
		return new ContentType(mimeType, charSet);
	}
	
	
	@Override public String[] getCookies() {
		List<String> cookies = httpExchange.getRequestHeaders().get("Cookie");
		return cookies==null ? new String[0] : cookies.stream().flatMap(s -> Arrays.stream(s.split(";\\s*"))).toArray(String[]::new);
	}
	
	
	@Override public InputStream getRequestBody() {
		if (requestBodyString!=null)
			return Streams.streamFromString(requestBodyString);
		else
			return httpExchange.getRequestBody();
	}
	
	
	@SuppressWarnings("resource")
	@Override public String getRequestBodyAsString() throws IOException {
		if (requestBodyString==null) {
			ContentType contentType = getRequestContentType();
			String charset = contentType==null ? null : contentType.getCharset();
			requestBodyString = Streams.readToString(getRequestBody(), charset).toString();
		}
		return (@NonNull String) requestBodyString;
	}
	
	
//	@Override public Map<String, String> getQueryBodyMap() throws IOException, URISyntaxException {
//		Map<String, String> result = getQueryMap();
//		result.putAll(getBodyMap());
//		return result;
//	}
	
	
		// ========== Http: Response==========

	
	
	@Override public void addResponseHeader(String key, String value) {
		httpExchange.getResponseHeaders().add(key, value);
	}

	
	@Override public void setResponseBody(byte[] bytes) throws IOException {
		try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
			setResponseBody(inputStream);
		}
	}


	@SuppressWarnings("resource")
	@Override public void setResponseBody(InputStream inputStream) throws IOException {
		Streams.copy(inputStream, httpExchange.getResponseBody());
	}


	@Override public void setResponseBody(String message) throws IOException {
		setResponseBody(message.getBytes("UTF-8"));
	}


	@Override public boolean isClosed() {
		return closed;
	}
	
	
	@Override public void sendStream(InputStream inputStream, @Nullable String fileName, long lastModified, int cacheSeconds, int returnCode, String @Nullable [] headers) throws IOException {
		addResponseHeader("Content-Type", Http.guessContentType(inputStream, fileName));
		addResponseHeader("Cache-Control", "max-age="+cacheSeconds+", public");
		if (lastModified>=0) {
			addResponseHeader("Last-Modified", java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME.format(Dates.toLocalDateTime(new Date(lastModified)).atZone(ZoneOffset.systemDefault())));
		}
		addResponseHeader("Expires", java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME.format(Dates.toLocalDateTime(new Date(System.currentTimeMillis()+cacheSeconds*1000L)).atZone(ZoneOffset.systemDefault()))); 
		if (fileName!=null) {
			addResponseHeader("Content-Disposition", "attachment; filename=\""+fileName+"\"");
		}
		if (headers!=null) {
			for (int i = 0; i<headers.length-1; i+=2) {
				addResponseHeader(headers[i], headers[i+1]);
			}
		}
		sendResponseHeaders(returnCode, 0);
		setResponseBody(inputStream);
		close();
	}
	
	
	@Override public String toString() {
		return getRequestMethod()+" "+getRemoteAddress()+" "+getRequestURI();
	}
	
	
		// ========== HttpExchange ==========
	
	
//	@Override public Headers getRequestHeaders() { return httpExchange.getRequestHeaders(); }
//	@Override public Headers getResponseHeaders() {
//		return httpExchange.getResponseHeaders();
//	}
//	@Override public HttpContext getHttpContext() { return httpExchange.getHttpContext(); }
	@Override public void close() {
		LOG.fine(() -> "close("+this+")");
		closed = true;
		httpExchange.close();
		https.remove(this);
	}
	@Override public OutputStream getResponseBody() { return httpExchange.getResponseBody(); }
	@Override public void sendResponseHeaders(int returnCode, long responseLength) throws IOException {
		addResponseHeader("Referrer-Policy", "same-origin");
		httpExchange.sendResponseHeaders(returnCode, responseLength);
	}
	@Override public InetSocketAddress getRemoteAddress() { return httpExchange.getRemoteAddress(); }
//	@Override public int getResponseCode() { return httpExchange.getResponseCode(); }
//	@Override public InetSocketAddress getLocalAddress() { return httpExchange.getLocalAddress(); }
//	@Override public String getProtocol() { return httpExchange.getProtocol(); }
//	@Override public Object getAttribute(String name) { return httpExchange.getAttribute(name); }
//	@Override public void setAttribute(String name, Object value) { httpExchange.setAttribute(name, value); }
//	@Override public void setStreams(InputStream i, OutputStream o) { httpExchange.setStreams(i, o); }
//	@Override public HttpPrincipal getPrincipal() { return httpExchange.getPrincipal(); }


}
