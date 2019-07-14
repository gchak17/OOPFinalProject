package game;

import java.sql.Connection;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.websocket.Session;

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
		this.roundStarterArtist = players.get(0);
		initMapGame();

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
		}, 5 * 1000);
	}
	
	public int secsPerTurn() {
		return secondsPerTurn;
	}

	private void initMapGame() {
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
		System.out.println(roundCounter + " daa " + numRounds);
		//if (roundStarterArtist != null) roundStarterArtist.shouldBeArtist(false);
		//chooseStarterPainter();
		// System.out.println(roundStarterArtist);
		// this.round = null; // mgoni garbage collectors vumartivebt saqmes.. tu ara ?
		// imena moshla xeliT
		// rogoraa ?
		this.round = new Round(roundStarterArtist);
	}

	public void endRound() {
		System.out.println("vai: " + roundCounter);
		if (roundCounter == numRounds) {
			System.out.println("aq var: " + roundCounter);
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

	private void endGame(){
		//showfinalresults
		//System.out.println("cocxali var");
		JSONObject json = new JSONObject();
		json.put("command", "finalresults");
		for(Player p : points.keySet()) {
			json.put(p.toString(), points.get(p));
		}
		GameSocket.sendMessage(id, new Message(json));	
		
		System.out.println("cocxali va2r");
		//redirect to main jsp
		Timer timer1 = new Timer();
		timer1.schedule(new TimerTask() {
			@Override
			public void run() {
				JSONObject json = new JSONObject();
				json.put("command", "endgame");
				GameSocket.sendMessage(id, new Message(json));
				
				//remove room and game object
			}
		}, 3 * 1000);
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void removePlayerFromGame(Player cur) {
		points.remove(cur);
		round.removePlayerFromRound(cur);
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
		private String chosenWord = "";
		private Connection conn;
		private ArrayList<String> randomWords = new ArrayList<String>();
		private Timer endTurnTimer;
		private boolean turnIsEnded;
		private Date wordChosenTime;
		private int numberOfGuesses;

		public Round(Player starterArtist) {
			// System.out.println("startter 2 " + starterArtist);
			this.artist = starterArtist;
			// this.date = new Date(System.currentTimeMillis());
			this.turnCounter = 0;
			initMapRound();
			//roundStarterArtist = players.get(0);
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

		private void initMapRound() {
			playersGuessedTimesForSingleTurn = new HashMap<Player, Long>();
			TurnPoints = new HashMap<Player, Integer>();
			for (Player p : players) {
				playersGuessedTimesForSingleTurn.put(p, (long) secondsPerTurn);
				TurnPoints.put(p, 0);
			}
		}

		private void removePlayerFromRound(Player p) {
			playersGuessedTimesForSingleTurn.remove(p);
			TurnPoints.remove(p);
//			if (p.equals(roundStarterArtist)) {
//				int index = players.indexOf(p) - 1;
//				if (index == -1) {
//					roundStarterArtist = null;
//				} else {
//					roundStarterArtist = players.get(index);
//				}
//			} else if (p.equals(artist)) {
//				endTurn();
//
//				int index = players.indexOf(p) - 1;
//				if (index == -1) {
//					artist = null;
//				} else {
//					artist = players.get(index);
//				}
//			}
//			players.remove(p);
			if (p.equals(artist)) {
//				if (p.equals(roundStarterArtist)) {
//					int index = players.indexOf(p) - 1;
//					if (index == -1) {
//						roundStarterArtist = null;
//					} else {
//						roundStarterArtist = players.get(index);
//					}
//				}
				
				endTurnTimer.cancel();
				players.remove(p);
				//if (p.equals(roundStarterArtist)) roundStarterArtist = players.get(0);
				endTurn();
			} else {
				players.remove(p);
			}
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
					//System.out.println(index + " daaa" + players.indexOf(artist));
					if (index == players.size())
						index = 0;

					artist = players.get(index);
					if (artist.equals(roundStarterArtist)) {
						System.out.println("fooork");
						artist.shouldBeArtist(false);
						turnOffTimers();
						endRound();
						roundIsnotEnded = false;
					}
					roundStarterArtist = players.get(0);
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
			JSONObject json1 = new JSONObject();
			json1.put("command", "revealword");
			json1.put("word", chosenWord);
			GameSocket.sendMessage(artist.getGame().getId(), new Message(json1));
			
			turnIsEnded = true;
			chosenWord = "";
			JSONObject json = new JSONObject();
			json.put("command", "endturn");
			json.put("artist", artist.toString());
			GameSocket.sendMessage(artist.getGame().getId(), new Message(json));
			
			setChosenWordTime(null);
			artist.endDrawing();

			generatePointsForPlayers();
			sendPointsToWebSocket();

			numberOfGuesses = 0;
			startTurnTimer = new Timer();
			startTurnTimer.schedule(new java.util.TimerTask() {
				public void run() {
					startTurn();
				}
			}, 5 * 1000);
		}

		private void startTurn() {
			turnIsEnded = false;
			turnCounter++;
			choosePainter();
			artist.shouldBeArtist(true);
			sendNewTurnInformationsToSocket();
			if (roundIsnotEnded) {
				long diff = 10 * 1000;
					Date newDate1 = new Date(System.currentTimeMillis());
					generateThreeWordsAndChooseOne();
					Date newDate2 = wordChosenTime;
					
					if (newDate2 != null) {
						diff = newDate2.getTime() - newDate1.getTime();
					}
					
					Timer newTimer = new Timer();
					newTimer.schedule(new TimerTask() {
						@Override
						public void run() {
							//System.out.println(chosenWord);
							if(chosenWord.isEmpty()) sendWordsToArtist("autochooseword");
						}
					}, 10 * 1000);
				artist.startDrawing();

				JSONObject json = new JSONObject();
				json.put("command", "startturn");
				json.put("artist", artist.toString());
				GameSocket.sendMessage(artist.getGame().getId(), new Message(json));

				this.date = new Date(System.currentTimeMillis());
				
			initMapRound();	
			numberOfGuesses = 0;
				
			endTurnTimer = new Timer();
				endTurnTimer.schedule(new TimerTask() {
					@Override
					public void run() {
						endTurn();
					}
				}, secondsPerTurn * 1000 + diff);
			}
		}

		private void clearCanvas() {
			JSONObject json = new JSONObject();
			json.put("command", "clear");
			GameSocket.sendMessage(getId(), new Message(json));
		}

		private void generateThreeWordsAndChooseOne(){
			try {
				conn = ConnectionManager.getDBConnection();
				Statement st = conn.createStatement();
				String query = "SELECT * FROM words ORDER BY RAND() LIMIT 3;";
				ResultSet rs = st.executeQuery(query);
				randomWords = new ArrayList<String>();
				while (rs.next()) {
					randomWords.add(rs.getString("word"));
				}
				artist.shouldBeArtist(true);

				sendWordsToArtist("chooseWord");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		private void sendWordsToArtist(String passedCommand) {
			JSONObject json = new JSONObject();
			json.put("command", passedCommand);

			json.put("one", randomWords.get(0));
			json.put("two", randomWords.get(1));
			json.put("three", randomWords.get(2));

			GameSocket.sendMessage(getId(), new Message(json));
		}

		private void sendNewTurnInformationsToSocket() {
			JSONObject json = new JSONObject();
			json.put("command", "newturn");

			json.put("seconds", secondsPerTurn);
			json.put("artist", artist.toString()); // es gaawitlos an rame
			for (Player p : points.keySet()) {
				json.put(p.toString(), points.get(p));
			}
			GameSocket.sendMessage(getId(), new Message(json));
		}

		public void smbdGuessed(Player p, long sec) {
			playersGuessedTimesForSingleTurn.put(p, sec);

			numberOfGuesses++;
			if (numberOfGuesses == players.size() - 1) {
				//System.out.println("SHEMODIS");
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
				TurnPoints.put(key, res);
				key.addScore(res);
			}
			if (TurnPoints.containsKey(artist)) {
				TurnPoints.put(artist, (numberOfGuesses == 0) ? 0 : allPoints / numberOfGuesses);
			}
		}
		
		public boolean isTurnEnded() {
			return turnIsEnded;
		}


		public void setChosenWordTime(Date date) {
			wordChosenTime = date;
		}
	}
}