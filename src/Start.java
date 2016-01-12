import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import client.Client;
import client.ClientConsole;
import server.Server;

public class Start {
	public static void main(String[] args) {
		System.out.print(
				"Bienvenue sur EatMe !\nVeuillez choisir le mode d'exécution :\nClient\nServeur\n\nVotre choix : ");

		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
		try {
			String s = keyboard.readLine().toLowerCase().trim();
			switch (s) {
			case "s":
			case "serveur":
				System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
				new Server();
				break;
			case "c":
			case "client":
				System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
				new ClientConsole();
				break;
			case "h":
			case "help":
				System.out.println("COMMANDES");
				break;
			case "x":
			case "exit":
				System.exit(0);
				break;
			default:
				System.out.println("Ce n'est pas une commande valide ! (Taper help (ou h) pour voir les options disponibles");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
