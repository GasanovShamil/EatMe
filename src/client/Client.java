package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import enums.Message;
import game.*;

public class Client {
	private Socket socket;
	ObjectOutputStream output;
	ObjectInputStream input;
	private String username;
	private boolean authenticated;
	private Player[] players;
	private int position;

	public Client() {
		authenticated = false;
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

	public boolean doAction(String choix) {
		
		Role me = players[position].getRole();

		if (me.isWolf()) {
			((Wolf) (me)).bite(players[Integer.parseInt(choix)]);
		} else {
			switch (choix) {
			case "1":
				((Innocent) (me)).sleep();
				System.out.println("Vous dormez.");
				break;
			case "2":
				((Innocent) (me)).putTrap();
				System.out.println("Vous posez un piège.");
				break;
			default:
				System.out.println("NEIN.");
				break;
			}
		}

		return send(players[position].getRole());
	}

	public int getRoundPoints() {
		return players[position].calcPoints();
	}

	public String getUsername() {
		return username;
	}
	
	public int getPosition() {
		return position;
	}

	public boolean isAuthenticated() {
		return authenticated;
	}

	public Player getPlayer(){
		return players[position];
	}
	
	public Player[] getPlayers(){
		return players;
	}
	
	public boolean isConnected() {
		boolean flag = true;
		try {
			socket.sendUrgentData(10);
		} catch (IOException e) {
			flag = false;
		}
		return flag;
	}

	public boolean send(Object obj) {
		boolean flag = true;
		try {
			output.writeObject(obj);
			Thread.sleep(1000);
			output.flush();
			output.reset();
		} catch (IOException e) {
			flag = false;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return flag;
	}

	public Object recieve() {
		Object result = null;

		try {
			result = input.readObject();
		} catch (ClassNotFoundException | IOException e) {
			return Message.CONNECTION_LOST;
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
			flag = false;
		}
		return flag;
	}

	public Message connect(Message type, String username, String password) {
		send(new ConnectionBean(type, username, password));
		 Message msg = (Message) recieve();
		if (msg == Message.SUCCESS) {
			authenticated = true;
			this.username = username;
		} else {
			authenticated = false;
		}
		return msg;
	}
}
