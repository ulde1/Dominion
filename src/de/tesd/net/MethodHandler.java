package de.tesd.net;

/** Verarbeitet eine Anfrage. Das {@link Http} muss nicht geschlossen werden. */
@FunctionalInterface
public interface MethodHandler {


	/** @return HttpExchange soll geschlossen werden. */
	public void handle(Http http) throws Exception;
}