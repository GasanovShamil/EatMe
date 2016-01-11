package client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	private Socket socket;
	private String serverAdress;
	private int serverPort;
	private String username;
	private String password;

	public Client(String serverAdress, int serverPort, String username, String password) {
		socket = null;
		this.serverAdress = serverAdress;
		this.serverPort = serverPort;
		this.username = username;
		this.password = password;
	}

	private void connectToServer() {
		while (socket == null) {
			try {
				socket = new Socket(serverAdress, serverPort);
				System.out.println("Vous êtes connecté sur le serveur.");
			} catch (UnknownHostException e) {
				e.printStackTrace();
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public void connect() {
		connectToServer();
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(new ConnectionBean(ConnectionType.AUTHENTICATE, username, password));
			oos.flush();
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
