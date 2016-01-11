package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import client.ConnectionBean;
import client.ConnectionType;

public class ServerAuthenticationThread extends Thread {
	private Connection dbConnection = null;
	private Statement statement = null;
	private ResultSet resultSet = null;
	private Properties prop;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private ConnectionBean connectionBean;
	private Socket s;

	public ServerAuthenticationThread(Socket s) {
		this.s = s;
		prop = new Properties();
		prop.put("characterEncoding", "UTF8");
		prop.put("user", "root");
		prop.put("password", "root");
		try {
			input = new ObjectInputStream(s.getInputStream());
			output = new ObjectOutputStream(s.getOutputStream());
			connectionBean = (ConnectionBean) input.readObject();
		} catch (IOException | ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void run() {

		try {
			dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/eatme", prop);
			statement = dbConnection.createStatement();
			if (connectionBean.getType() == ConnectionType.AUTHENTICATE) {
				authenticate();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * <p>
	 * Interroge la base de donnee pour trouver l'utilisateur
	 * </p>
	 * 
	 * @return true s'il y a un utilisateur dans BD et false sinon
	 */
	public boolean authenticate() throws SQLException {
		String query = "SELECT * FROM users WHERE username='" + connectionBean.getLogin() + "' AND password=MD5('"
				+ connectionBean.getPassword() + "');";
		resultSet = statement.executeQuery(query);
		if (resultSet.next()) {
			System.out.println("Hello " + resultSet.getString("username") + "\n Console: ");
			return true;
		} else {
			System.out.println("There is no user with this name: " + connectionBean.getLogin() + "\n Console: ");
			return false;
		}
	}

	public boolean createAccount() {
		return true;
	}
}
