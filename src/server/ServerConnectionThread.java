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
import enums.ConnectionMessageType;
import game.User;

public class ServerConnectionThread extends Thread {
	private Connection dbConnection = null;
	private ArrayList<ServerUserThread> users = null;
	private Queue queue = null;
	private ObjectInputStream input = null;
	private ObjectOutputStream output = null;
	private ConnectionBean connectionBean = null;
	private Socket socket = null;
	private ServerUserThread serverUserThread = null;

	public ServerConnectionThread(Socket socket, ArrayList<ServerUserThread> users, Queue queue) {
		this.users = users;
		this.socket = socket;
		this.queue = queue;
		try {
			input = new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());

		} catch (IOException e1) {
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
			User user = null;
			connectionBean = (ConnectionBean) input.readObject();
			if (connectionBean.getType() == ConnectionMessageType.AUTHENTICATE) {
				user = authenticate();
			} else if (connectionBean.getType() == ConnectionMessageType.CREATE_ACCOUNT) {
				user = createAccount();
			} else {
				socket.close();
				interrupt();
			}
			if (user != null) {
				synchronized (queue) {
					serverUserThread = new ServerUserThread(user, queue);
				}
				serverUserThread.start();
				synchronized (users) {
					users.add(serverUserThread);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * <p>
	 * Interroge la base de donnee pour trouver l'utilisateur
	 * </p>
	 * 
	 * @return Object User s'il y a un utilisateur dans BD et null sinon
	 */
	public User authenticate() throws SQLException {
		String query = "SELECT * FROM users WHERE username='" + connectionBean.getLogin() + "' AND password=MD5('"
				+ connectionBean.getPassword() + "');";
		Statement statement = dbConnection.createStatement();
		ResultSet resultSet = statement.executeQuery(query);
		if (resultSet.next()) {
			System.out.print(resultSet.getString("username") + " vient de se connecter.\nConsole : ");

			try {
				output.writeObject(ConnectionMessageType.SUCCESS);
				output.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return new User(resultSet.getString("username"), socket, input, output);
		} else {
			System.out.println("Identifiants incorrects. " + connectionBean.getLogin() + "\n Console: ");

			try {
				output.writeObject(ConnectionMessageType.FAIL);
				output.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	/**
	 * <p>
	 * Créer un compte sur la base de donnee
	 * </p>
	 * 
	 * @return Object User dans le cas de succes et null sinon
	 */
	public User createAccount() throws SQLException {
		String query = "SELECT * FROM users WHERE username='" + connectionBean.getLogin() + "';";
		Statement statement = dbConnection.createStatement();
		ResultSet resultSet = statement.executeQuery(query);
		if (!resultSet.next()) {
			query = "INSERT INTO users (username, password) VALUE ('" + connectionBean.getLogin() + "', MD5('"
					+ connectionBean.getPassword() + "'));";
			statement.executeUpdate(query);

			try {
				query = "SELECT * FROM users WHERE username='" + connectionBean.getLogin() + "' AND password=MD5('"
						+ connectionBean.getPassword() + "');";
				resultSet = statement.executeQuery(query);
				resultSet.next();
				System.out.print(resultSet.getString("username") + " vient de se connecter.\nConsole : ");

				output.writeObject(ConnectionMessageType.SUCCESS);
				output.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return new User(resultSet.getString("username"), socket, input, output);

		} else {
			System.out.println("Il y a deja un utilisateur avec ce nom : " + connectionBean.getLogin() + "\nConsole: ");
			try {
				output.writeObject(ConnectionMessageType.EXIST);
				output.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
