package game;

import java.util.ArrayList;

public class Room {
	private Player admin;
	private int Rounds;
	private int turnDuration;
	private int MaxPlayer;
	private String roomId;
	private ArrayList<Player> players = new ArrayList<Player>();
	private boolean isWaiting;

	public Room(Player admin, int Rounds, int turnDuration, int MaxPlayer){
		this.admin = admin;
		this.Rounds = Rounds;
		this.turnDuration = turnDuration;
		this.MaxPlayer = MaxPlayer;
		this.players.add(admin);
		this.isWaiting = true;
	}

	public boolean addPlayer(Player newPlayer) {
		if (players.size() < MaxPlayer) {
			if (players.contains(newPlayer)) return false;
			newPlayer.setRoom(this);
			players.add(newPlayer);
			return true;
		}
		return false;
	}

	public ArrayList<Player> getPlayers() {
		return this.players;
	}

	public Player getAdmin() {
		return this.admin;
	}

	public int getRounds() {
		return this.Rounds;
	}

	public int getTurnDuration() {
		return this.turnDuration;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getRoomId() {
		return this.roomId;
	}

	public void removePlayer(Player user) {
		if(!players.isEmpty()) {
			players.remove(user);
		}
	}

	public String toString() {
		return "Created by: " + admin.toString() + ", " + players.size() + " players, \n" + "Number of Rounds: "
				+ Rounds + ", Turn Duration: " + turnDuration;
	}
	
	public boolean isEmpty() {
		return players.isEmpty();
	}
	
	public boolean isWaitingRoom() {
		return isWaiting;
	}
	
	public void setStarted() {
		isWaiting = false;
	}
}