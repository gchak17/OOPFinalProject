package game;


import java.util.ArrayList;

import account.Account;



public class Room {

	
	Account admin;
	int curPlayers;
	int Rounds;
	int roundDuration;
	int MaxPlayer;
	private ArrayList<Player> players = new ArrayList<Player>();
	
	public Room(Account admin, int Rounds, int roundDuration, int MaxPlayer) {
		this.admin  = admin;
		this.Rounds = Rounds;
		this.MaxPlayer = MaxPlayer;
		curPlayers = 1;
		players.add(new Player(admin));
		
	}
	
	public String toString() {
		
		return "numPlayers: " + curPlayers + " Rounds: " + Rounds + " Admin: " + admin.getUsername(); 
	}
	
}
