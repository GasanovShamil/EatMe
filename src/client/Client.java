package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import game.User;

public class Client {
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private String username;
	private String password;
	private Integer userID;
	private boolean connected;

	public Client(String serverAdress, int serverPort, String username, String password) {
		try {
			socket = new Socket(serverAdress, serverPort);
			synchronized (socket) {
				input = new ObjectInputStream(socket.getInputStream());
			}
			synchronized (socket) {
				output = new ObjectOutputStream(socket.getOutputStream());
				output.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.username = username;
		this.password = password;
		userID = 0;
		connected = false;
	}

	public String getUsername() {
		return username;
	}

	public boolean isConnected() {
		return connected;
	}

	public void send(Object object) {
		try {
			output.flush();
			output.writeObject(object);
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void connect(ConnectionType type) {
		try {
			send(new ConnectionBean(type, username, password));
			userID = (Integer) input.readObject();

			if (userID > 0) {
				System.out.println("Vous êtes connecté sur le serveur.");
				connected = true;
			} else if (userID == -1) {
				System.out.println("Identifiants incorrects. Si le problème persiste, vérifiez votre connexion.");
				connected = false;
			} else if (userID == -2) {
				System.out.println("Ce username est déjà utilisé, veuillez en choisir un autre.");
				connected = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			connected = false;
		}
	}
}
