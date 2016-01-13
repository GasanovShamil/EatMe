package server;

import client.Message;
import game.User;

public class ServerUserThread extends Thread {
	private User user;
	private Queue queue;

	public ServerUserThread(User user, Queue queue) {
		this.user = user;
		this.queue=queue;
	}

	public void run() {
		Message msg = (Message) user.recieve();

		switch (msg) {
		case START_3P:
			System.out.println(user.getUsername()+" need game 3 p");
			synchronized(queue){
				queue.addUser(3, user);
			}
			break;
		case START_4P:

			break;
		case START_5P:

			break;
		case START_6P:

			break;
		case DECONNECT:

			break;
		default:
			break;
		}
	}
}
