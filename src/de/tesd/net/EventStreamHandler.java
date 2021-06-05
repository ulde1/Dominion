package de.tesd.net;


import static java.net.HttpURLConnection.HTTP_OK;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.BiFunction;

import de.tesd.util.Strings;


public abstract class EventStreamHandler extends Handler {
	
	
	public final static BiFunction<String, Http, EventStream> NEW_EVENTSTREAM = (path, http) -> new EventStream(getStreamName(path, http), http);


	public static String getStreamName(String path, Http http) {
		try {
			URI uri = http.getRequestURI();
			String uriPath = uri.getPath();
			String uriQuery = uri.getQuery();
			if (Strings.compareLeft(uriPath, path)==0)
				uriPath = uriPath.substring(path.length());
			else
				System.out.println(path+" :: "+uriPath);
			if (uriQuery!=null)
				uriPath = uriPath+"?"+uriQuery;
			return uriPath;
		} catch (URISyntaxException e) {
			return e.getMessage();
		}
	}


	protected BiFunction<String, Http, EventStream> generator;


	public EventStreamHandler(String path, BiFunction<String, Http, EventStream> generator) {
		super(path);
		this.generator = generator;
	}


	public EventStreamHandler(String path) {
		this(path, NEW_EVENTSTREAM);
	}
	

	public void setGenerator(BiFunction<String, Http, EventStream> generator) {
		this.generator = generator;
	}


	/** @deprecated Better use {@link #setGenerator(BiFunction)} */
	@Deprecated
	public void onNewEventStream(Http http, EventStream eventStream) {
	}

	

		// ========== Handler ==========

	
	@Override protected void get(Http http) throws IOException {
		http.addResponseHeader("Content-Type", "text/event-stream; charset=utf-8");
		if (http instanceof de.tesd.net.sun.HttpImpl)
			http.addResponseHeader("Transfer-Encoding", "chunked");
//		headers.add("Cache-Control", "no-cache");
		http.sendResponseHeaders(HTTP_OK, 0);
		http.noAutoClose();
		EventStream eventStream = generator.apply(path, http);
		onNewEventStream(http, eventStream);
	}
	
	
}