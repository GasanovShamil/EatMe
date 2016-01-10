package main.client.model;

import java.net.Socket;

public class User {
	
	Socket socket;
	
	public User(Socket socket){
		this.socket=socket;
	}
}
