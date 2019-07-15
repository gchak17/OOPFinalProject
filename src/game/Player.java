package game;


import dao.Account;

public class Player {
	private Account account;
	private boolean isArtist;
	private Game game;
	private int score;
	private Room room;

	public Player(Account account) {
		this.account = account;
		isArtist = false;
		score = 0;
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

	public void setRoom(Room r) {
		this.room = r;
	}

	public Room getRoom() {
		return this.room;
	}

	public int getScore() {
		return this.score;
	}

	public boolean isArtist() {
		return isArtist;
	}

	public void addScore(int res) {
		score += res;
	}
	
	public void shouldBeArtist(boolean shouldBe) {
		isArtist = shouldBe;
	}
}