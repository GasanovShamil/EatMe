package main.client.model;




import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.io.OutputStream;


public class Client {
	private final int CREATE_ACCOUNT=0;
	private final static int AUTHENTICATE=1;

	//public ConnectionBean Authentificate(){

	//	ConnectionBean c= new ConnectionBean(AUTHENTICATE, "user", "pas");
	//	return c;
	//}

	//public static  ConnectionBean CreateAccount(){
	//	ConnectionBean c= new ConnectionBean(CREATE_ACCOUNT, "test", "mdp");
	//	return c;
	//}

	public static void main(String[] args) throws IOException, InterruptedException, ConnectException {
		Socket c=null;
		while(c == null){
			try{
				c = new Socket("localhost",8080);
				System.out.println("Client \n local port: "+c.getLocalPort()+", Target port: "+c.getPort());

			}catch(ConnectException e){
				Thread.sleep(1000);	
			}
		}
		OutputStream os = c.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);

		ConnectionBean conect = new ConnectionBean(AUTHENTICATE, "user", "pas");
		oos.writeObject(conect);
		oos.close();
		os.close();
		c.close();
	}
}
