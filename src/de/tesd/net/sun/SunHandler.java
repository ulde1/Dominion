package de.tesd.net.sun;


import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import de.tesd.net.Handler;


public class SunHandler implements HttpHandler {


	private Handler handler;


	public SunHandler(Handler handler) {
		this.handler = handler;
	}


	public HttpContext bindTo(HttpServer httpServer) {
		return httpServer.createContext(handler.getPath(), this);
	}


	@SuppressWarnings("null") @Override public void handle(HttpExchange httpExchange) {
		handler.handle(new HttpImpl(httpExchange));
	}

}