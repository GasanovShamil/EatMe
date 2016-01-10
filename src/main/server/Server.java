package main.server;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import main.client.model.User;
import main.server.threads.Connection;

public class Server {
	private ArrayList<User> users;
	private ArrayList<Socket> sockets;
	private static ConfigurationBean conf;
	
	public static void main(String[] args) throws IOException, InterruptedException{
		setConf();
		Connection connection= new Connection(conf.getPort());
		System.out.println("Server started at port N:"+conf.getPort());
		connection.start();
		Scanner sc=new Scanner(System.in);
		System.out.print("Console: ");
		String s=sc.nextLine();
		switch(s){
		case "exit": 
			System.exit(0);
			break;
		default: System.out.println(s);
		}
	}
	
	
	
	
	 /**
	  * <p>Lis le fichier conf.xml pour recuperer les parametres de la configuration du serveur.</p>
	  * @return ConfigurationBean- object pour configurer le serveur
	  */
	public static void setConf(){
		int port=0;
		
		try{
			SAXBuilder parser=new SAXBuilder();
			FileReader fr=new FileReader("src"+File.separator+"main"+File.separator+"server"+File.separator+"config.xml");
			Document Doc=parser.build(fr);
			String res=Doc.getRootElement().getAttributeValue("port");
			port=Integer.parseInt(res);
			conf=new ConfigurationBean(port);		
		}
		catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
		
	}
}
