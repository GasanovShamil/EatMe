package game;

import java.net.Socket;

public class User {
	private String username;
	private Socket socket;

	public User(String username, Socket socket) {
		this.username=username;
		this.socket = socket;
	}
}
