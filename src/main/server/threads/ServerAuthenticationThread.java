package main.server.threads;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import main.client.model.ConnectionBean;

public class ServerAuthenticationThread extends Thread {
	private Connection dbConnection = null;
	private Statement statement = null;
	private ResultSet resultSet = null;
	private Properties prop;
	ConnectionBean connectionBean;
	Socket s;

	public ServerAuthenticationThread(Socket s) {
		this.s = s;
	}

	public void run() {

		try {
			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
			connectionBean=(ConnectionBean)ois.readObject();
			System.out.println(connectionBean.getLogin());
		} catch (IOException | ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		prop = new Properties();
		prop.put("characterEncoding", "UTF8");
		prop.put("user", "root");
		prop.put("password", "root");

		try {
			dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbusers", prop);
			statement = dbConnection.createStatement();
			String query = "SELECT * FROM users WHERE username=\""+connectionBean.getLogin()+"\"";
			resultSet = statement.executeQuery(query);
			if (resultSet.next()){
				System.out.println("Hello "+resultSet.getString("username")+"");
			}else{
				System.out.println("There is no user with this name: "+connectionBean.getLogin());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
