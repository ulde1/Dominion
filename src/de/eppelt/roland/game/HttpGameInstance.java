package de.eppelt.roland.game;

import java.util.stream.Stream;

import de.tesd.util.Password;


public abstract class HttpGameInstance<INSTANCE extends HttpGameInstance<INSTANCE, CLIENT, PLAYER>, CLIENT extends HttpGameClient<INSTANCE, CLIENT, PLAYER>, PLAYER extends GamePlayer<INSTANCE, CLIENT, PLAYER>> implements GameInstance<INSTANCE, CLIENT, PLAYER> {

	
	HttpGame<INSTANCE, CLIENT, PLAYER> game;
	String token = Password.generateNewPassword(15); // besitzt nur der aktive Spieler
	String id = Password.generateNewPassword(7); // zum Teilnehmen


	public HttpGameInstance(HttpGame<INSTANCE, CLIENT, PLAYER> game) {
		this.game = game;
	}


	@Override public HttpGame<INSTANCE, CLIENT, PLAYER> getGame() {
		return game;
	}

	
	public String getID() {
		return id;
	}
	
	
	public String getToken() {
		return token;
	}
	
	
	public void newToken() {
		token = Password.generateNewPassword(15);
	}

	
	public Stream<CLIENT> stream() {
		return getGame().stream()
				.filter(c -> equals(c.getInstance())); 
	}


	public void updateAllPlayers() {
		stream()
			.forEach(HttpGameClient::sendHtml);
	}
	
	
	public void updatePlayersExcept(HttpGameClient<INSTANCE, CLIENT, PLAYER> client) {
		stream()
			.filter(other -> !client.equals(other))
			.filter(other -> !other.hasInputField())
			.forEach(HttpGameClient::sendHtml);
	}


	public void updatePlayersExcept(GamePlayer<INSTANCE, CLIENT, PLAYER> player) {
		stream()
			.filter(other -> !other.getPlayer().equals(player))
			.filter(other -> !other.hasInputField())
			.forEach(HttpGameClient::sendHtml);
	}
	

	@SuppressWarnings("unchecked")
	public void openForNewPlayers() {
		getGame().openInstanceForNewPlayers((INSTANCE) this);
	}


	@SuppressWarnings("unchecked")
	public boolean isOpenForNewPlayers() {
		return getGame().isInstanceOpenForNewPlayers((INSTANCE) this);
	}


	@SuppressWarnings("unchecked")
	public void closeForNewPlayers() {
		getGame().closeInstanceForNewPlayers((INSTANCE) this);
	}


}
