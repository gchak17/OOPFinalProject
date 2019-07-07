package game;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.websocket.EncodeException;

import org.json.JSONObject;

import com.mysql.cj.Session;

import message.Message;

public class Round {

	private int nth;
	private ArrayList<Player> players;
	private Player artist;
	private HashMap<Player, Integer> playersGuessedTimes;
	private boolean everybodyGuessed = false;
	private HashMap<Player, Integer> points;
	private String gameId;

	public Round(int nthRound, ArrayList<Player> players, Player artist, String gameId) {
		this.nth = nthRound;
		this.players = players;
		this.artist = artist;
		this.gameId = gameId;
		initMap();

		artist.startDrawing();
		sendNewRoundInformationsToSocket();

	}

	private void initMap() {
		points = new HashMap<Player, Integer>();
		for (Player p : players) {
			points.put(p, 0);
		}
	}

	private void sendNewRoundInformationsToSocket() {
		// TODO Auto-generated method stub

	}

	public void endRound() {
		System.out.println(artist.getAccount().getUsername() + "'s round is over");
		artist.endDrawing();
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
		JSONObject json = new JSONObject();
		json.put("type", "showResults");
		for (Player p : players) {
			String user = p.getAccount().getUsername();
			int res = points.get(p);
			json.put(user, res);
		}

		Message newM = new Message(json);

		try {
			GameEndpoint.sendMessage(gameId, newM);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EncodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void generatePointsForPlayers() {

	}

	public HashMap<Player, Integer> getPoints() {
		return points;
	}

}
