package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import game.User;

public class ServerUserThread extends Thread{
	private User user;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	
	public ServerUserThread(User user){
		this.user=user;
		try {
			input = new ObjectInputStream(user.getSocket().getInputStream());
			output = new ObjectOutputStream(user.getSocket().getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		
	}
}
