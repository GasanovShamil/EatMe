package game;

import java.net.Socket;

public class User {
	
	Socket socket;
	
	public User(Socket socket){
		this.socket=socket;
	}
}
