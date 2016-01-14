package game;

import java.io.Serializable;

public abstract class Role implements Serializable{
	private String name;
	
	public Role(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public abstract boolean isWolf();
}