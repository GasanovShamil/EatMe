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
import java.util.ArrayList;
import java.util.Properties;

import client.ConnectionBean;
import client.ConnectionType;
import game.User;

public class ServerAuthenticationThread extends Thread {
	private Connection dbConnection = null;
	private Statement statement = null;
	private ResultSet resultSet = null;
	private ArrayList<User> users;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private ConnectionBean connectionBean;
	private Socket socket;

	public ServerAuthenticationThread(Socket socket, ArrayList<User> users) {
		this.socket = socket;
		try {
			input = new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());
			connectionBean = (ConnectionBean) input.readObject();
		} catch (IOException | ClassNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	public void run() {
		try {
			Properties prop = new Properties();
			prop.put("characterEncoding", "UTF8");
			prop.put("user", "root");
			prop.put("password", "root");
			dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/eatme", prop);
			statement = dbConnection.createStatement();
			if (connectionBean.getType() == ConnectionType.AUTHENTICATE) {
				authenticate();
			}
		} catch (SQLException e) {
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
			System.out.print(resultSet.getString("username") + " vient de se connecter.\nConsole : ");
			User u = new User(resultSet.getString("username"), socket);
			try {
				synchronized (users) {
					users.add(u);
				}
				output.writeObject(u);
				output.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		} else {
			System.out.println("There is no user with this name: " + connectionBean.getLogin() + "\n Console: ");
			User u = null;
			try {
				output.writeObject(u);
				output.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}
	}

	public boolean createAccount() throws SQLException {
		String query = "SELECT * FROM users WHERE username='" + connectionBean.getLogin() + "';";
		resultSet = statement.executeQuery(query);
		if (!resultSet.next()) {
			query = "INSERT INTO users (username, password) VALUE ('" + connectionBean.getLogin() + "', MD5('"
					+ connectionBean.getPassword() + "'));";
			resultSet = statement.executeQuery(query);
			System.out.println("Hello " + resultSet.getString("username") + "\n Console: ");
			User user = new User(resultSet.getString("username"), socket);
			try {
				synchronized (users) {
					users.add(user);
				}
				output.writeObject(user);
				output.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		} else {
			System.out.println("Il y a deja un utilisateur avec ce nom : " + connectionBean.getLogin() + "\nConsole: ");
			return false;
		}
	}
}
