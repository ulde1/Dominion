package de.tesd.net;


import org.eclipse.jdt.annotation.Nullable;


public class ContentType {


	String mimeType;
	@Nullable String charSet;


	public ContentType(String mimeType, @Nullable String charSet) {
		this.mimeType = mimeType;
		this.charSet = charSet;
	}


	public ContentType(String contentType) {
		String[] strings = contentType.split("\\s*;\\s*");
		mimeType = strings[0];
		for (String string : strings) {
			if (string.startsWith("charset=")) {
				charSet = string.substring(8);
			}
		}
	}


	public String getMimeType() {
		return mimeType;
	}


	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}


	public @Nullable String getCharset() {
		return charSet;
	}


	public void setCharSet(String charSet) {
		this.charSet = charSet;
	}

}
