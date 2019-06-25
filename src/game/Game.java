package game;

import java.util.ArrayList;

public class Game {
	ArrayList<Player> players;
	private Player artist, winner;
	private String id;
	private boolean isGameOver = false;
	
	public Game(ArrayList<Player> players, String id) {
		this.players = players;
		this.id = id;
	}
	
	
}