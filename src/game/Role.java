package game;

public abstract class Role {
	private String name;
	
	public Role(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public abstract boolean isWolf();
}