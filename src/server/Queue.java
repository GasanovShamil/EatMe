package server;

import java.util.ArrayList;

import game.Game;
import game.Player;
import game.User;

public class Queue {
	private ArrayList<User> players3;
	private ArrayList<User> players4;
	private ArrayList<User> players5;
	private ArrayList<User> players6;

	public Queue() {
		players3 = new ArrayList<User>();
		players4 = new ArrayList<User>();
		players5 = new ArrayList<User>();
		players6 = new ArrayList<User>();
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
		test(numberOfPlayers, users);
	}

	private void test(int numberOfPlayers, ArrayList<User> users) {
		for (int i = 0; i < users.size(); i++) {
			User user = null;
			if (users.get(i).isDeconnected()) {
				users.remove(i);
			}
		}
		
		if (users.size() == numberOfPlayers) {
			Player[] players = new Player[numberOfPlayers];
			for (int i = 0; i < players.length; i++) {
				players[i] = new Player(users.get(i), i);
			}
			System.out.println("New Game");
			Game game = new Game(players);
			game.start();
			users = new ArrayList<User>();
		}
	}
}
