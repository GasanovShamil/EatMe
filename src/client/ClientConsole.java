package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import enums.*;
import game.Player;

public class ClientConsole {
	public static void main(String[] args) throws IOException {
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("*************************************");
		System.out.println("* Bienvenue Sur Eat Me If You Can ! *");
		System.out.println("*************************************\n");

		Client client = null;
		boolean flag = false;
		boolean test = true;

		while (!flag) {
			if (!test) {
				// port = 9443
				System.out.print("Entrez l'adresse du serveur : ");
				String serverAdress = keyboard.readLine();
				System.out.print("Entrez le port du serveur : ");
				String serverPort = keyboard.readLine();

				ConnectionType type = null;

				while (type == null) {
					System.out.print("\n1/ S'inscrire" + "\n2/ Se connecter" + "\nVotre choix : ");
					String choix = keyboard.readLine();
					switch (choix) {
					case "1":
						type = ConnectionType.CREATE_ACCOUNT;
						break;

					case "2":
						type = ConnectionType.AUTHENTICATE;
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

				client = new Client(serverAdress, Integer.parseInt(serverPort), username, password);
				client.connect(type);
				flag = client.isConnected();
			} else {
				System.out.print("\nEntrez votre username : ");
				String username = keyboard.readLine();
				System.out.print("Entrez votre mot de passe : ");
				String password = keyboard.readLine();
				client = new Client("localhost", 9443, username, password);
				System.out.println(client.connect(ConnectionType.AUTHENTICATE));
				flag = client.isConnected();
			}
		}

		flag = false;

		while (!flag) {
			System.out.println("MENUUUUUUUUUUUUUUUUUU");
			System.out.print("\n1/ Lancer une partie � 3 joueurs" + "\n2/ Lancer une partie � 4 joueurs"
					+ "\n3/ Lancer une partie � 5 joueurs" + "\n4/ Lancer une partie � 6 joueurs" + "\n999/ D�connexion"
					+ "\nVotre choix : ");
			String choix = keyboard.readLine();
			switch (choix) {
			case "1":
				client.send(StartGameType.START_3P);
				flag = true;
				break;

			case "2":
				client.send(StartGameType.START_4P);
				flag = true;
				break;

			case "3":
				client.send(StartGameType.START_5P);
				flag = true;
				break;

			case "4":
				client.send(StartGameType.START_6P);
				flag = true;
				break;

			case "999":
				break;

			default:
				System.out.println("Saisie incorrecte.");
				break;
			}
		}

		System.out.println("En attente d'une partie ...");
		flag = false;

		while (!flag) {
			Player[] players = (Player[]) client.recieve();
			System.out.println(client.startRound(players));
			System.out.print("Votre choix : ");
			String choix = keyboard.readLine();
			System.out.println(client.doAction(choix));

			GameMessageType message = (GameMessageType) client.recieve();

			switch (message) {
			case GAME_END_LOSER:
				System.out.println("LOSER");
				flag = true;
				break;

			case GAME_END_WINNER:
				System.out.println("WINNER");
				flag = true;
				break;

			case ROUND_END_LOSER:
				System.out.println(client.getRoundPoints());
				System.out.println("ATTRIB ROLE !");
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
