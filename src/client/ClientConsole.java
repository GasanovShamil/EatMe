package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientConsole {
	public static void main(String[] args) throws IOException {
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("*************************************");
		System.out.println("* Bienvenue Sur Eat Me If You Can ! *");
		System.out.println("*************************************\n");

		System.out.print("Entrez l'adresse du serveur : ");
		String serverAdress = keyboard.readLine();
		System.out.print("Entrez le port du serveur : ");
		String serverPort = keyboard.readLine();

		ConnectionType type = null;
		
		while (type == null){
			System.out.print("\n1/ S'inscrire\n2/ Se connecter\nVotre choix : ");
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

		Client client = new Client(serverAdress, Integer.parseInt(serverPort), username, password);
		client.connect(type);
	}
}