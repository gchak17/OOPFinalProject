package game;

import java.util.concurrent.ConcurrentHashMap;

public class GameManager {
	private ConcurrentHashMap<String, Game> games;
	
	public GameManager() {
		games = new ConcurrentHashMap<String, Game>();
	}
	
	public void addGame(Player player1, Player player2) {
		Game game = new Game(player1, player2, "randomid?");
		games.put("randomid?", game);
	}
	
	public Game getGame(String id){
        return games.get(id);
    }
	
    public void endGame(String gameID){
        games.remove(gameID);
    }
}