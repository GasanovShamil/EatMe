package game;

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

class Pig extends Innocent {
	public Pig() {
		super("Cochon", 1);
	}
}

class LittleRedCap extends Innocent {
	public LittleRedCap() {
		super("Petit Chaperon Rouge", 2);
	}
}

class YoungKids extends Innocent {
	public YoungKids() {
		super("Chevreaux", 3);
	}
}