package server;

import java.util.ArrayList;

import enums.Message;
import game.User;

public class ServerUserThread extends Thread {
	private User user;
	private Queue queue;
	private ArrayList<ServerUserThread> usersThreads;

	public ServerUserThread(User user, Queue queue, ArrayList<ServerUserThread> usersThreads) {
		this.user = user;
		this.queue = queue;
		this.usersThreads = usersThreads;
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
				synchronized (usersThreads) {
					usersThreads.remove(this);
				}
				interrupt();
				break;

			case CONNECTION_LOST:
				synchronized (usersThreads) {
					usersThreads.remove(this);
				}
				interrupt();
				break;

			default:
				break;
			}

			synchronized (user) {
				try {
					user.wait();
				} catch (InterruptedException e) {
					System.out.println("ServerUserThread : Connection lost - " + user.getUsername());
					synchronized (usersThreads) {
						usersThreads.remove(this);
					}
					interrupt();
				}
			}
		}
	}

	public String getUsername() {
		return user.getUsername();
	}
}
