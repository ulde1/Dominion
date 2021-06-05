package de.tesd.net;


import static java.net.HttpURLConnection.HTTP_NOT_FOUND;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;


public class FilesHandler extends Handler {
	
	
	protected String rootPath;
	
	
	public FilesHandler(String httpPath, String rootPath) {
		super(httpPath);
		this.rootPath = rootPath;
	}
	
	
	@Override public void get(Http http) throws IOException {
		http.consumeBody();
		try {
			String requestPath = http.getRequestURI().getPath();
			File file = new File(rootPath+requestPath.substring(getPath().length(), requestPath.length()));
			if (!file.exists()) {
				http.sendError("Seite nicht gefunden", "Sorry, die Seite "+requestPath+" gibt es nicht.", HTTP_NOT_FOUND);
			} else {
				http.sendFile(file, file.getName(), 86400);
			}
		} catch (URISyntaxException e) {
			http.sendError("Seite nicht gefunden", "Sorry, beim Parsen der URI ist dieser Fehler aufgetreten: "+e.getMessage(), HTTP_NOT_FOUND);
		}
	}
	
		
}