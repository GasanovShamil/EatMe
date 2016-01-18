package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Logger;

import client.ConnectionBean;
import enums.Message;
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
	private static Logger log = Logger.getLogger(ServerConnectionThread.class.getName());

	public ServerConnectionThread(Socket socket, ArrayList<ServerUserThread> users, Queue queue,
			Connection dbConnection) {
		this.users = users;
		this.socket = socket;
		this.queue = queue;
		this.dbConnection = dbConnection;
		try {
			input = new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void run() {
		while (!isInterrupted()) {
			boolean connected = true;
			try {
				socket.sendUrgentData(10);
			} catch (IOException e1) {
				connected = false;
			}

			try {
				User user = null;
				if (connected) {
					connectionBean = (ConnectionBean) input.readObject();

					if (connectionBean.getType() == Message.AUTHENTICATE) {
						user = authenticate();
					} else if (connectionBean.getType() == Message.CREATE_ACCOUNT) {
						user = createAccount();
					}
					if (user != null) {
						synchronized (queue) {
							serverUserThread = new ServerUserThread(user, queue, users);
						}
						serverUserThread.start();
						synchronized (users) {
							users.add(serverUserThread);
						}

						interrupt();
					}
				} else {
					interrupt();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				interrupt();
			}
		}
	}

	/**
	 * <p>
	 * Interroge la base de donnee pour trouver l'utilisateur
	 * </p>
	 * 
	 * @return Object User s'il y a un utilisateur dans BD et null sinon
	 * @throws IOException
	 */
	public User authenticate() throws SQLException, IOException {
		String query = "SELECT * FROM users WHERE username='" + connectionBean.getLogin() + "' AND password=MD5('"
				+ connectionBean.getPassword() + "');";
		Statement statement = dbConnection.createStatement();
		ResultSet resultSet = statement.executeQuery(query);
		if (resultSet.next()) {
			for (ServerUserThread sut : users) {
				if (sut.getUsername().equals(connectionBean.getLogin())) {
					output.writeObject(Message.ALREADY_IN_USE);
					output.flush();
					return null;
				}
			}
			log.info(resultSet.getString("username") + " vient de se connecter.\nConsole : ");

			output.writeObject(Message.SUCCESS);
			output.flush();
			return new User(resultSet.getString("username"), socket, input, output);
		} else {
			log.info("Identifiants incorrects. " + connectionBean.getLogin() + "\n Console: ");

			output.writeObject(Message.FAIL);
			output.flush();
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
				log.info(resultSet.getString("username") + " vient de se connecter.\nConsole : ");

				output.writeObject(Message.SUCCESS);
				output.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return new User(resultSet.getString("username"), socket, input, output);

		} else {
			log.info("Il y a deja un utilisateur avec ce nom : " + connectionBean.getLogin() + "\nConsole: ");
			try {
				output.writeObject(Message.EXIST);
				output.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
