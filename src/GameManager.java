import java.util.concurrent.ConcurrentHashMap;

public class GameManager {
	private ConcurrentHashMap<String, Game> games;
	
	public GameManager() {
		games = new ConcurrentHashMap<String, Game>();
	}
}