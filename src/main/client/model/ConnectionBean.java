package main.client.model;
import java.io.Serializable;

import main.client.model.ConnectionBean;

public class ConnectionBean implements Serializable{
	
	private ConnectionType type;
	
	private String login,password;
	
	
	public ConnectionBean(ConnectionType type, String login, String password){
		this.type=type;
		this.login=login;
		this.password=password;
	}


	public ConnectionType getType() {
		return type;
	}

	public String getLogin() {
		return login;
	}


	public String getPassword() {
		return password;
	}


	
}
