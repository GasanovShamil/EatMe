package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import game.User;

public class ServerAcceptThread extends Thread {
	private ArrayList<User> users;
	private ServerSocket serverSocket;
	private Socket socket;

	public ServerAcceptThread(int port, ArrayList<User> users) throws IOException {
		this.users = users;
		serverSocket = new ServerSocket(port);
	}

	public void run() {
		while (!this.isInterrupted()) {
			try {
				socket = serverSocket.accept();
				ServerConnectionThread sct = new ServerConnectionThread(socket, users);
				sct.start();
			} catch (IOException e) {
				this.interrupt();
				e.printStackTrace();
			}
		}
	}
}
