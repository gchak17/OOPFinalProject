package game;

import java.io.IOException;
import java.sql.Date;
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
	private Date date;

	public Round(int nthRound, ArrayList<Player> players, Player artist, String gameId) {
		this.nth = nthRound;
		this.players = players;
		this.artist = artist;
		this.gameId = gameId;
		this.date = new Date(System.currentTimeMillis());
		initMap();

		artist.startDrawing();
		sendNewRoundInformationsToSocket();

	}
	
	public Date getDate() {
		return this.date;
	}

	private void initMap() {
		points = new HashMap<Player, Integer>();
		playersGuessedTimes = new HashMap<Player, Integer>();
		for (Player p : players) {
			points.put(p, 0);
			playersGuessedTimes.put(p, 100);
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
		json.put("command", "showResults");
		for (Player p : players) {
			String user = p.getAccount().getUsername();
			int res = points.get(p);
			System.out.println(res);
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
		for (Player key : playersGuessedTimes.keySet()) {
			int res = 100 - playersGuessedTimes.get(key);
			points.put(key, res);
		}
	}

	public HashMap<Player, Integer> getPoints() {
		return points;
	}

}
