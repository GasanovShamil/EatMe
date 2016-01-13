package game;

public class Player {
	private User user;
	private Role role;
	private int points;
	private int position;

	public Player(User user, int position) {
		this.user = user;
		role = null;
		points = 0;
		this.position = position;
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

	public void addPoints() {
		points += calcPoints();
	}

	public void removePoints() {
		points -= calcPoints();
	}

	private int calcPoints() {
		return (role instanceof Innocent) ? ((Innocent) role).getValue()
				: ((Innocent) ((Wolf) role).getTarget().role).getValue();
	}

	public void chooseRoles() {
		// TODO Auto-generated method stub

	}
}