package server;

import java.util.ArrayList;
import java.util.logging.Logger;

import enums.Message;
import game.User;

public class ServerUserThread extends Thread {
	private User user;
	private Queue queue;
	private ArrayList<ServerUserThread> usersThreads;
	private static Logger log = Logger.getLogger(ServerUserThread.class.getName());
	
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
				}
				break;
			case START_4P:
				synchronized (queue) {
					queue.addUser(4, user);
				}
				break;
			case START_5P:
				synchronized (queue) {
					queue.addUser(5, user);
				}
				break;
			case START_6P:
				synchronized (queue) {
					queue.addUser(6, user);
				}
				break;
			case DISCONNECT:
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
					log.info("ServerUserThread : Connection lost - " + user.getUsername());
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
	
	public User getUser(){
		return user;
	}
}
