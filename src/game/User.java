package game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class User implements Serializable{
	private String username;
	private Socket socket;

	public User(String username, Socket socket) {
		this.username=username;
		this.socket = socket;
	}
	
	public ObjectInputStream getInput() throws IOException{
		return new ObjectInputStream(socket.getInputStream());
	}
	
	 public ObjectOutputStream getOutput() throws IOException{
		 return new ObjectOutputStream(socket.getOutputStream());
	 }
}
