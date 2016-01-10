package main.client.model;

public class Player {
	User user;
	Role role;

	public Player(User user) {
		this.user = user;
		role = null;
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
}