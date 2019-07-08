package game;

import java.util.Timer;
import java.util.TimerTask;

import dao.Account;

public class Player {
	private Account account;
	private long timeLeft;
	private boolean isArtist;
	private Game game;
	private long turnStartTimeMillis;
	private Timer timer;
	private int score;

	public Player(Account account) {
		this.account = account;
		isArtist = false;
		score = 0;
	}

	public void startDrawing() {
		assert (timeLeft > 0);
		isArtist = true;
		turnStartTimeMillis = System.currentTimeMillis();
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				game.getRound().endTurn();
			}
		}, 10 * 1000);// aq roundidan amogebuli dro damchirdeba
		System.out.println(account.getUsername() + " started turn, time remaining:" + getRemainingTimeSeconds());
	}

	public long getRemainingTimeSeconds() {
		if (isArtist)
			return timeLeft - (System.currentTimeMillis() - timeLeft) / 1000;
		return timeLeft;
	}

	public void endDrawing() {
		isArtist = false;
		timeLeft -= (System.currentTimeMillis() - turnStartTimeMillis) / 1000;
		System.out.println(account.getUsername() + " ended turn, time remaining:" + getRemainingTimeSeconds());
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Account getAccount() {
		return account;
	}

	public String toString() {
		return account.getUsername();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}

		if (!(other instanceof Player)) {
			return false;
		}
		return (this.account.equals(((Player) other).account));
	}

	public Game getGame() {
		return this.game;
	}

	public void setRoom() {
		// TODO Auto-generated method stub
	}

	public int getScore() {
		return this.score;
	}

	public boolean isArtist() {
		return isArtist;
	}

	public Timer getTimer() {
		return timer;
	}

}