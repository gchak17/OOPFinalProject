package game;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import org.json.*;
import dao.Account;
import message.*;

@ServerEndpoint(value = "/notifications", encoders = { MessageEncoder.class }, decoders = { MessageDecoder.class },
											configurator = Configuration.class)		
public class FriendNotificationEndpoint {

	private static Map<Long, Session> online = new HashMap<Long, Session>();
	
	@OnOpen
	public void onOpen(Session session) {
		HttpSession httpSession = (HttpSession) session.getUserProperties().get("HttpSession");
		Account user = (Account)httpSession.getAttribute("user");
		session.getUserProperties().put("user_id", user.getID());
		
		session.getUserProperties().put("user", user);
		online.put(user.getID(), session);
	}
	
	@OnClose
	public void onClose(Session session){
		Long user_id = (Long)session.getUserProperties().get("user_id");
		session.getUserProperties().remove("user_id");
		session.getUserProperties().remove("user");
		online.remove(user_id);
	}
	
	
	@OnMessage
	public void onMessage(Message message, Session session) throws IOException, EncodeException {
		JSONObject request = message.getJson();
		Long senderID = request.getLong("senderID");
		Long receiverID = request.getLong("receiverID");
		
		JSONObject notification = new JSONObject();
		
		Account user = (Account)(session.getUserProperties().get("user"));
		if((request.getString("notificationType")).equals("friendRequest")) {
			notification.put("message", user.getUsername() + " has sent you a friend request!");
		}else if((request.getString("notificationType")).equals("friendRemoval")) {
			notification.put("message", user.getUsername() + " has unfriended you!");
		}
		
		if(online.containsKey(receiverID)) {
			Session reciever = online.get(receiverID);
			Message notificationMessage = new Message(notification);
			reciever.getBasicRemote().sendObject(notificationMessage);
		}
		
	}
	
	@OnError
	    public void onError(Session session, Throwable t) {
	        t.printStackTrace();
	    }
	
	
	
		
}
