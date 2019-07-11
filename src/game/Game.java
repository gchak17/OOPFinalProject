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
	private int secondsPerTurn;
	private int numRounds;
	private Timer betweenRoundsTimer;
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

		
		//appear players on canvas
		JSONObject json = new JSONObject();
		json.put("command", "appearplayers");
		for (Player p : players)  json.put(p.toString(), 0);
		GameSocket.sendMessage(getId(), new Message(json));
		
		//javascript starts countdown from 5
//		JSONObject json1 = new JSONObject();
//		json1.put("command", "startgametimer");
//		try{ GameEndpoint.sendMessage(getId(), new Message(json1));
//		}catch (IOException | EncodeException e) { e.printStackTrace();}
		
		//game starts in 5 seconds
		Timer timer = new Timer();
		timer.schedule(
		new java.util.TimerTask() {
            public void run() {
            	startNewRound();
            }
        }, 5000);
	}

	private void initMap() {
		points = new HashMap<Player, Integer>();
		for(Player p : players) {
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
			artistIndex = players.indexOf(roundStarterArtist) + 1;
			if (artistIndex == players.size()) {
				artistIndex = 0;
			}
		}
		roundStarterArtist = players.get(artistIndex);
	}

	public void startNewRound() {
		System.out.println("axali raundi");
		roundCounter++;
		chooseStarterPainter();
		//this.round = null; // mgoni garbage collectors vumartivebt saqmes.. tu ara ? imena moshla xeliT
										// rogoraa ?
		Round r = new Round(roundStarterArtist);
		this.round = r;
	}

	public void endRound() {
		if (roundCounter == numRounds) {
			endGame();
		} else {

			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					startNewRound();
				}
			}, 10 * 1000);// aq tavidan minichebuli dro damchirdeba
			
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
		//aq mainc amoagdos rame didi fanjara
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
		private Map<Player, Integer> TurnPoints;
		private Date date;
		private Player roundStarter;
		private int turnCounter;
		private boolean roundIsnotEnded = true;
		private String chosenWord = "word";
		private Connection conn;
		private ArrayList<String> randomWords = new ArrayList<String>();
		private Timer endTurnTimer;
		
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
			playersGuessedTimesForSingleTurn = new HashMap<Player, Long>();
			TurnPoints = new HashMap<Player, Integer>();
			for (Player p : players) {
				playersGuessedTimesForSingleTurn.put(p, (long) 100);
				TurnPoints.put(p, 0);
			}
		}

		private void removePlayer(Player p) {
			playersGuessedTimesForSingleTurn.remove(p);
			TurnPoints.remove(p);
			if (p.equals(roundStarter)) {
				int ind = players.indexOf(p) + 1;
				if(ind == players.size())
					ind = 0;
				roundStarter = players.get(ind);
			}
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
			
			JSONObject json = new JSONObject();
			json.put("command", "removecanvaslisteners");
			json.put("artist", artist.toString());
			GameSocket.sendMessage(artist.getGame().getId(), new Message(json));
			
			generatePointsForPlayers();
			sendPointsToWebSocket();

			
			Timer timer = new Timer();
			timer.schedule(
			new java.util.TimerTask() {
	            public void run() {
	            	startTurn();
	            }
	        }, 5000);
			
		}

		private void startTurn() {
			turnCounter++;
			choosePainter();
			sendNewTurnInformationsToSocket();
			if (roundIsnotEnded) {
				//try {
					//generateThreeWordsAndChooseOne();
				//} catch (SQLException e) {
				//	e.printStackTrace();
				//}
				artist.startDrawing();
				
				JSONObject json = new JSONObject();
				json.put("command", "addcanvaslisteners");
				json.put("artist", artist.toString());
				GameSocket.sendMessage(artist.getGame().getId(), new Message(json));
				
				endTurnTimer = new Timer();
				endTurnTimer.schedule(new TimerTask() {
					@Override
					public void run() {
						endTurn();
					}
				}, 10 * 1000);// gadmocemuli dro
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
			while (rs.next()) {
				randomWords.add(rs.getString("word"));
			}
			System.out.println(randomWords);

		}

		private void sendNewTurnInformationsToSocket() {
			JSONObject json = new JSONObject();
			json.put("command", "newturn");
			//System.out.println(points.size());
			
			json.put("seconds", secondsPerTurn);
			json.put("artist", artist.toString()); // es gaawitlos an rame
			for (Player p : points.keySet()) {
				System.out.println(p.toString() + " : " + points.get(p));
				json.put(p.toString(), points.get(p));
			}
			GameSocket.sendMessage(getId(), new Message(json));
		}

		public void smbdGuessed(Player p, long sec) throws SQLException {
			playersGuessedTimesForSingleTurn.put(p, sec);

			if (playersGuessedTimesForSingleTurn.size() == players.size()-1) {
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
			for (Player key : playersGuessedTimesForSingleTurn.keySet()) {
				// int res = 100 - playersGuessedTimesForSingleTurn.get(key);
				int res = new Random().nextInt(50);
				TurnPoints.put(key, res);
				key.addScore(res);
			}
		}
	}
}