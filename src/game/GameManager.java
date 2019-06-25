package game;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class GameManager {
	private ConcurrentHashMap<String, Game> games;
	private static final int CODE_LEN = 60;
	private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	public GameManager() {
		games = new ConcurrentHashMap<String, Game>();
	}
	
	public String addGame(ArrayList<Player> players) {
		String id = generateRandomCode();
		Game game = new Game(players, id);
		games.put(id, game);
		return id;
	}
	
	private String generateRandomCode() {
        StringBuilder bldr = new StringBuilder();
        int codeLen = CODE_LEN;
        while(--codeLen!=0){
            char charToAppend = CHARS.charAt((int) (Math.random()*CHARS.length()));
            bldr.append(charToAppend);
        }
        return bldr.toString();
    }
	
	public Game getGame(String id){
        return games.get(id);
    }
	
    public void endGame(String gameID){
        games.remove(gameID);
    }
}