package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientConsol {

	public static void main(String[] args) throws IOException {

		BufferedReader keyboard= new BufferedReader(
				new InputStreamReader(System.in));
		
		System.out.println("*********************************");
		System.out.println("* Welcome To Eat Me If You Can !*");
		System.out.println("*********************************");
		System.out.println("");

		String s = keyboard.readLine();
		switch(s){
		case "exit": 
			System.exit(0);
			
			break;
		default: System.out.println("Help: \n exit - arreter le serveur \n help - l'affichage d'aide" );

		}

	}

}
