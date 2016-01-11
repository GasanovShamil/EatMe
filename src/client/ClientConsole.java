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
		System.out.print("Entrez votre username : ");
		String username = keyboard.readLine();
		System.out.print("Entrez votre mot de passe : ");
		String password = keyboard.readLine();
		
		Client client = new Client(serverAdress, Integer.parseInt(serverPort), username, password);
		client.connect();
	}
}