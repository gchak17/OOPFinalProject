package game;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import managers.AccountData;

public class GameManager {
	private ConcurrentHashMap<String, Game> games;
	private ConcurrentHashMap<String, Room>  waitingRooms = new ConcurrentHashMap<String, Room>();
	private static final int CODE_LEN = 60;
	private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private AccountData databaseManager; 
	private static GameManager mainInstance = new GameManager(AccountData.getInstance());

	
	private GameManager(AccountData baseManager) {
		games = new ConcurrentHashMap<String, Game>();
		this.databaseManager = baseManager;
	}
	
	public void addGame(Game game) {
		games.put(game.getId(), game);
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
	
	public String registerRoom(Room room) {
        //id damtxvevis shansebi imdenad mcirea, rom ugulvebelvyoft;
        String id = generateRandomCode();
        waitingRooms.put(id,room);
        return id;
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
    
    public Room getRoomById(String id) {
    	return waitingRooms.get(id);
    }
    public ArrayList<String> getWaitingRooms(){
    	ArrayList<String> rooms =  new ArrayList<String>();
    	for (String key : waitingRooms.keySet()) { 
    		rooms.add(key); 
    	}
    	return rooms;
    }
}