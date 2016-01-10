package main.client.view;

public class ClientConsol {

	public static void main(String[] args) {
		
		boolean flag = false;

		System.out.println("*********************************");
		System.out.println("* Welcome To EatMeIfYouCan !	*");
		System.out.println("*********************************");
		System.out.println("");
		
		while(!flag){
			System.out.print("Console: ");
			
			String user =keyboard.readLine();
			switch(s){
			case "exit": 
				connection.interrupt();
				System.exit(0);
				break;
			default: System.out.println("Help: \n exit - arreter le serveur \n help - l'affichage d'aide" );
			}

	}
	
	}

}
