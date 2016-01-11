package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import game.User;

public class ServerConnectionThread extends Thread {
	private ArrayList<User> users;
	private ServerSocket serverSocket;
	private Socket socket;

	public ServerConnectionThread(int port, ArrayList<User> users) throws IOException {
		this.users = users;
		serverSocket = new ServerSocket(port);
	}

	public void run() {
		while (!this.isInterrupted()) {
			try {
				socket = serverSocket.accept();
				ServerAuthenticationThread sat = new ServerAuthenticationThread(socket, users);
				sat.start();
			} catch (IOException e) {
				this.interrupt();
				e.printStackTrace();
			}
		}
	}
}
