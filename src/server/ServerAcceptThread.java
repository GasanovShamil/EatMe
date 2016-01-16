package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

public class ServerAcceptThread extends Thread {
	private ArrayList<ServerUserThread> users;
	private Queue queue;
	private ServerSocket serverSocket;
	private ServerConnectionThread serverConnectionThread;
	//private static Logger log = Logger.getLogger(ServerAcceptThread.class.getName());
	
	public ServerAcceptThread(int port, ArrayList<ServerUserThread> users, Queue queue) throws IOException {
		this.users = users;
		this.queue = queue;
		serverSocket = new ServerSocket(port);
	}

	public void run() {
		Socket socket = null;
		while (!this.isInterrupted()) {
			try {
				socket = serverSocket.accept();
				synchronized (queue) {
					serverConnectionThread = new ServerConnectionThread(socket, users, queue);
				}
				//log.warning("new connection recieved");
				serverConnectionThread.start();

			} catch (IOException e) {
				this.interrupt();
				e.printStackTrace();
			}
			socket = null;
		}
	}
}
