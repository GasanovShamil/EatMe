package game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

@SuppressWarnings("serial")
public class User implements Serializable {
	private String username;
	private transient Socket socket;
	private transient ObjectInputStream input;
	private transient ObjectOutputStream output;

	public User(String username, Socket socket, ObjectInputStream input, ObjectOutputStream output) {
		this.username = username;
		this.socket = socket;
		this.input = input;
		this.output = output;
	}

	public String getUsername() {
		return username;
	}

	public boolean isConnected() {
		boolean flag = true;
		try {
			socket.sendUrgentData(0);
		} catch (IOException e) {
			flag = false;
		}
		return flag;
	}

	public void send(Object message) {
		try {
			output.writeObject(message);
			Thread.sleep(1000);
			output.flush();
			output.reset();
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
