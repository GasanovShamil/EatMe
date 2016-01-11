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
	private String serverAdress;
	private int serverPort;
	private String username;
	private String password;
	private Integer userID;
	private boolean connected;

	public Client(String serverAdress, int serverPort, String username, String password) {
		socket = null;
		this.serverAdress = serverAdress;
		this.serverPort = serverPort;
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
	
	public void send(Object object) throws IOException{
		output.writeObject(object);
		output.flush();
	}

	public void connect(ConnectionType type) {
		try {
			socket = new Socket(serverAdress, serverPort);
			input = new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());
			
			send(new ConnectionBean(type, username, password));
			userID = (Integer) input.readObject();

			if (userID > 0) {
				System.out.println("Vous êtes connecté sur le serveur.");
				connected = true;
			} else {
				System.out.println("Identifiants incorrects. Si le problème persiste, vérifiez votre connexion.");
				connected = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			connected = false;
		}
	}
}