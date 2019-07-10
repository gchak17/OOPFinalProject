package game;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.websocket.EncodeException;
import org.json.JSONObject;
import managers.ConnectionManager;
import message.Message;

public class Game {
	private ArrayList<Player> players;
	private HashMap<Player, Integer> points;
	private Player roundStarterArtist;
	private String id;
	private int time;
	private int numRounds;
	private Timer betweenRoundsTimer;
	private Round round;
	private int roundCounter;
	private int artistIndex;

	public Game(ArrayList<Player> players, int round, int time, String id) {
		this.players = players;
		setGameForPlayers();
		this.numRounds = round;
		this.time = time;
		this.id = id;
		this.points = new HashMap<Player, Integer>();
		this.artistIndex = -1;
		this.roundCounter = 0;
		initMap();
		startNewRound();
	}

	private void initMap() {
		points = new HashMap<Player, Integer>();
		for(Player p : players) {
			points.put(p,0);
		}
	}

	private void setGameForPlayers() {
		for (Player p : players) {
			p.setGame(this);
		}
	}

	public String getId() {
		return this.id;
	}

	public Round getRound() {
		return round;
	}

	private void chooseStarterPainter() {
		if (artistIndex == -1) {
			artistIndex++;
		} else {
			artistIndex = players.indexOf(roundStarterArtist) + 1;
			if (artistIndex == players.size()) {
				artistIndex = 0;
			}
		}
		roundStarterArtist = players.get(artistIndex);
	}

	public void startNewRound() {
		roundCounter++;
		chooseStarterPainter();
		this.betweenRoundsTimer = null; // mgoni garbage collectors vumartivebt saqmes.. tu ara ? imena moshla xeliT
										// rogoraa ?
		Round r = new Round(roundStarterArtist);
		this.round = r;
	}

	public void endRound() {
		addRoundResToTotalScores();

		if (roundCounter == numRounds) {
			endGame();
		} else {

			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					startNewRound();
				}
			}, 10 * 1000);// aq roundidan amogebuli dro damchirdeba
		}
	}

	private void addRoundResToTotalScores() {
		Map<Player, Integer> roundPoints = round.getPoints();
		for (Player key : points.keySet()) {
			int prevRes = points.get(key);
			int res = roundPoints.get(key) + prevRes;
			points.put(key, res);
		}
	}

	private void endGame() {
		showFinalResults();

		ArrayList<Player> leftPlayers = playersWantToPlayAgain();
		if (leftPlayers.size() > 0) {
			// start game from 0
			this.players = leftPlayers;
			points = new HashMap<Player, Integer>();
			roundCounter = 0;
			chooseStarterPainter();
			startNewRound();
		}
	}

	private void showFinalResults() {
	}

	private ArrayList<Player> playersWantToPlayAgain() {
		// ask if they want to play again
		return new ArrayList<Player>();
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void removePlayer(Player cur) {
		players.remove(cur);
		points.remove(cur);
		round.removePlayer(cur);
	}

	public HashMap<Player, Integer> getPoints() {
		return points;
	}

	public class Round {
		private Player artist;
		private Map<Player, Long> playersGuessedTimesForSingleTurn;
		private boolean everybodyGuessed = false;
		private Map<Player, Integer> RoundPoints;
		private Map<Player, Integer> TurnPoints;
		private Date date;
		private Player roundStarter;
		private int turnCounter;
		private boolean roundIsnotEnded = true;
		private String chosenWord = "word";
		private Connection conn;
		private ArrayList<String> randomWords = new ArrayList<String>();

		public Round(Player starterArtist) {
			this.artist = starterArtist;
			this.roundStarter = starterArtist;
			this.date = new Date(System.currentTimeMillis());
			this.turnCounter = 0;
			initMaps();

			startTurn();
		}

		public String getChosenWord() {
			return chosenWord;
		}

		public void setChosenWord(String chosenWord) {
			this.chosenWord = chosenWord;
		}

		public Date getDate() {
			return this.date;
		}

		private void initMaps() {
			RoundPoints = new HashMap<Player, Integer>();
			playersGuessedTimesForSingleTurn = new HashMap<Player, Long>();
			TurnPoints = new HashMap<Player, Integer>();
			for (Player p : players) {
				RoundPoints.put(p, 0);
				playersGuessedTimesForSingleTurn.put(p, (long) 100);
				TurnPoints.put(p, 0);
			}
		}

		private void removePlayer(Player p) {
			RoundPoints.remove(p);
			playersGuessedTimesForSingleTurn.remove(p);
			TurnPoints.remove(p);
			if (p.equals(roundStarter)) {
				int ind = players.indexOf(p);
				roundStarter = players.get(++ind);
			}
		}

		public Map<Player, Integer> getPoints() {
			return RoundPoints;
		}

		private void choosePainter() {
			if (turnCounter != 1) {
				clearCanvas();

				int index = players.indexOf(artist) + 1;
				if (index == players.size())
					index = 0;

				artist = players.get(index);
				if (artist.equals(roundStarter)) {
					endRound();
					roundIsnotEnded = false;
				}
			}
		}

		public void endTurn() {
			artist.endDrawing();
			generatePointsForPlayers();
			sendPointsToWebSocket();

			startTurn();
		}

		private void startTurn() {
			turnCounter++;
			choosePainter();
			sendNewTurnInformationsToSocket();
			if (roundIsnotEnded) {
				try {
					generateThreeWordsAndChooseOne();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				artist.startDrawing();
			}
		}

		private void clearCanvas() {
			JSONObject json = new JSONObject();
			json.put("command", "clear");
			try {
				GameEndpoint.sendMessage(getId(), new Message(json));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (EncodeException e) {
				e.printStackTrace();
			}
		}

		private void generateThreeWordsAndChooseOne() throws SQLException {
			conn = ConnectionManager.getDBConnection();
			Statement st = conn.createStatement();
			String query = "SELECT * FROM words ORDER BY RAND() LIMIT 3;";
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				randomWords.add(rs.getString("word"));
			}
			System.out.println(randomWords);

		}

		private void sendNewTurnInformationsToSocket() {
			JSONObject json = new JSONObject();
			json.put("command", "newturn");
			System.out.println(points.size());
			

			json.put("artist", artist.toString()); // es gaawitlos an rame
			for (Player p : points.keySet()) {
				System.out.println(p.toString() + " : " + points.get(p));
				json.put(p.toString(), points.get(p));
			}
			try {
				GameEndpoint.sendMessage(getId(), new Message(json));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (EncodeException e) {
				e.printStackTrace();
			}
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
			for (Player p : RoundPoints.keySet()) {
				json.put(p.toString(), points.get(p) + " + " + TurnPoints.get(p));
			}
			
			try {
				GameEndpoint.sendMessage(getId(), new Message(json));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (EncodeException e) {
				e.printStackTrace();
			}
		}

		private void generatePointsForPlayers() {
			for (Player key : playersGuessedTimesForSingleTurn.keySet()) {
				// int res = 100 - playersGuessedTimesForSingleTurn.get(key);
				int res = new Random().nextInt(50);
				TurnPoints.put(key, res);
				RoundPoints.put(key, RoundPoints.get(key) + res);
			}
		}
	}
}