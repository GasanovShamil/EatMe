package game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class User implements Serializable {
	private String username;
	private transient Socket socket;
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

	public void send(Object message) {
		try {
			output.writeObject(message);
			Thread.sleep(1000);
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Object recieve() {
		try {
			return input.readObject();
		} catch (ClassNotFoundException e) {
			System.out.println("CFE");
			e.printStackTrace();
			
		} catch (IOException e) {
			System.out.println("IO");
			e.printStackTrace();
			
		}
		return null;
	}
}
