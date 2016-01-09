package main.client.model;
import main.client.model.ConnectionBean;

public class ConnectionBean {
	private final int CREATE_ACCOUNT=0;
	private final int AUTHENTICATE=1;
	private int type;
	private String login,password;
	
	
	public ConnectionBean(int type, String login, String password){
		this.type=type;
		this.login=login;
		this.password=password;
	}


	public int getType() {
		return type;
	}

	public String getLogin() {
		return login;
	}


	public String getPassword() {
		return password;
	}


	
}
