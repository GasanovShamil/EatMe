package server;

import java.util.ArrayList;

import game.Game;
import game.Player;
import game.User;

public class Queue {
	private ArrayList<ServerUserThread> usersThreads;
	private ArrayList<User> players3;
	private ArrayList<User> players4;
	private ArrayList<User> players5;
	private ArrayList<User> players6;

	public Queue(ArrayList<ServerUserThread> usersThreads) {
		players3 = new ArrayList<User>();
		players4 = new ArrayList<User>();
		players5 = new ArrayList<User>();
		players6 = new ArrayList<User>();
		this.usersThreads=usersThreads;
	}

	public void addUser(int numberOfPlayers, User user) {
		ArrayList<User> users = null;

		switch (numberOfPlayers) {
		case 3:
			users = players3;
			break;

		case 4:
			users = players4;
			break;

		case 5:
			users = players5;
			break;

		case 6:
			users = players6;
			break;

		default:
			break;
		}

		users.add(user);
		checkStart(numberOfPlayers, users);
	}

	private void connectionTest(ArrayList<User> users) {
		ArrayList<User> remove = new ArrayList<User>();
		for (int i = 0; i < users.size(); i++) {
			User user = users.get(i);
			if (!user.isConnected()) {
				remove.add(user);
			}
		}
			
		for (int i = 0; i < remove.size(); i++) {
			users.remove(remove.get(i));
			for(int j=0;j<usersThreads.size(); j++){
				usersThreads.get(j).interrupt();
			}
			
		}
	}

	private void checkStart(int numberOfPlayers, ArrayList<User> users) {
		connectionTest(users);

		if (users.size() == numberOfPlayers) {
			Player[] players = new Player[numberOfPlayers];
			for (int i = 0; i < players.length; i++) {
				players[i] = new Player(users.get(i), i);
			}
			System.out.println("New Game");
			Game game = new Game(players);
			game.start();
			users.removeAll(users);
		}
	}
}
