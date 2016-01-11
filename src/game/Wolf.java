package game;

public class Wolf extends Role {
	Player target;

	public Wolf() {
		super("Loup");
		target = null;
	}
	
	public Player getTarget(){
		return target;
	}

	public void bite(Player target) {
		this.target = target;
	}
}