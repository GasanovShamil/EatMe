package server;

import enums.Message;
import game.User;

public class ServerUserThread extends Thread {
	private User user;
	private Queue queue;

	public ServerUserThread(User user, Queue queue) {
		this.user = user;
		this.queue = queue;
	}

	public void run() {
		while (!isInterrupted()) {
			Message msg = (Message) user.recieve();

			switch (msg) {
			case START_3P:
				synchronized (queue) {
					queue.addUser(3, user);
					System.out.println(user.getUsername() + " need game 3 p");
				}
				break;
			case START_4P:
				synchronized (queue) {
					queue.addUser(4, user);
					System.out.println(user.getUsername() + " need game 4 p");
				}
				break;
			case START_5P:
				synchronized (queue) {
					queue.addUser(5, user);
					System.out.println(user.getUsername() + " need game 5 p");
				}
				break;
			case START_6P:
				synchronized (queue) {
					queue.addUser(6, user);
					System.out.println(user.getUsername() + " need game 6 p");
				}
				break;
			case DECONNECT:
				//interrupt();
				break;
			default:
				break;
			}
			
			synchronized(user){
				try {
					user.wait();
				} catch (InterruptedException e) {
					System.out.println("ServerUserThread : Connection lost - "+ user.getUsername());
				}
			}
		}
	}
}
