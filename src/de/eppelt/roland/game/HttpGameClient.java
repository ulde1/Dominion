package de.eppelt.roland.game;

import java.net.InetSocketAddress;
import java.util.logging.Logger;

import de.tesd.net.EventStreams;
import de.tesd.util.Loggers;
import de.tesd.util.Password;
import de.tesd.util.Strings;


public abstract class HttpGameClient<INSTANCE extends HttpGameInstance<INSTANCE, CLIENT, PLAYER>, CLIENT extends HttpGameClient<INSTANCE, CLIENT, PLAYER>, PLAYER extends GamePlayer<INSTANCE, CLIENT, PLAYER>> implements GameClient<INSTANCE, CLIENT, PLAYER>, Loggers {


	public final static Logger LOG = Logger.getLogger(HttpGameClient.class.getName());
	@Override public Logger logger() { return LOG; }
	
	
	/**
	 * @param sb
	 * @param idName
	 * @param accessKey nur {@link Character#isLetterOrDigit(char)} wird berücksichtigt
	 * @param text
	 * @param ajax
	 */
	public static void button(StringBuffer sb, String idName, char accessKey, String text, boolean ajax) {
		sb.append("<button id=\"");
		sb.append(idName);
		sb.append("\" name=\"");
		sb.append(idName);
		sb.append("\" type=\"");
		if (ajax) {
			sb.append("button\" onClick=\"submitButton(this)");
		} else {
			sb.append("submit");
		}
		if (Character.isLetterOrDigit(accessKey)) {
			sb.append("\" accesskey=\"");
			sb.append(accessKey);
		}
		sb.append("\">");
		if (Character.isLetterOrDigit(accessKey)) {
			sb.append(text.replaceAll("(["+String.valueOf(accessKey)+String.valueOf(accessKey).toUpperCase()+"])", "<u>$1</u>"));
		} else {
			sb.append(text);
		}
		sb.append("</button>");
	}


	public static void button(StringBuffer sb, String name, String html, int value) {
		sb.append("<button name=\"");
		sb.append(name);
		sb.append("\" type=\"button\" onClick=\"submitButton(this)\" value=\"");
		sb.append(value);
		sb.append("\">");
		sb.append(html);
		sb.append("</button>");
	}
	
	
		// ========== Client ==========
	
	
	InetSocketAddress address;
	PLAYER player;
	public String session;
	EventStreams eventStreams = new EventStreams();
	boolean hasInputField = false;


	public HttpGameClient(InetSocketAddress address, PLAYER player) {
		this.address = address;
		this.player = player;
		this.session = Password.generateNewPassword(17);
		@SuppressWarnings("unchecked") CLIENT client = (CLIENT) this;
		player.getGame().put(session, client);
		player.getInstance().updatePlayersExcept(this);
	}
	
	
	@Override public INSTANCE getInstance() {
		return player.getInstance();
	}


	public InetSocketAddress getAddress() {
		return address;
	}

	
	@Override public PLAYER getPlayer() {
		return player;
	}
	
	
	@Override public HttpGame<INSTANCE, CLIENT, PLAYER> getGame() {
		return getInstance().getGame();
	}
	
	
	public String getSession() {
		return session;
	}
	

	public boolean hasInputField() {
		return hasInputField;
	}


	public void setHasInputField(boolean hasInputField) {
		this.hasInputField = hasInputField;
	}


	public void appendSession(StringBuffer sb) {
		sb.append("<input type=\"hidden\" id=\"session\" name=\"session\" value=\"");
		sb.append(session);
		sb.append("\" />\n");
	}
	
	
	public void appendToken(StringBuffer sb, String token) {
		sb.append("<input type=\"hidden\" id=\"token\" name=\"token\" value=\"");
		sb.append(token);
		sb.append("\" />\n");
	}
	
	
	/** Versteckte Inputs für session und, falls aktiver Player, das token*/
	public void appendHidden(StringBuffer sb, String token) {
		appendSession(sb);
		if (player.getInstance().getActive()==player) {
			appendToken(sb, token);
		}
	}
	
	
	protected abstract void appendBody(StringBuffer sb);


	public String getBodyClass() {
		return getPlayer().getInstance().getActive()==getPlayer() ? "aktiv" : "";
	}
	
	
	public String gameHtml() {
		StringBuffer sb = new StringBuffer();
		getGame().appendHead(sb);
		sb.append("<body id=\"body\" class=\"");
		sb.append(getBodyClass());
		sb.append("\">\n");
		appendBody(sb);
		sb.append("</body>\n</html>");
		return sb.toString();
	}


	public String gameBody() {
		StringBuffer sb = new StringBuffer();
		appendBody(sb);
		return sb.toString();
	}
	
	
	public String gameBodyJSON() {
		return "{\"body\": "+Strings.toJSON(gameBody())+",\n\"className\": "+Strings.toJSON(getBodyClass())+"}";
	}


	public int sendHtml() {
		fine(() -> "UPDATE "+this);
		return eventStreams.sendEvent(null, null, "send", gameBodyJSON(), true);
	}
	
	
	public int playAudio(String url) {
		fine(() -> "AUDIO "+this);
		return eventStreams.sendEvent(null, null, "playAudio", url, true);
	}


	public void close() {
		getInstance().getGame().remove(session);
	}


}
