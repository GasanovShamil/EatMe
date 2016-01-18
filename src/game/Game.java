package game;

import java.util.ArrayList;
import enums.Message;
import server.ServerUserThread;

/**
 * <p>
 * Classe correspondant à une partie
 * </p>
 */
public class Game extends Thread {
	private Player[] players;
	private ArrayList<ServerUserThread> usersThreads;
	private ArrayList<Integer> roundWinners;
	private ArrayList<Integer> roundNeutrals;
	private ArrayList<Integer> gameLosers;
	private int roundLoser;

	public Game(Player[] players, ArrayList<ServerUserThread> usersThreads) {
		this.players = players;
		this.usersThreads=usersThreads;
		randomRoles();
	}

	public void run() {
		while (!isInterrupted()) {
			roundWinners = new ArrayList<Integer>();
			roundNeutrals = new ArrayList<Integer>();
			roundLoser = -1;
			sendAll(players);
			if (recieveAll()) {
				doGame();
				int winner = checkWin();
				if (winner != -1) {
					send(Message.GAME_END_WINNER, winner);
					send(Message.GAME_END_LOSER, gameLosers);
					for (int i = 0; i < players.length; i++) {
						players[i].notifyUser();
					}
					interrupt();
				} else {
					send(Message.ROUND_END_WINNER, roundWinners);
					send(Message.ROUND_END_NEUTRAL, roundNeutrals);
					send(Message.ROUND_END_LOSER, roundLoser);
					setRoles((Role[]) players[roundLoser].recieve());
					
				}
			}else{
				sendAll(Message.ENNEMY_DISCONNECTED);
				for (int i = 0; i < players.length; i++) {
					players[i].notifyUser();
				}
				interrupt();
			}
		}
	}

	private boolean recieveAll() {
		for (int i = 0; i < players.length; i++) {
			Object obj = players[i].recieve();
			if (obj instanceof Message && (obj == Message.CONNECTION_LOST || obj == Message.DISCONNECT)) {
				return false;
			} else {
				players[i].setRole((Role) obj);
			}
		}
		return true;
	}

	private boolean send(Object message, int position) {
		return players[position].send(message);
	}

	private void send(Object message, ArrayList<Integer> positions) {
		for (int n : positions) {
			players[n].send(message);
		}
	}

	private void sendAll(Object message) {
		ArrayList<Integer> positions = new ArrayList<Integer>();
		for (int i = 0; i < players.length; i++) {
			positions.add(i);
		}
		send(message, positions);
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

	/**
	 * <p>
	 * Méthode de vérification des points afin de désigner un vainqueur
	 * </p>
	 * 
	 * @return La position du vainqueur de la partie ou -1 si personne n'a gagné
	 */
	public int checkWin() {
		ArrayList<Integer> winners = new ArrayList<Integer>();
		int check = -1;

		for (Player player : players) {
			if (player.getPoints() >= 5) {
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
					int cpt = wolf + 1;
					while (cpt <= wolf + players.length && !exAequos.contains(cpt % players.length)) {
						cpt++;
					}

					check = cpt % players.length;
				}
			}
		}

		if (check != -1) {
			gameLosers = new ArrayList<Integer>();
			for (int i = 0; i < players.length; i++) {
				if (i != check)
					gameLosers.add(i);
			}
		}

		return check;
	}

	/**
	 * <p>
	 * Méthode d'attribution aléatoire des rôles
	 * </p>
	 */
	private void randomRoles() {
		ArrayList<Role> roles = Role.generateRoles(players.length);
		int cpt = 0;

		while (roles.size() > 0) {
			int rand = (int) (Math.random() * roles.size());
			Role role = roles.get(rand);

			players[cpt++].setRole(role);
			roles.remove(role);
		}
	}

	private void doGame() {
		int wolf = getWolf();
		int target = ((Wolf) (players[wolf].getRole())).getTarget().getPosition();

		if (((Innocent) players[target].getRole()).isTrap()) {
			players[target].addPoints();
			players[wolf].removePoints();
			roundLoser = wolf;
			roundWinners.add(target);
		} else {
			players[target].removePoints();
			players[wolf].addPoints();
			roundLoser = target;
			roundWinners.add(wolf);
		}

		for (int i = 0; i < players.length; i++) {
			if (i != wolf && i != target) {
				if (((Innocent) players[i].getRole()).isTrap()) {
					roundNeutrals.add(i);
				} else {
					players[i].addPoints();
					roundWinners.add(i);
				}
			}
		}
	}

	private void setRoles(Role[] roles) {
		for (int i = 0; i < players.length; i++) {
			players[i].setRole(roles[i]);
		}
	}

}