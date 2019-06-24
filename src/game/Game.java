package game;

import java.util.List;

public class Game {
	private Player player1, player2;
	private Player artist, winner;
	private String id;
	private boolean isGameOver = false;
	
	public Game(Player player1, Player player2, String id) {
		this.player1 = player1;
		this.player2 = player2;
		this.id = id;
	}
	
	
}