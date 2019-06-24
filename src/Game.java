import java.util.List;

public class Game {
	private List<Player> players;
	private Player artist, winner;
	private String id;
	private boolean isGameOver = false;
	
	public Game(List<Player> players, String id) {
		this.players = players;
		this.id = id;
	}
	
	
}