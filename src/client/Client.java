package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import game.User;

public class Client {
	private Socket socket;
	ObjectOutputStream output;
	ObjectInputStream input;
	private String serverAdress;
	private int serverPort;
	private String username;
	private String password;
	private Integer userID;
	private boolean connected;

	public Client(String serverAdress, int serverPort, String username, String password) {
		try {
			socket = new Socket(serverAdress, serverPort);
			output = new ObjectOutputStream(socket.getOutputStream());
			
			input = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.serverAdress = serverAdress;
		this.serverPort = serverPort;
		this.username = username;
		this.password = password;
		userID = 0;
		connected = false;
	}
	
	public String getUsername(){
		return username;
	}
	
	public boolean isConnected(){
		return connected;
	}
	
	public void send(Object obj){
		try {
			output.writeObject(obj);
			output.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Object recieve(){
		try {
			return input.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void connect(ConnectionType type) {
		try {
			send(new ConnectionBean(type, username, password));
			userID = (Integer) recieve();

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
