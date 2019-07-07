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

@ServerEndpoint(value = "/client.html", encoders = { MessageEncoder.class }, decoders = {
		MessageDecoder.class }, configurator = GameSocketConfig.class)
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
	public static void sendMessage(Message message, Session session) throws IOException, EncodeException {
		String type = message.getJson().getString("type");
		JSONObject json = message.getJson();
		if (type.equals("isArtist?")) {
			
			json.put("answer", false);

			HttpSession httpSession = ((HttpSession) session.getUserProperties().get("HttpSession"));
			Player user = (Player) httpSession.getAttribute("player");
			// System.out.println(user);

			if (user.isArtist()) {
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