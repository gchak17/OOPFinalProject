package game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONObject;

import dao.Account;
import message.*;

@ServerEndpoint(value = "/client.html", encoders = {MessageEncoder.class}, decoders = {MessageDecoder.class}, configurator = GameSocketConfig.class)
public class GameEndpoint {
    public static List<Session> peers = Collections.synchronizedList(new ArrayList<Session>());
    
    @OnOpen
    public void onOpen(Session peer, EndpointConfig config) {

    	peers.add(peer);
    }

    @OnClose
    public void onClose(Session peer) {
        peers.remove(peer);
    }

    @OnMessage
    public void sendMessage(Message message, Session session) throws IOException, EncodeException {
    	if(message.getJson().getString("type").equals("isArtist?") ) {    	
        	JSONObject json = message.getJson();
        	json.put("answer", false);

            HttpSession httpSession = ((HttpSession)session.getUserProperties().get("HttpSession"));
            Account user = (Account) httpSession.getAttribute("user");
            
        	if(peers.get(0) == session) {
        		json.put("answer", true);
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