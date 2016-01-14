package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import enums.ConnectionType;
import game.Player;
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
	private Player[] players;
	private int position;

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

	public String startRound(Player[] players) {
		this.players = players;
		int cpt = 0;
		while (cpt < players.length && !players[cpt].getUser().getUsername().equals(username)) {
			cpt++;
		}
		position = cpt;
		
		return options();
	}

	private String options() {
		String options = "";
		Player me = players[position];

		if (me.getRole().isWolf()) {
			options += "\nMordre :";
			for (int i=0; i< players.length; i++){
				if (!players[i].equals(me)) {
					options += "\n" + players[i].getPosition() + "/ " + players[i].getUser().getUsername() + " ("
							+ players[i].getRole().getName() + ")";
				}
			}
		} else {
			options += "\n1/ Je dors";
			options += "\n2/ Je pose un pi�ge";
		}

		return options;
	}

	/*
	 * private void getMyPosition(){ int cpt=0;
	 * 
	 * while(cpt<players.length &&
	 * players[cpt].getUser().getUsername().equals(username)){ cpt++; }
	 * position=cpt; }
	 */

	public String getUsername() {
		return username;
	}

	public boolean isConnected() {
		return connected;
	}

	public void send(Object obj) {
		try {
			output.writeObject(obj);
			Thread.sleep(1000);
			output.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Object recieve() {
		Object result = null;

		try {
			result = input.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	public String connect(ConnectionType type) {
		String result = "";

		try {
			send(new ConnectionBean(type, username, password));
			userID = (Integer) recieve();

			if (userID > 0) {
				connected = true;
				result = "Vous �tes connect� sur le serveur.";
			} else {
				connected = false;
				result = "Identifiants incorrects. Si le probl�me persiste, v�rifiez votre connexion.";
			}
		} catch (Exception e) {
			e.printStackTrace();
			connected = false;
			result = "Un probl�me est survenu lors de votre tentative de connexion, r�essayer plus tard.";
		}

		return result;
	}
}
