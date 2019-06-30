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
		this.roundDuration = roundDuration;
		this.MaxPlayer = MaxPlayer;
		curPlayers = 1;
		players.add(new Player(admin));
	}
	
	public String toString() {
		return "Players: " + players.toString() + " Rounds: " + Rounds + " Time: " + roundDuration ; 
	}
	
	public boolean addPlayer(Player newPlayer) {
		if(curPlayers < MaxPlayer) {
			if(players.contains(newPlayer))return false;
			curPlayers++;
			players.add(newPlayer);
			return true;
		}
			//dastartvas vinc elodeba imattanac gamochndes.. da soketi gvinda albat iq
		
		return false;	
	}
	
	
	
}
