package game;

import java.util.ArrayList;
import java.util.Random;

import enums.GameMessageType;

public class Game extends Thread {
	private Player[] players;
	private int numRound;
	private int loser;
	private boolean end;

	public Game(Player[] players) {
		this.players = players;
		numRound = 0;
		loser = -1;
		end = false;

		randomRoles();
	}

	public void run() {
		while (!isInterrupted() && !end) {
			numRound++;
			randomRoles();

			sendInfos();

			send(GameMessageType.YOUR_TURN, getInnocents());

			recieve(getInnocents());

			send(GameMessageType.YOUR_TURN, getWolf());

			recieve(getWolf());

			doGame();

			if (checkWin() != -1) {
				// SEND GEND
				end = true;
			} else {
				// SEND CONTINUE
			}
		}
	}

	private void recieve(int position) {
		players[position] = (Player) players[position].recieve();
	}

	private void recieve(ArrayList<Integer> positions) {
		for (int n : positions) {
			players[n] = (Player) players[n].recieve();
		}
	}

	private void send(Object message, int position) {
		players[position].send(message);
	}

	private void send(Object message, ArrayList<Integer> positions) {
		for (int n : positions) {
			players[n].send(message);
		}
	}

	private void sendInfos() {
		ArrayList<Integer> positions = new ArrayList<Integer>();
		for (int i = 0; i < players.length; i++) {
			positions.add(i);
		}
		send(players, positions);
	}

	private ArrayList<Integer> getInnocents() {
		ArrayList<Integer> positions = new ArrayList<Integer>();
		for (Player p : players) {
			if (!p.getRole().isWolf()) {
				positions.add(p.getPosition());
			}
		}
		return positions;
	}

	private int getWolf() {
		int position = -1;
		int cpt = 0;
		while (position == -1 && cpt < players.length) {
			if (players[cpt].getRole().isWolf()) {
				position = players[cpt].getPosition();
			}
			cpt++;
		}
		return position;
	}

	public int checkWin() {
		ArrayList<Integer> winners = new ArrayList<Integer>();
		int check = -1;

		for (Player player : players) {
			if (player.getPoints() >= 10) {
				winners.add(player.getPosition());
			}
		}

		if (winners.size() == 1) {
			check = winners.get(0);
		} else if (winners.size() > 1) {
			ArrayList<Integer> exAequos = new ArrayList<Integer>();

			int max = 0;
			for (int i = 0; i < winners.size(); i++) {
				int pts = players[winners.get(i)].getPoints();
				if (pts > max) {
					max = pts;
					exAequos = new ArrayList<Integer>();
					exAequos.add(winners.get(i));
				} else if (pts == max) {
					exAequos.add(winners.get(i));
				}
			}

			if (exAequos.size() == 1) {
				check = exAequos.get(0);
			} else if (exAequos.size() > 1) {
				int wolf = getWolf();
				int target = ((Wolf) (players[wolf].getRole())).getTarget().getPosition();

				if (exAequos.contains(wolf)) {
					check = wolf;
				} else if (exAequos.contains(target)) {
					check = target;
				} else {
					int cpt = wolf +1;
					while (cpt <= wolf + players.length && !exAequos.contains(cpt%players.length)){
						cpt++;
					}
					
					check = cpt%players.length;
				}
			}
		}

		return check;
	}

	private void randomRoles() {
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

	private void doGame() {

	}
}