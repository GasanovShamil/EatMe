package main.sever;

import java.io.File;
import java.io.FileReader;
import java.net.Socket;
import java.util.ArrayList;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;

import main.client.model.User;

public class Server {
	private ArrayList<User> users;
	private ArrayList<Socket> sockets;
	private static ConfigurationBean conf;
	
	public static void main(String[] args){
		setConf();
		System.out.println(conf.getPort());
	}
	
	
	
	
	
	public static void setConf(){
		int port=0;
		
		try{
			SAXBuilder parser=new SAXBuilder();
			FileReader fr=new FileReader("main"+File.separator+"server"+File.separator+"config.xml");
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
