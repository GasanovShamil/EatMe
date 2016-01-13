package game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class User implements Serializable {
	private String username;
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;

	public User(String username, Socket socket, ObjectInputStream input, ObjectOutputStream output) {
		this.username = username;
		this.socket = socket;
		this.input = input;
		this.output = output;
	}

	public String getUsername() {
		return username;
	}

	public void send(Object obj) {
		try {
			output.writeObject(obj);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Object recieve() {
		
			try {
				return input.readObject();
			} catch (ClassNotFoundException e) {
				System.out.println("CFE");
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				System.out.println("IO");
				e.printStackTrace();
				return null;
			}
		
	}
}
