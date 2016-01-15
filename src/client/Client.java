package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import enums.ConnectionMessageType;
import game.*;

public class Client {
	private Socket socket;
	ObjectOutputStream output;
	ObjectInputStream input;
	private String username;
	private String password;
	private ConnectionMessageType msg;
	private boolean connected;
	private Player[] players;
	private int position;

	public Client() {
		msg = ConnectionMessageType.DEFAULT;
		connected = false;
	}

	public String startRound(Player[] players) {
		this.players = players;
		int cpt = 0;
		while (cpt < players.length && !players[cpt].getUsername().equals(username)) {
			cpt++;
		}
		position = cpt;

		return infos() + options();
	}

	public String infos() {
		String infos = "";

		for (Player player : players) {
			infos += player.getUsername() + " " + player.getPosition() + " " + player.getRole().getName() + " "
					+ player.getPoints() + "\n";
		}

		return infos;
	}

	private String options() {
		String options = "";
		Player me = players[position];

		if (me.getRole().isWolf()) {
			options += "\nMordre :";
			for (int i = 0; i < players.length; i++) {
				if (!players[i].equals(me)) {
					options += "\n" + players[i].getPosition() + "/ " + players[i].getUsername() + " ("
							+ players[i].getRole().getName() + ")";
				}
			}
		} else {
			options += "\n1/ Je dors";
			options += "\n2/ Je pose un piège";
		}

		return options;
	}

	public String doAction(String choix) {
		String action = "";
		Role me = players[position].getRole();

		if (me.isWolf()) {
			((Wolf) (me)).bite(players[Integer.parseInt(choix)]);
		} else {
			switch (choix) {
			case "1":
				((Innocent) (me)).sleep();
				action = "Vous dormez.";
				break;
			case "2":
				((Innocent) (me)).putTrap();
				action = "Vous posez un piège.";
				break;
			default:
				action = "NEIN.";
				break;
			}
		}

		send(players[position].getRole());

		return action;
	}

	public int getRoundPoints() {
		return players[position].calcPoints();
	}

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
			output.reset();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
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

	public boolean setConnection(String serverAdress, int serverPort) {
		boolean flag = false;
		try {
			socket = new Socket(serverAdress, serverPort);
			if (flag = socket.isConnected()) {
				output = new ObjectOutputStream(socket.getOutputStream());
				input = new ObjectInputStream(socket.getInputStream());
			}
		} catch (IOException e) {
			flag=false;
		}
		return flag;
	}

	public String connect(ConnectionMessageType type, String username, String password) {
		String result = "";

		try {
			send(new ConnectionBean(type, username, password));
			msg = (ConnectionMessageType) recieve();

			if (msg == ConnectionMessageType.SUCCESS) {
				connected = true;
				this.username = username;
				this.password = password;
				result = "Vous êtes connecté sur le serveur.";
			} else if (msg == ConnectionMessageType.FAIL) {
				connected = false;
				result = "Identifiants incorrects. Si le problème persiste, vérifiez votre connexion.";
			} else if (msg == ConnectionMessageType.EXIST) {
				connected = false;
				result = "L'utilisateur \"" + username + "\" existe deja";
			}
		} catch (Exception e) {
			e.printStackTrace();
			connected = false;
			result = "Un problème est survenu lors de votre tentative de connexion, réessayer plus tard.";
		}

		return result;
	}
}
