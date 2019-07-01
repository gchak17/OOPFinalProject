package game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONObject;

import message.*;

@ServerEndpoint(value = "/websocket", encoders = {MessageEncoder.class}, decoders = {MessageDecoder.class})
public class GameEndpoint {
    public static List<Session> peers = Collections.synchronizedList(new ArrayList<Session>());
    
    @OnOpen
    public void onOpen(Session peer) {
    	peers.add(peer);
    }

    @OnClose
    public void onClose(Session peer) {
        peers.remove(peer);
    }

    @OnMessage
    public void sendMessage(Message message, Session session) throws IOException, EncodeException {
        

    	System.out.println("cdilobs gaigos ");
    	if(message.getJson().getString("type").equals("isArtist?") ) {    	
        	JSONObject json = message.getJson();
        	json.put("answer", false);
        	if(peers.get(0) == session) {
        		json.put("answer", true);
        		System.out.println("shevida");
        	}
        	
        	message.setJson(json);
        	session.getBasicRemote().sendObject(message);
        }else {
	    	for (Session peer : peers) {
	            if (!peer.equals(session)) {
	                peer.getBasicRemote().sendObject(message);
	            }
	        }
        }
    }
}