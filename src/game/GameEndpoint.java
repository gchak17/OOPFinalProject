package game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONObject;

import message.*;

@ServerEndpoint(value = "/client.jsp/web", encoders = { MessageEncoder.class }, decoders = {
		MessageDecoder.class }, configurator = GameSocketConfig.class)
public class GameEndpoint {
	private static ConcurrentHashMap<String, ArrayList<Session>> sessions = new ConcurrentHashMap<>();

	@OnOpen
	public void onOpen(Session curS, EndpointConfig config) {
		HttpSession httpSession = (HttpSession) curS.getUserProperties().get("HttpSession");
		httpSession.setAttribute("session", curS);
		String id = (String) httpSession.getAttribute("gameId");

		ArrayList<Session> ses;
		if (sessions.get(id) == null) {
			ses = new ArrayList<Session>();
		} else {
			ses = sessions.get(id);
		}

		ses.add(curS);
		sessions.put(id, ses);
	}

	@OnClose
	public void onClose(Session peer) throws IOException, EncodeException {

		HttpSession httpSession = (HttpSession) peer.getUserProperties().get("HttpSession");
		String id = (String) httpSession.getAttribute("gameId");
		
		Game g = GameManager.getInstance().getGame(id);
		g.removePlayer((Player) httpSession.getAttribute("player"));
		
		JSONObject json = new JSONObject();// es unda iyos otaxshi vinebi darchnen imis shemcveli

		Message message = new Message(json);
		ArrayList<Session> peers = sessions.get(id);
		for (Session s : peers) {
			if (!s.equals(peer))
				s.getBasicRemote().sendObject(message);
		}
		peers.remove(peer);
	}

	@OnMessage
	public static void onMessage(Message message, Session session) throws IOException, EncodeException {
		HttpSession httpSession = (HttpSession) session.getUserProperties().get("HttpSession");

		String type = message.getJson().getString("command");
		JSONObject json = message.getJson();
		if (type.equals("checkStatus")) {

			json.put("answer", false);

			Player user = (Player) httpSession.getAttribute("player");

			if (user.isArtist()) {
				json.put("answer", true);
			}

			message.setJson(json);
			session.getBasicRemote().sendObject(message);

		} else { // aq typebze damokidebuli gaxdeba bevri ideaSi satitaod

			String id = (String) httpSession.getAttribute("gameId");
			ArrayList<Session> peers = sessions.get(id);
			for (Session peer : peers) {
				if (!peer.equals(session)) {
					peer.getBasicRemote().sendObject(message);
				}
			}
		}
	}

	public static void sendMessage(String gameId, Message message) throws IOException, EncodeException {
		
		ArrayList<Session> peers = sessions.get(gameId);
		for (Session peer : peers) {
			peer.getBasicRemote().sendObject(message);
		}
	}
}