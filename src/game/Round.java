package game;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.websocket.EncodeException;

import org.json.JSONObject;

import com.mysql.cj.Session;

import managers.ConnectionManager;
import message.Message;

public class Round {

	private int nth;
	private ArrayList<Player> players;
	private Player artist;
	private HashMap<Player, Long> playersGuessedTimes;
	private boolean everybodyGuessed = false;
	private HashMap<Player, Integer> points;
	private String gameId;
	private Date date;
	
	private Connection conn;	
	private ArrayList<String> randomWords = new ArrayList<String>();

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
	
	private void SelectRandomWord() throws SQLException {
		conn = ConnectionManager.getDBConnection();
		Statement st  = conn.createStatement();
		String query = "SELECT * FROM words ORDER BY RAND() LIMIT 3;";
		ResultSet rs = st.executeQuery(query);
		while (rs.next()) {
			randomWords.add(rs.getString("word"));
		}
		System.out.println(randomWords);
	}
	
	public Date getDate() {
		return this.date;
	}

	private void initMap() {
		points = new HashMap<Player, Integer>();
		playersGuessedTimes = new HashMap<Player, Long>();
		for (Player p : players) {
			points.put(p, 0);
			playersGuessedTimes.put(p, (long)100);
		}
	}

	private void sendNewRoundInformationsToSocket() {
		// aman prosta painteri vinaa is unda ganaaxlos dafaze
		// da sityvis zomis shesabamisi xazebi gamoachinos
	}

	public void endRound() {
		System.out.println(artist.getAccount().getUsername() + "'s round is over");
		artist.endDrawing();
		generatePointsForPlayers();
		sendPointsToWebSocket();

	}

	public void smbdGuessed(Player p, long sec) throws SQLException {
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
			int res = (int) ((long)100 - playersGuessedTimes.get(key));
			points.put(key, res);
		}
	}

	public HashMap<Player, Integer> getPoints() {
		return points;
	}

}
