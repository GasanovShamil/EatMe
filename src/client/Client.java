package client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.io.OutputStream;

public class Client {
	

	public static void main(String[] args) throws IOException, InterruptedException, ConnectException {

		Socket c = null;
		while (c == null) {
			try {
				c = new Socket("localhost", 8080);
				System.out.println("Client \n local port: " + c.getLocalPort() + ", Target port: " + c.getPort());
				System.out.println("connecté au serveur " + c.getInetAddress());
			} catch (ConnectException e) {
				Thread.sleep(1000);
			}
		}

		OutputStream os = c.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);

		ConnectionBean conect = new ConnectionBean(ConnectionType.AUTHENTICATE, "user", "pas");
		oos.writeObject(conect);
		oos.flush();
		oos.close();
		os.close();
		c.close();
	}
}
