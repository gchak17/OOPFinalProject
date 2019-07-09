package game;

import java.util.ArrayList;

public class Room {

	private Player admin;
	private int curPlayers;
	private int Rounds;
	private int roundDuration;
	private int MaxPlayer;
	private String roomId;
	private ArrayList<Player> players = new ArrayList<Player>();

	public Room(Player admin, int Rounds, int roundDuration, int MaxPlayer) {
		this.admin = admin;
		this.Rounds = Rounds;
		this.roundDuration = roundDuration;
		this.MaxPlayer = MaxPlayer;
		this.curPlayers = 1;
		this.players.add(admin);
	}

	public boolean addPlayer(Player newPlayer) {
		if (curPlayers < MaxPlayer) {
			if (players.contains(newPlayer))
				return false;
			curPlayers++;
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

	public int getTime() {
		return this.roundDuration;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getRoomId() {
		return this.roomId;
	}

	public void removePlayer(Player user) {
		players.remove(user);
	}

	public String toString() {
		return "Created by: " + admin.toString() + ", " + players.size() + " players, \n" + "Number of Rounds: "
				+ Rounds + ", Round Duration: " + roundDuration;
	}
}
