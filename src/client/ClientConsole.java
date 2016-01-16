package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import enums.*;
import game.Player;
import game.Role;

public class ClientConsole {
	public static void main(String[] args) throws IOException {
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("*************************************");
		System.out.println("* Bienvenue Sur Eat Me If You Can ! *");
		System.out.println("*************************************\n");

		Client client = null;
		boolean flag = false;
		boolean inGame = false;
		boolean test = false;
		client = new Client();
		// port = 9443
		do {
			System.out.print("Entrez l'adresse du serveur : ");
			String serverAdress = keyboard.readLine();
			System.out.print("Entrez le port du serveur : ");
			int serverPort = Integer.parseInt(keyboard.readLine());
			flag = client.setConnection(serverAdress, serverPort);
			if (!flag) {
				System.out.println("Connection to server failed");
			}
		} while (!flag);

		Message type = null;
		flag = false;

		while (!flag) {
			while (type == null) {
				System.out.print("\n1/ S'inscrire" + "\n2/ Se connecter" + "\n3/ Quiter" + "\nVotre choix : ");
				String choix = keyboard.readLine();
				switch (choix) {
				case "1":
					type = Message.CREATE_ACCOUNT;
					break;

				case "2":
					type = Message.AUTHENTICATE;
					break;
				case "3":
					System.exit(0);
					break;
				default:
					System.out.println("Saisie incorrecte.");
					break;
				}
			}
			System.out.print("\nEntrez votre username : ");
			String username = keyboard.readLine();
			System.out.print("Entrez votre mot de passe : ");
			String password = keyboard.readLine();

			Message msg = client.connect(type, username, password);

			if (msg == Message.SUCCESS) {
				System.out.println("Vous êtes connecté sur le serveur.");
			} else if (msg == Message.FAIL) {

				System.out.println("Identifiants incorrects. Si le problème persiste, vérifiez votre connexion.");
			} else if (msg == Message.EXIST) {

				System.out.println("L'utilisateur \"" + username + "\" existe deja");
			}
			flag = client.isConnected();
		}

		flag = false;

		while (!flag) {
			System.out.println("MENUUUUUUUUUUUUUUUUUU");
			System.out.print("\n1/ Lancer une partie à 3 joueurs" + "\n2/ Lancer une partie à 4 joueurs"
					+ "\n3/ Lancer une partie à 5 joueurs" + "\n4/ Lancer une partie à 6 joueurs" + "\n999/ Déconnexion"
					+ "\nVotre choix : ");
			String choix = keyboard.readLine();
			switch (choix) {
			case "1":
				client.send(Message.START_3P);
				inGame = true;
				break;

			case "2":
				client.send(Message.START_4P);
				inGame = true;
				break;

			case "3":
				client.send(Message.START_5P);
				inGame = true;
				break;

			case "4":
				client.send(Message.START_6P);
				inGame = true;
				break;

			case "999":
				client.send(Message.DECONNECT);
				flag = true;
				break;

			default:
				System.out.println("Saisie incorrecte.");
				break;
			}

			if (inGame) {
				System.out.println("En attente d'une partie ...");
			}

			while (inGame) {
				Player[] players = (Player[]) client.recieve();
				System.out.println(client.startRound(players));
				System.out.print("Votre choix : ");
				System.out.println(client.doAction(keyboard.readLine()));

				Message message = (Message) client.recieve();

				switch (message) {
				case GAME_END_LOSER:
					System.out.println("LOSER");
					inGame = false;
					break;

				case GAME_END_WINNER:
					System.out.println("WINNER");
					inGame = false;
					break;

				case ROUND_END_LOSER:
					System.out.println("-" + client.getRoundPoints());
					System.out.println("ATTRIB ROLE !");

					ArrayList<Role> listRoles = Role.generateRoles(players.length);
					Role[] roles = new Role[players.length];

					for (int i = 0; i < players.length - 1; i++) {
						System.out.println("Role de : " + players[i].getUsername() + " ?");
						for (int j = 0; j < listRoles.size(); j++) {
							System.out.println(j + "/ " + listRoles.get(j).getName());
						}
						System.out.print("Votre choix : ");
						Role role = listRoles.get(Integer.parseInt(keyboard.readLine()));
						roles[i] = role;
						listRoles.remove(role);
					}
					roles[roles.length - 1] = listRoles.get(0);
					client.send(roles);
					break;

				case ROUND_END_NEUTRAL:
					System.out.println("0 pts");
					break;

				case ROUND_END_WINNER:
					System.out.println("+" + client.getRoundPoints());
					break;

				default:
					System.out.println("NE ZA CHTO");
					break;
				}
			}
		}
	}
}
