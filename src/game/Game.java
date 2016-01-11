package game;

import java.util.ArrayList;
import java.util.Random;

public class Game {
	private Player[] players;
	private int numRound;
	private int loser;

	public Game(Player[] players) {
		this.players = players;
		numRound = 0;
		loser = -1;

		int size = players.length;
		ArrayList<Role> roles = new ArrayList<Role>();
		Random random = new Random();
		int cpt = 0;

		if (size >= 3) {
			roles.add(new Wolf());
			roles.add(new YoungKids());
			roles.add(new Pig());

			if (size >= 4) {
				roles.add(new LittleRedCap());

				if (size >= 5) {
					roles.add(new Pig());

					if (size == 6) {
						roles.add(new Pig());
					}
				}
			}
		}

		while (roles.size() > 0) {
			int rand = random.nextInt(roles.size());
			Role role = roles.get(rand);
			players[cpt++].setRole(role);
			roles.remove(role);
		}
	}

	public void newRound() {
		numRound++;
		roleAttribution();

		// ACTION INNOCENTS

		// ACTION WOLF

		// RESULT
	}

	private void roleAttribution() {

	}
}