package game;

public class Player {
	private String username;
	private Role role;
	private Player[] players;
	private int points;
	private int position;

	public Player(String username, Player[] players, int position) {
		this.username = username;
		role = null;
		this.players = players;
		points = 0;
		this.position = position;
	}

	public String getUsername() {
		return username;
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