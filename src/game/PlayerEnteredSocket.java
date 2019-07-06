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
    
    @OnOpen
    public void onOpen(Session peer) throws IOException, EncodeException {
    	System.out.println("oh henlo fren");
    	peers.add(peer);
    	HttpSession httpSession = (HttpSession) peer.getUserProperties().get("HttpSession");
    	ohHenloFrens(httpSession);
    }

    @OnClose
    public void onClose(Session peer) {
        peers.remove(peer);
        HttpSession httpSession = (HttpSession) peer.getUserProperties().get("HttpSession");
        Player user = (Player) httpSession.getAttribute("player");
        if(user.equals(GameManager.getInstance().getRoomById((String)httpSession.getAttribute("gameId")).getAdmin())){
        	System.out.println("waishalos otaxi");
        }
        
        user.leftGame();
        
    }

    @OnMessage
    public void sendMessage(Message message, Session session) throws IOException, EncodeException {
    
    }
    
    private void ohHenloFrens(HttpSession httpSession) throws IOException, EncodeException{
    	
    	String RoomId = (String) httpSession.getAttribute("gameId");
    	Room r = GameManager.getInstance().getRoomById(RoomId);
    	ArrayList<Player> players = r.getPlayers();
    	
    	JSONObject json = generateJsonForPlayers(players);
    	
    	
    	for (Session peer : peers) {
    		peer.getBasicRemote().sendObject(new Message(json));
        }
    }

	private JSONObject generateJsonForPlayers(ArrayList<Player> players) {
		JSONObject json = new JSONObject();
		json.put("players", "");
		
		return json;
	}
    
}