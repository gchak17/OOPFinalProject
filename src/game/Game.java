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

	private Player artist;
	private String id;
	private int maxPlayers;
	private int time;
	private int numRounds;
	private Timer betweenRoundsTimer;
	private boolean everybodyGuessed;

	private boolean isGameOver = false;
	private Round round;
	private int nthRound;

	public Game(ArrayList<Player> players, int round, int time, String id) {
		this.players = players;
		setGameForPlayers();
		this.numRounds = round;
		this.time = time;
		this.id = id;
		points = new HashMap<Player, Integer>();
		nthRound = 0;
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
		int index = 0;
		for(int i = 0; i < players.size(); i++) {
			if(players.get(i).equals(artist)) {
				index = i+1;
				if(index == players.size())
					index = 0;
			}
		}
		artist = players.get(index);
		
	}

	public void startNewRound() {
		choosePainter();
		System.out.println("painter is " + artist);
		nthRound++;
		Round r = new Round(nthRound, players, artist, id);
		this.round = r;
	}

	public void endRound() {
		this.round.endRound();
		addRoundResToTotalScores();

		if (nthRound == numRounds) {
			endGame();
		}
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				startNewRound();
			}
		}, 5 * 1000);// aq roundidan amogebuli dro damchirdeba
		
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
			// start game from 0
			this.players = leftPlayers;
			points = new HashMap<Player, Integer>();
			nthRound = 0;
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