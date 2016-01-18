package game;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <p>
 * Classe correspondant au rôle générique
 * </p>
 */
@SuppressWarnings("serial")
public abstract class Role implements Serializable{
	private String name;
	
	public Role(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public abstract boolean isWolf();
	
	public String toString(){
		return name;
	}
	
	public static ArrayList<Role> generateRoles(int size){
		ArrayList<Role> roles = new ArrayList<Role>();
		
		if (size >= 3) {
			roles.add(new Wolf());
			roles.add(new YoungKids());
			roles.add(new Pig());

			if (size >= 4) {
				roles.add(new LittleRedCap());

				if (size >= 5) {
					roles.add(new Pig());

					if (size == 6) {
						roles.add(new Pig());
					}
				}
			}
		}
		
		return roles;
	}
}