package de.tesd.net;


import java.io.IOException;
import java.net.URI;

import de.tesd.collection.HashMap;


public class Handler {
	
	
	/** @param uri {@link URI}
	 * @return {@link URI#getPath() Pfad} der {@link URI} ohne führendes "/". Garantiert nicht null. */
	public static String normalizePath(URI uri) {
		String result = uri.getPath();
		if (result==null)
			result = "";
		if (result.startsWith("/"))
			result = result.substring(1);
		return result;
	}
	
	
	/** @param uri {@link URI}
	 * @return {@link URI#getQuery() Pfad} der {@link URI} ohne führendes "?". Garantiert nicht null. */
	public static String normalizeQuery(URI uri) {
		String result = uri.getRawQuery();
		if (result==null)
			result = "";
		if (result.startsWith("?"))
			result = result.substring(1);
		return result;
	}
	
	
		// ========== Handler ==========
	
	
	protected String path;
	protected HashMap<String, MethodHandler> methods = new HashMap<>();
	/** Länge von {@link #getPath()} ohne führendes "/", aber mit abschließendem "/". */
	protected final int rootPathLength; 
	
	
	
	
	public Handler(String path) {
		this.path = path;
		rootPathLength = path.length()-(path.startsWith("/") ? 1 : 0)+(path.endsWith("/") ? 0: 1);
		register("GET", this::get);
		register("POST", this::post);
		register("HEAD", this::head);
		register("PUT", this::put);
		register("PATCH", this::patch);
		register("DELETE", this::delete);
		register("TRACE", this::trace);
		register("OPTIONS", this::options);
		register("CONNECT", this::connect);
	}
	
	
	public Handler(String path, String method1, MethodHandler methodHandler1) {
		this(path);
		register(method1, methodHandler1);			
	}
	
	
	public Handler(String path, String method1, MethodHandler methodHandler1, String method2, MethodHandler methodHandler2) {
		this(path);
		register(method1, methodHandler1);			
		register(method2, methodHandler2);			
	}
	
	
	public Handler(String path, String method1, MethodHandler methodHandler1, String method2, MethodHandler methodHandler2, String method3, MethodHandler methodHandler3) {
		this(path);
		register(method1, methodHandler1);			
		register(method2, methodHandler2);			
		register(method3, methodHandler3);			
	}
	
	
	public String getPath() { return path; }


	public void register(String method, MethodHandler methodHandler) {
		methods.put(method, methodHandler);
	}
	
	
	public void handle(Http http) {
		try {
            String requestMethod = http.getRequestMethod();
			MethodHandler methodHandler = methods.getOrNull(requestMethod);
			if (methodHandler==null)
				http.wrongMethod();
			else  if (!http.getRequestURI().getPath().startsWith(getPath()))
				http.pageNotFoundError();
			else 
				methodHandler.handle(http);
		} catch (Throwable t) {
			Http.logWarning(t);
			try {
				if (t instanceof IllegalArgumentException) {
					http.badRequestError(t.getMessage());
				} else {
					http.internalError(t);
				}
			} catch (IOException e) {
				http.close();
			}
		} finally {
			if (!http.isClosed() && http.isAutoClose())
				http.close();
		}
	}
	
	
	protected void get(Http http) throws Exception { http.wrongMethod(); }		
	protected void post(Http http) throws Exception { http.wrongMethod(); }		
	protected void head(Http http) throws Exception { http.wrongMethod(); }		
	protected void put(Http http) throws Exception { http.wrongMethod(); }		
	protected void patch(Http http) throws Exception { http.wrongMethod(); }		
	protected void delete(Http http) throws Exception { http.wrongMethod(); }		
	protected void trace(Http http) throws Exception { http.wrongMethod(); }		
	protected void options(Http http) throws Exception { http.wrongMethod(); }		
	protected void connect(Http http) throws Exception { http.wrongMethod(); }


}