package game;

import java.io.Serializable;

public class Player implements Serializable {
	private User user;
	private Role role;
	private int position;
	private int points;

	public Player(User user, int position) {
		this.user = user;
		role = null;
		this.position = position;
		points = 0;
	}

	public User getUser() {
		return user;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	public int getPosition(){
		return position;
	}
	
	public int getPoints(){
		return points;
	}

	public void addPoints() {
		points += calcPoints();
	}

	public void removePoints() {
		points -= calcPoints();
	}

	public int calcPoints() {
		return (role instanceof Innocent) ? ((Innocent) role).getValue()
				: ((Innocent) ((Wolf) role).getTarget().role).getValue();
	}
	
	private int calcPointsTMP(){
		return 0;
	}
	
	public void send(Object message){
		user.send(message);
	}

	public Object recieve(){
		return user.recieve();
	}
	
	public void chooseRoles() {
		// TODO Auto-generated method stub

	}
}