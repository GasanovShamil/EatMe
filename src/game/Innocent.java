package game;

/**
 * <p>
 * Classe correspondant aux rôles autres que loup
 * </p>
 */
@SuppressWarnings("serial")
public abstract class Innocent extends Role {
	private int value;
	private boolean trap;

	public Innocent(String name, int value) {
		super(name);
		this.value = value;
		trap = false;
	}

	public int getValue() {
		return value;
	}

	public boolean isTrap() {
		return trap;
	}

	public void sleep() {
		trap = false;
	}

	public void putTrap() {
		trap = true;
	}
	
	public boolean isWolf(){
		return false;
	}
}

/**
 * <p>
 * Classe correspondant au rôle de cochon
 * </p>
 */
@SuppressWarnings("serial")
class Pig extends Innocent {
	public Pig() {
		super("Cochon", 1);
	}
}

/**
 * <p>
 * Classe correspondant au rôle du petit chaperon rouge
 * </p>
 */
@SuppressWarnings("serial")
class LittleRedCap extends Innocent {
	public LittleRedCap() {
		super("Petit Chaperon Rouge", 2);
	}
}

/**
 * <p>
 * Classe correspondant au rôle des chevreaux
 * </p>
 */
@SuppressWarnings("serial")
class YoungKids extends Innocent {
	public YoungKids() {
		super("Chevreaux", 3);
	}
}