package game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONObject;

import dao.Account;
import message.Message;
import message.MessageDecoder;
import message.MessageEncoder;


@ServerEndpoint(value = "/PublishRoom", encoders = {MessageEncoder.class}, decoders = {MessageDecoder.class}, configurator = GameSocketConfig.class)
public class PlayerEnteredSocket {
    public static List<Session> peers = Collections.synchronizedList(new ArrayList<Session>());
    private static boolean gameIsNotStarted = true;
    @OnOpen
    public void onOpen(Session peer) throws IOException, EncodeException {
    	System.out.println("oh henlo fren");
    	peers.add(peer);
    	HttpSession httpSession = (HttpSession) peer.getUserProperties().get("HttpSession");
    	ohHenloFrens(httpSession);
    }

    @OnClose
    public void onClose(Session peer) {
    	
    	if(gameIsNotStarted) {
	    	System.out.println("chaxura");
	    	
	        peers.remove(peer);
	        HttpSession httpSession = (HttpSession) peer.getUserProperties().get("HttpSession");
	        Player user = (Player) httpSession.getAttribute("player");
	        
	        if(user.equals(GameManager.getInstance().getRoomById((String)httpSession.getAttribute("gameId")).getAdmin())){
	        	System.out.println("waishalos otaxi");
	        }
	        
	        user.leftGame();
        
    	}
        
    }

    @OnMessage
    public void sendMessage(Message message, Session session) throws IOException, EncodeException {
    	JSONObject json = message.getJson();
    	
    	if(message.getJson().getString("type").equals("start") ) {    	
        	
        	HttpSession httpSession = (HttpSession) session.getUserProperties().get("HttpSession");
        	String id = (String) httpSession.getAttribute("gameId");
        	
    		Room r = GameManager.getInstance().getRoomById(id);

			
    		Player admin = r.getAdmin();
    		Player curPlayer  = (Player) (httpSession.getAttribute("player")) ;
    		if(curPlayer.equals(admin)) {
    			if(r.getPlayers().size() < 2) {
    				System.out.println("daelodos oponentens");
    				
    			}else {
    				Game g =  new Game(r.getPlayers(), r.getRounds(), r.getTime(), id);
    				GameManager.getInstance().addGame(g);
    				System.out.println("gaafolsa");
    				gameIsNotStarted = false;
    				
    				json.put("forward", true);
    				for (Session peer : peers) {
    		            peer.getBasicRemote().sendObject(message);
    		        }
    	 		}
    		}else {
    			session.getBasicRemote().sendObject(json);
    			//only admin can start
    		}
        	
          
	    	
        }
    	
    	
    
    }
    
    private void ohHenloFrens(HttpSession httpSession) throws IOException, EncodeException{
    	
    	String RoomId = (String) httpSession.getAttribute("gameId");
    	Room r = GameManager.getInstance().getRoomById(RoomId);
    	ArrayList<Player> players = r.getPlayers();
    	
    	JSONObject json = generateJsonForPlayers(players);
    	Message newM = new Message(json);
    	
    	for (Session peer : peers) {
    		peer.getBasicRemote().sendObject(newM);
        }
    }

	private JSONObject generateJsonForPlayers(ArrayList<Player> players) {
		JSONObject json = new JSONObject();
		String Players =  "";
		for(int i = 0; i < players.size(); i++) {
			Players += players.get(i).toString() + " ";
		}
		
		json.put("players", Players);
		json.put("type", "playersList");
		
		return json;
	}
    
}