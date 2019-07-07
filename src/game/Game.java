package game;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;

import dao.Account;

public class Game {
	private ArrayList<Player> players;
	
	private HashMap<Player, Integer> points;

	private Player artist;
	private String id;
	private int maxPlayers;
	private int time;
	private int numRounds;
	private int painterIndex;
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
		points =  new HashMap<Player, Integer>();
		painterIndex = 0;
		nthRound = 0;
		startNewRound();
	}

	private void setGameForPlayers() {
		for(Player p : players) {
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
		while (true) {
			if (players.get(painterIndex).isInGame()) {
				artist = players.get(painterIndex);
				painterIndex++;
				break;
			}
			painterIndex++;
		}
	}

	public void startNewRound() {
		choosePainter();
		
		nthRound++;
		Round r = new Round(nthRound, players, artist);
		this.round = r;
	}

	public void endRound() {
		this.round.endRound();
		addRoundResToTotalScores();

		if (nthRound == numRounds) {
			endGame();
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

		showResults();

		ArrayList<Player> leftPlayers = playersWantToPlayAgain();
		if (leftPlayers.size() > 0) {
			// start game from 0
			this.players = leftPlayers;
			points = new HashMap<Player, Integer>();
			painterIndex = 0;
			nthRound = 0;
			choosePainter();
			startNewRound();
		}
	}

	private void showResults() {

	}

	private ArrayList<Player> playersWantToPlayAgain() {
		// ask if they want to play again
		return null;
	}

	public ArrayList<Player> getPlayers(){
		return players;
	}
}