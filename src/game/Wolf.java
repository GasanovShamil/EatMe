package game;

/**
 * <p>
 * Classe correspondant au rôle de loup
 * </p>
 */
@SuppressWarnings("serial")
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
	
	public boolean isWolf(){
		return true;
	}
}