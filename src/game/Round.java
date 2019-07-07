package game;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Round {

	private int nth;
	private ArrayList<Player> players;
	private Player artist;
	private HashMap<Player, Integer> playersGuessedTimes;
	private boolean everybodyGuessed = false;
	private HashMap<Player, Integer> points;

	public Round(int nthRound, ArrayList<Player> players, Player artist) {
		this.nth = nthRound;
		this.players = players;
		this.artist = artist;
		artist.startDrawing();
		sendNewRoundInformationsToSocket();

	}

	private void sendNewRoundInformationsToSocket() {
		// TODO Auto-generated method stub

	}

	public void endRound() {
		System.out.println(artist.getAccount().getUsername() + "'s round is over");

		generatePointsForPlayers();
		sendPointsToWebSocket();

	}

	public void smbdGuessed(Player p, int sec) throws SQLException {
		playersGuessedTimes.put(p, sec);
		
		if (playersGuessedTimes.size() == players.size()) {
			everybodyGuessed = true;
			artist.getTimer().cancel();
			endRound();// es ar vici zustad sachiroa tu ara, sheidzleba taimerma tviton gaushvas mainc?
		}

	}

	private void sendPointsToWebSocket() {
		// TODO Auto-generated method stub

	}

	private void generatePointsForPlayers() {
		// TODO Auto-generated method stub

	}
	
	public HashMap<Player, Integer> getPoints(){
		return points;
	}

}
