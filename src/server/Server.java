package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;

import game.User;

public class Server {
	private ArrayList<User> users;
	private ArrayList<Socket> sockets;
	private static boolean flag = false;
	private static ConfigurationBean conf;

	public static void main(String[] args) throws IOException, InterruptedException {
		setConf();
		ServerConnectionThread connection = new ServerConnectionThread(conf.getPort());
		System.out.println("Server started at port N:" + conf.getPort());
		connection.start();
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
		while (!flag) {
			System.out.print("Console: ");
			String s = keyboard.readLine();
			switch (s) {
			case "exit":
				connection.interrupt();
				System.exit(0);
				break;
			default:
				System.out.println("Help: \n exit - arreter le serveur");
			}
		}
	}

	/**
	 * <p>
	 * Lis le fichier conf.xml pour recuperer les parametres de la configuration
	 * du serveur.
	 * </p>
	 * 
	 * @return ConfigurationBean- object pour configurer le serveur
	 */
	public static void setConf() {
		int port = 0;

		try {
			SAXBuilder parser = new SAXBuilder();
			FileReader fr = new FileReader("src" + File.separator + "config.xml");
			Document Doc = parser.build(fr);
			String res = Doc.getRootElement().getAttributeValue("port");
			port = Integer.parseInt(res);
			conf = new ConfigurationBean(port);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

	}
}
