package client;

import java.io.Serializable;

import client.ConnectionBean;
import enums.Message;

/**
 * <p>
 * Classe correspondant à une tentative de connexion via un type, un identifiant et un mot de passe
 * </p>
 */
@SuppressWarnings("serial")
public class ConnectionBean implements Serializable {
	private Message type;
	private String login, password;

	public ConnectionBean(Message type, String login, String password) {
		this.type = type;
		this.login = login;
		this.password = password;
	}

	public Message getType() {
		return type;
	}

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

}
