package game;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import managers.AccountData;

public class GameManager {
	private ConcurrentHashMap<String, Game> games;
	private ArrayList<Room> waitingRooms = new ArrayList<Room>();
	private static final int CODE_LEN = 60;
	private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private AccountData databaseManager; 
	private static GameManager mainInstance = new GameManager(AccountData.getInstance());

	
	private GameManager(AccountData baseManager) {
		games = new ConcurrentHashMap<String, Game>();
		this.databaseManager = baseManager;
	}
	
	public String addGame(Game game) {
		String id = generateRandomCode();
		game.setId(id);
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
    
    public static GameManager getInstance() {
    	return mainInstance;
    }
    
    public ArrayList<Room> getWaitingRooms() {
    	return waitingRooms;
    }
}