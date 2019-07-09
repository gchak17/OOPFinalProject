package game;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import dao.Account;

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
		points = new HashMap<Player, Integer>();
		roundCounter = 0;
		artistIndex = 0;
		startNewRound();
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

	private void choosePainter() {
		if (artistIndex == 0) {
			roundStarterArtist = players.get(0);
		} else {
			for (int i = 0; i < players.size(); i++) {
				if (players.get(i).equals(roundStarterArtist)) {
					artistIndex = i + 1;
					if (artistIndex == numRounds)
						endGame();
				}
			}
			roundStarterArtist = players.get(artistIndex);

			System.out.println(roundStarterArtist + " raunds daiwyebs");
		}
	}

	public void startNewRound() {
		choosePainter();
		roundCounter++;
		this.betweenRoundsTimer = null; // mgoni garbage collectors vumartivebt saqmes.. tu ara ? imena moshla xeliT rogoraa ?
		Round r = new Round(players, roundStarterArtist, this);
		this.round = r;
	}

	public void endRound() {
		System.out.println("damTavrda raundi");
		addRoundResToTotalScores();

		System.out.println(roundCounter + " : " + numRounds);
		if (roundCounter == numRounds) {
			endGame();
		} else {

			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					startNewRound();
				}
			}, 5 * 1000);// aq roundidan amogebuli dro damchirdeba
		}
	}

	private void addRoundResToTotalScores() {
		HashMap<Player, Integer> roundPoints = round.getPoints();
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
			System.out.println("riatto");
			// start game from 0
			this.players = leftPlayers;
			points = new HashMap<Player, Integer>();
			roundCounter = 0;
			choosePainter();
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
	}

	public HashMap<Player, Integer> getPoints() {
		return points;
	}

}