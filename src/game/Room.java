package game;


import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import dao.Account;
import managers.ConnectionManager;



public class Room {

	private Player admin;
	private int curPlayers;
	private int Rounds;
	private int roundDuration;
	private int MaxPlayer;
	private String roomId;
	private ArrayList<Player> players = new ArrayList<Player>();
	
	public Room(Player admin, int Rounds, int roundDuration, int MaxPlayer) {
		this.admin  = admin;
		this.Rounds = Rounds;
		this.roundDuration = roundDuration;
		this.MaxPlayer = MaxPlayer;
		
		
		curPlayers = 1;
		players.add(admin);
	}
	
	
	public String toString() {
		return "Players: " + players.toString() + " Rounds: " + Rounds + " Time: " + roundDuration ; 
	}
	
	public boolean addPlayer(Player newPlayer) {
		if(curPlayers < MaxPlayer) {
			if(players.contains(newPlayer))return false;
			curPlayers++;
			newPlayer.setRoom();
			players.add(newPlayer);
			
			return true;
		}
			//dastartvas vinc elodeba imattanac gamochndes.. da soketi gvinda albat iq
		
		return false;	
	}
	
	public ArrayList<Player> getPlayers(){
		return this.players;
	}
	
	public Player getAdmin() {
		return this.admin;
	}
	public int getRounds() {
		return this.Rounds;
	}
	
	public int getTime() {
		return this.roundDuration;
	}
	
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public void removePlayer(Player user) {
		// TODO Auto-generated method stub
		
	}
}
