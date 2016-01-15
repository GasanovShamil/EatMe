package client;
import java.io.Serializable;

import client.ConnectionBean;
import enums.ConnectionMessageType;

public class ConnectionBean implements Serializable{
	
	private ConnectionMessageType type;
	
	private String login,password;
	
	
	public ConnectionBean(ConnectionMessageType type, String login, String password){
		this.type=type;
		this.login=login;
		this.password=password;
	}


	public ConnectionMessageType getType() {
		return type;
	}

	public String getLogin() {
		return login;
	}


	public String getPassword() {
		return password;
	}


	
}
