package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;

public class Server {
	//private static Logger log = Logger.getLogger(Server.class.getName());
	private static ArrayList<ServerUserThread> users;
	private static Queue queue;
	private static boolean flag = false;
	private static ConfigurationBean conf;
	
	
	public static void main(String[] args) throws IOException, InterruptedException {
		Connection dbConnection=null;
		setConf();
		//LogManager.getLogManager().readConfiguration(Server.class.getResourceAsStream("logging.properties"));
		users = new ArrayList<ServerUserThread>();
		queue = new Queue();
		InetAddress addr = InetAddress.getLocalHost();
		Properties prop = new Properties();
		prop.put("characterEncoding", "UTF8");
		prop.put("user", "root");
		prop.put("password", "root");
		try {
			dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/eatme", prop);
		} catch (SQLException e) {
			System.out.println("Pas de connection sur la BD");
			System.exit(1);
		}
		ServerAcceptThread sat = new ServerAcceptThread(conf.getPort(), users, queue, dbConnection);
		//log.info("Server started at " + addr.getHostAddress() + ":" + conf.getPort());
		System.out.println("Server started at " + addr.getHostAddress() + ":" + conf.getPort());
		sat.start();
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
		while (!flag) {
			System.out.print("Console: ");
			String s = keyboard.readLine();
			switch (s) {
			case "exit":
				sat.interrupt();
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
	 * @return ConfigurationBean - object pour configurer le serveur
	 */
	private static void setConf() {
		int port = 0;

		try {
			SAXBuilder parser = new SAXBuilder();
			Document doc = parser.build(new FileReader("src" + File.separator + "config.xml"));
			String res = doc.getRootElement().getAttributeValue("port");
			port = Integer.parseInt(res);
			conf = new ConfigurationBean(port);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

	}
}
