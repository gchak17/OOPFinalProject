package game;

import java.util.ArrayList;

public class Game {
	private ArrayList<Player> players;
	private Player artist, winner;
	private String id;
	private int maxPlayers;
	private int time;
	private int numRounds;
	private int painterIndex;
	
	private boolean isGameOver = false;
	
	public Game(ArrayList<Player> players, int round, int time, String id) {
		this.players = players;			
		this.numRounds = round;
		this.time = time;
		this.id = id;
		painterIndex = 0;
		choosePainter();
	}
	
	public String getId() {
		return this.id;
	}
	
	private void choosePainter() {
		while(true) {
			if(players.get(painterIndex).isInGame()) {
				players.get(painterIndex).startDrawing();
				painterIndex++;
				break;
			}
			painterIndex++;
		}
	}
	

	
	
}