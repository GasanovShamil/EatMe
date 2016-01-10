package main.client.model;

public class Game {
	Player[] players;
	Round round;
	boolean applyable;
	
	public Game(int nbPlayers){
		players = new Player[nbPlayers];
		applyable = true;
	}
	
	public boolean isApplyable()
	{
		return applyable;
	}
	
	public int getGameSize(){
		return players.length;
	}
}
