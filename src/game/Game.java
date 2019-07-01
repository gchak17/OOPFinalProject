package game;

import java.util.ArrayList;

public class Game {
	ArrayList<Player> players;
	private Player artist, winner;
	private String id;
	private int maxPlayers;
	private int time;
	private int numRounds;
	
	private boolean isGameOver = false;
	
	public Game(ArrayList<Player> players, int round, int time) {
		this.players = players;			
		this.numRounds = round;
		this.time = time;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	

	
	
}