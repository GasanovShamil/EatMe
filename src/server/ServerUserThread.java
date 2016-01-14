package server;

import enums.StartGameType;
import game.User;

public class ServerUserThread extends Thread {
	private User user;
	private Queue queue;

	public ServerUserThread(User user, Queue queue) {
		this.user = user;
		this.queue=queue;
	}

	public void run() {
		StartGameType msg = (StartGameType) user.recieve();

		switch (msg) {
		case START_3P:
			System.out.println(user.getUsername()+" need game 3 p");
			synchronized(queue){
				queue.addUser(3, user);
			}
			break;
		case START_4P:
			System.out.println(user.getUsername()+" need game 4 p");
			synchronized(queue){
				queue.addUser(4, user);
			}
			break;
		case START_5P:
			System.out.println(user.getUsername()+" need game 5 p");
			synchronized(queue){
				queue.addUser(5, user);
			}
			break;
		case START_6P:
			System.out.println(user.getUsername()+" need game 6 p");
			synchronized(queue){
				queue.addUser(6, user);
			}
			break;
		case DECONNECT:

			break;
		default:
			break;
		}
	}
}
