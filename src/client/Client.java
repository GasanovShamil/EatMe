package client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;

public class Client {

	public static void main(String[] args) throws IOException, InterruptedException, ConnectException {

		Socket c = null;
		while (c == null) {
			try {
				c = new Socket("localhost", 8080);
				System.out.println("Client\n Local port: " + c.getLocalPort() + " - Target port: " + c.getPort());
				System.out.println("connecté au serveur " + c.getInetAddress());
			} catch (ConnectException e) {
				Thread.sleep(1000);
			}
		}

		ObjectOutputStream oos = new ObjectOutputStream(c.getOutputStream());
		oos.writeObject(new ConnectionBean(ConnectionType.AUTHENTICATE, "milan", "milan"));
		oos.flush();
		oos.close();
		c.close();
	}
}
