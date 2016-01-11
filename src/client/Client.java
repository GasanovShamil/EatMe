package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import game.User;

public class Client {
	private Socket socket;
	private String serverAdress;
	private int serverPort;
	private String username;
	private String password;
	private User user;

	public Client(String serverAdress, int serverPort, String username, String password) {
		socket = null;
		this.serverAdress = serverAdress;
		this.serverPort = serverPort;
		this.username = username;
		this.password = password;
		user = null;
	}

	public boolean connect(ConnectionType type) {
		try {
			socket = new Socket(serverAdress, serverPort);
			ObjectOutputStream oos;
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(new ConnectionBean(type, username, password));
			oos.flush();
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			user = (User) ois.readObject();

			if (user != null) {
				System.out.println("Vous êtes connecté sur le serveur.");
				return true;
			} else {
				System.out.println("Identifiants incorrects. Si le problème persiste, vérifiez votre connexion.");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
