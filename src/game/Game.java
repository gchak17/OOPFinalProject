package game;

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

import org.json.JSONObject;
import managers.ConnectionManager;
import message.Message;

public class Game {
	private ArrayList<Player> players;
	private HashMap<Player, Integer> points;
	private Player roundStarterArtist;
	private String id;
	private int secondsPerTurn;
	private int numRounds;
	private Timer startTurnTimer;
	private Timer newRoundTimer;
	private Round round;
	private int roundCounter;
	private int artistIndex;

	public Game(ArrayList<Player> players, int round, int secondsPerTurn, String id) {
		this.players = players;
		setGameForPlayers();
		this.numRounds = round;
		this.secondsPerTurn = secondsPerTurn;
		this.id = id;
		this.points = new HashMap<Player, Integer>();
		this.artistIndex = -1;
		this.roundCounter = 0;
		initMap();

		// appear players on canvas
		JSONObject json = new JSONObject();
		json.put("command", "appearplayers");
		for (Player p : players)
			json.put(p.toString(), 0);
		GameSocket.sendMessage(getId(), new Message(json));

		// javascript starts countdown from 5
//		JSONObject json1 = new JSONObject();
//		json1.put("command", "startgametimer");
//		try{ GameEndpoint.sendMessage(getId(), new Message(json1));
//		}catch (IOException | EncodeException e) { e.printStackTrace();}

		// game starts in 5 seconds
		Timer timer = new Timer();
		timer.schedule(new java.util.TimerTask() {
			public void run() {
				startNewRound();
			}
		}, 5000);
	}
	
	public int secsPerTurn() {
		return secondsPerTurn;
	}

	private void initMap() {
		points = new HashMap<Player, Integer>();
		for (Player p : players) {
			points.put(p, 0);
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
			if (roundStarterArtist == null) {
				roundStarterArtist = players.get(0);
			} else {
				artistIndex = players.indexOf(roundStarterArtist) + 1;
				if (artistIndex == players.size()) {
					artistIndex = 0;
				}
			}
		}
		roundStarterArtist = players.get(artistIndex);
	}

	public void startNewRound() {
		// System.out.println("axali raundi");
		roundCounter++;
		if (roundStarterArtist != null) roundStarterArtist.shouldBeArtist(false);
		chooseStarterPainter();
		// System.out.println(roundStarterArtist);
		// this.round = null; // mgoni garbage collectors vumartivebt saqmes.. tu ara ?
		// imena moshla xeliT
		// rogoraa ?
		this.round = new Round(roundStarterArtist);
	}

	public void endRound() {

		if (roundCounter == numRounds) {
			endGame();
		} else {

			newRoundTimer = new Timer();
			newRoundTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					startNewRound();
				}
			}, 10 * 1000);// aq tavidan minichebuli dro damchirdeba

		}
	}

	private void endGame() {
// <<<<<<< HEAD
		// showfinalresults and redirect to main jsp
		JSONObject json = new JSONObject();
		json.put("command", "endgame");
		GameSocket.sendMessage(id, new Message(json));
// =======
// 		if (players.size() != 0) {
// 			showFinalResults();

// 			ArrayList<Player> leftPlayers = playersWantToPlayAgain();
// 			if (leftPlayers.size() > 0) {
// 				// start game from 0
// 				this.players = leftPlayers;
// 				points = new HashMap<Player, Integer>();
// 				roundCounter = 0;
// 				chooseStarterPainter();
// 				startNewRound();
// 			}
// 		}
// 	}

// 	private void showFinalResults() {
// 		// aq mainc amoagdos rame didi fanjara
// 	}

// 	private ArrayList<Player> playersWantToPlayAgain() {
// 		// ask if they want to play again
// 		return new ArrayList<Player>();
// >>>>>>> 8aeaf297e8907d070eb6adca8641603a3a318c79
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void removePlayer(Player cur) {
		points.remove(cur);
		round.removePlayer(cur);
	}

	public HashMap<Player, Integer> getPoints() {
		return points;
	}

	public class Round {
		private Player artist;
		private Map<Player, Long> playersGuessedTimesForSingleTurn;
		private Map<Player, Integer> TurnPoints;
		private Date date;
		private int turnCounter;
		private boolean roundIsnotEnded = true;
		private String chosenWord; // = "word";
		private Connection conn;
		private ArrayList<String> randomWords = new ArrayList<String>();
		private Timer endTurnTimer;
		private boolean turnIsEnded;

		public Round(Player starterArtist) {
			// System.out.println("startter 2 " + starterArtist);
			this.artist = starterArtist;
			// this.date = new Date(System.currentTimeMillis());
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
		
		public Map<Player, Long> guessedTimes(){
			return playersGuessedTimesForSingleTurn;
		}

		private void initMaps() {
			playersGuessedTimesForSingleTurn = new HashMap<Player, Long>();
			TurnPoints = new HashMap<Player, Integer>();
			for (Player p : players) {
				playersGuessedTimesForSingleTurn.put(p, (long) secondsPerTurn);
				TurnPoints.put(p, 0);
			}
		}

		private void removePlayer(Player p) {
			playersGuessedTimesForSingleTurn.remove(p);
			TurnPoints.remove(p);
			if (p.equals(roundStarterArtist)) {
				int index = players.indexOf(p) - 1;
				if (index == -1) {
					roundStarterArtist = null;
				} else {
					roundStarterArtist = players.get(index);
				}
			} else if (p.equals(artist)) {
				endTurn();

				int index = players.indexOf(p) - 1;
				if (index == -1) {
					artist = null;
				} else {
					artist = players.get(index);
				}
			}
			players.remove(p);
			if (players.size() == 0) {
				turnOffTimers();
				if (newRoundTimer != null)
					newRoundTimer.cancel();
			}
		}

		private void choosePainter() {
			if (turnCounter != 1) {
				clearCanvas();

				if (artist == null) {
					artist = players.get(0);
				} else {
					int index = players.indexOf(artist) + 1;
					if (index == players.size())
						index = 0;

					artist = players.get(index);
					if (artist.equals(roundStarterArtist)) {
						artist.shouldBeArtist(false);
						turnOffTimers();
						endRound();
						roundIsnotEnded = false;
					}
				}
			}
		}

		private void turnOffTimers() {
			if (startTurnTimer != null)
				startTurnTimer.cancel();

			if (endTurnTimer != null)
				endTurnTimer.cancel();
		}

		public void endTurn() {
			turnIsEnded = true;
			JSONObject json = new JSONObject();
			json.put("command", "endturn");
			json.put("artist", artist.toString());
			GameSocket.sendMessage(artist.getGame().getId(), new Message(json));
			artist.endDrawing();

			generatePointsForPlayers();
			sendPointsToWebSocket();

			startTurnTimer = new Timer();
			startTurnTimer.schedule(new java.util.TimerTask() {
				public void run() {
					startTurn();
				}
			}, 5000);
		}

		private void startTurn() {
			turnIsEnded = false;
			turnCounter++;
			choosePainter();
			artist.shouldBeArtist(true);
			sendNewTurnInformationsToSocket();
			if (roundIsnotEnded) {
				try {
					generateThreeWordsAndChooseOne();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				artist.startDrawing();

				JSONObject json = new JSONObject();
				json.put("command", "startturn");
				json.put("artist", artist.toString());
				GameSocket.sendMessage(artist.getGame().getId(), new Message(json));

				this.date = new Date(System.currentTimeMillis());
				
				endTurnTimer = new Timer();
				endTurnTimer.schedule(new TimerTask() {
					@Override
					public void run() {
						endTurn();
					}
				}, secondsPerTurn * 1000);
			}
		}

		private void clearCanvas() {
			JSONObject json = new JSONObject();
			json.put("command", "clear");
			GameSocket.sendMessage(getId(), new Message(json));
		}

		private void generateThreeWordsAndChooseOne() throws SQLException {
			conn = ConnectionManager.getDBConnection();
			Statement st = conn.createStatement();
			String query = "SELECT * FROM words ORDER BY RAND() LIMIT 3;";
			ResultSet rs = st.executeQuery(query);
			randomWords = new ArrayList<String>();
			while (rs.next()) {
				randomWords.add(rs.getString("word"));
			}
			artist.shouldBeArtist(true);

			sendWordsToArtist();
		}

		private void sendWordsToArtist() {
			JSONObject json = new JSONObject();
			json.put("command", "chooseWord");

			json.put("one", randomWords.get(0));
			json.put("two", randomWords.get(1));
			json.put("three", randomWords.get(2));

			GameSocket.sendMessage(getId(), new Message(json));
		}

		private void sendNewTurnInformationsToSocket() {
			JSONObject json = new JSONObject();
			json.put("command", "newturn");
			// System.out.println(points.size());

			json.put("seconds", secondsPerTurn);
			json.put("artist", artist.toString()); // es gaawitlos an rame
			for (Player p : points.keySet()) {
				// System.out.println(p.toString() + " : " + points.get(p));
				json.put(p.toString(), points.get(p));
			}
			GameSocket.sendMessage(getId(), new Message(json));
		}

		public void smbdGuessed(Player p, long sec) throws SQLException {
			playersGuessedTimesForSingleTurn.put(p, sec);

			if (playersGuessedTimesForSingleTurn.size() == players.size() - 1) {
				endTurnTimer.cancel();
				endTurn();
			}

		}
		
		

		private void sendPointsToWebSocket() {
			JSONObject json = new JSONObject();

			json.put("command", "showResults");
			for (Player p : TurnPoints.keySet()) {
				int turenPoint = TurnPoints.get(p);
				json.put(p.toString(), points.get(p) + " + " + turenPoint);
				points.put(p, points.get(p) + turenPoint);
			}

			GameSocket.sendMessage(getId(), new Message(json));
		}

		private void generatePointsForPlayers() {
			int allPoints = 0;
			for (Player key : playersGuessedTimesForSingleTurn.keySet()) {
				int seconds = (int) (playersGuessedTimesForSingleTurn.get(key) / 1000);
				 int res = 100 * seconds/secondsPerTurn;
				 allPoints += res;
				 System.out.println(res);
				TurnPoints.put(key, res);
				key.addScore(res);
			}
			TurnPoints.put(artist, allPoints / playersGuessedTimesForSingleTurn.size() - 1);
		}
		
		public boolean isTurnEnded() {
			return turnIsEnded;
		}
	}
}