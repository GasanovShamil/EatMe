package main.server.threads;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import main.client.model.User;

public class ServerConnectionThread extends Thread{
	private ArrayList<User> users;
	private ArrayList<Socket> sockets;
	private ServerSocket serverSocket;
	private Socket socket;
	
	
	public ServerConnectionThread(int port) throws IOException{
		serverSocket=new ServerSocket(port);
	}
	
	public void run(){
		while(!this.isInterrupted()){
			try {
				socket=serverSocket.accept();
				ServerAuthenticationThread sat=new ServerAuthenticationThread(socket);
				sat.start();
			} catch (IOException e) {
				this.interrupt();
				e.printStackTrace();
			}
		}
	}
}
