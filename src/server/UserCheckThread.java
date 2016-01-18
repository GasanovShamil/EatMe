package server;

import java.util.ArrayList;

public class UserCheckThread extends Thread {
	private ArrayList<ServerUserThread> usersThreads;

	public UserCheckThread(ArrayList<ServerUserThread> usersThreads) {
		this.usersThreads = usersThreads;
	}

	public void run() {
		while (!isInterrupted()) {

			for (int i = 0; i < usersThreads.size(); i++) {
				ServerUserThread sut = usersThreads.get(i);
				if (sut.isInterrupted() || !sut.getUser().isConnected()) {
					synchronized (usersThreads) {
						usersThreads.remove(sut);
					}
				}
			}
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				interrupt();
			}
		}
	}
}
