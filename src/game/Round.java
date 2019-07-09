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

	private HashMap<Player, Long> playersGuessedTimesForSingleTurn;

	private boolean everybodyGuessed = false;
	private HashMap<Player, Integer> points;
	private Game game;
	private Date date;

	private Connection conn;	
	private ArrayList<String> randomWords = new ArrayList<String>();

	private Player roundStarter;
	private int turnCounter;
	private boolean roundIsnotEnded = true;


	public Round(ArrayList<Player> players, Player artist, Game g) {

		this.players = players;
		this.artist = artist;
		this.roundStarter = artist;
		this.game = g;
		this.date = new Date(System.currentTimeMillis());
		this.turnCounter = 0;
		initMap();

		startTurn();
	}

	

	public Date getDate() {
		return this.date;
	}

	private void initMap() {
		points = new HashMap<Player, Integer>();


		playersGuessedTimesForSingleTurn = new HashMap<Player, Long>();

		for (Player p : players) {
			points.put(p, 0);

			playersGuessedTimesForSingleTurn.put(p, (long)100);

		}
	}

	public HashMap<Player, Integer> getPoints() {
		return points;
	}

	private void choosePainter() {
		if (turnCounter != 1) {
			clearCanvas();
			int index = 0;
			for (int i = 0; i < players.size(); i++) {
				if (players.get(i).equals(artist)) {
					index = i + 1;
					if (index == players.size())
						index = 0;
				}
			}
			artist = players.get(index);
			if (artist.equals(roundStarter)) {
				game.endRound();
				roundIsnotEnded = false;
			}
		}
	}

	public void endTurn() {
		System.out.println(artist + " is svla morcha");
		artist.endDrawing();
		generatePointsForPlayers();
		randomWords.clear();
		// sendPointsToWebSocket();

		startTurn();
	}

	private void startTurn() {
		
		turnCounter++;
		choosePainter();
		if (roundIsnotEnded) {
			try {
				generateThreeWordsAndChooseOne();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(this + " roomma daawyebia");
			artist.startDrawing();
			sendNewTurnInformationsToSocket();
		}
	}

	private void clearCanvas() {
		JSONObject json = new JSONObject();
		json.put("command", "clear");
		try {
			GameEndpoint.sendMessage(game.getId(), new Message(json));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EncodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void generateThreeWordsAndChooseOne() throws SQLException {
		conn = ConnectionManager.getDBConnection();
		Statement st  = conn.createStatement();
		String query = "SELECT * FROM words ORDER BY RAND() LIMIT 3;";
		ResultSet rs = st.executeQuery(query);
		while (rs.next()) {
			randomWords.add(rs.getString("word"));
		}
		System.out.println(randomWords);
	}

	private void sendNewTurnInformationsToSocket() {
		// aman prosta painteri vinaa is unda ganaaxlos dafaze
		// da sityvis zomis shesabamisi xazebi gamoachinos
		// System.out.println("vin xatavs ganaxlda dafaze da sityvis xazebi gamochnda");
	}



	public void smbdGuessed(Player p, long sec) throws SQLException {
		playersGuessedTimesForSingleTurn.put(p, sec);


		if (playersGuessedTimesForSingleTurn.size() == players.size()) {
			everybodyGuessed = true;
			artist.getTimer().cancel();
			endTurn();// es ar vici zustad sachiroa tu ara, sheidzleba taimerma tviton gaushvas mainc?
		}

	}

	private void sendPointsToWebSocket() {
		JSONObject json = new JSONObject();
		json.put("command", "showResults");
		for (Player p : players) {
			String user = p.getAccount().getUsername();
			int res = points.get(p);
			json.put(user, res);
		}

		Message newM = new Message(json);

		try {
			GameEndpoint.sendMessage(game.getId(), newM);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EncodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void generatePointsForPlayers() {
		for (Player key : playersGuessedTimesForSingleTurn.keySet()) {
			int res = (int) ((long)100 - playersGuessedTimesForSingleTurn.get(key));

			points.put(key, res);
		}
	}

}
