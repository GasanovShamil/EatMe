package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import client.Message;
import game.User;

public class ServerUserThread extends Thread {
	private User user;
	ObjectInputStream input;
	ObjectOutputStream output;

	public ServerUserThread(User user) {
		this.user = user;
		try {
			this.input = user.getInput();
			this.output = user.getOutput();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void run() {
		try {
			Message message = (Message) input.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		

	}
}