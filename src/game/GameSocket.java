package game;

import java.util.Date;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONObject;

import message.*;

@ServerEndpoint(value = "/client/game", encoders = { MessageEncoder.class }, decoders = {
		MessageDecoder.class }, configurator = Configuration.class)
public class GameSocket {
	private static ConcurrentHashMap<String, List<Session>> sessions = new ConcurrentHashMap<>();

	@OnError
	public void onError(Throwable t) {
		System.out.println(t.getMessage());
	}
//	@OnError
//	public void onError(Throwable t) throws Throwable {
//	    // Most likely cause is a user closing their browser. Check to see if
//	    // the root cause is EOF and if it is ignore it.
//	    // Protect against infinite loops.
//	    int count = 0;
//	    Throwable root = t;
//	    while (root.getCause() != null && count < 20) {
//	        root = root.getCause();
//	        count ++;
//	    }
//	    if (root instanceof EOFException) {
//	        // Assume this is triggered by the user closing their browser and
//	        // ignore it.
//	    } else {
//	        throw t;
//	    }
//	}

	@OnOpen
	public void onOpen(Session curS, EndpointConfig config) {
		HttpSession httpSession = (HttpSession) curS.getUserProperties().get("HttpSession");
		httpSession.setAttribute("session", curS);
		String id = (String) httpSession.getAttribute("gameId");
		Room r = (Room) GameManager.getInstance().getRoomById(id);

		List<Session> ses;
		if (!sessions.containsKey(id)) {
			ses = Collections.synchronizedList(new ArrayList<Session>());
		} else {
			ses = sessions.get(id);
		}

		ses.add(curS);
		sessions.put(id, ses);

		if (ses.size() == r.getPlayers().size()) {
			// System.out.println(r.getRounds() + " " + r.getTime());
			Game g = new Game(r.getPlayers(), r.getRounds(), r.getTime(), id);
			GameManager.getInstance().addGame(g);
		}

	}

	@OnClose
	public void onClose(Session peer) throws IOException, EncodeException {

		HttpSession httpSession = (HttpSession) peer.getUserProperties().get("HttpSession");
		String id = (String) httpSession.getAttribute("gameId");
		List<Session> peers = sessions.get(id);
		peers.remove(peer);

		Game g = GameManager.getInstance().getGame(id);
		g.removePlayer((Player) httpSession.getAttribute("player"));

		JSONObject json = new JSONObject();
		json.put("command", "showplayers");
		for (Player p : g.getPoints().keySet()) {
			json.put(p.toString(), g.getPoints().get(p));
		}

		Message message = new Message(json);
		for (Session s : peers) {
			s.getBasicRemote().sendObject(message);
		}

	}

	@OnMessage
	public static void onMessage(Message message, Session session) throws IOException, EncodeException {
		HttpSession httpSession = (HttpSession) session.getUserProperties().get("HttpSession");

		String command = message.getJson().getString("command");
		JSONObject json = message.getJson();

		if (command.equals("checkStatus")) {

			json.put("answer", false);

			Player user = (Player) httpSession.getAttribute("player");

			if (user.isArtist()) {
				json.put("answer", true);
			}

			message.setJson(json);
			session.getBasicRemote().sendObject(message);

		} else if (command.equals("clear")) {
			String id = (String) httpSession.getAttribute("gameId");
			sendToEveryone(message, id);
		} else if (command.equals("wordIsChosen")) {
			String word = json.getString("chosen");
			String gameId = (String) httpSession.getAttribute("gameId");

			Game game = GameManager.getInstance().getGame(gameId);
			Game.Round round = game.getRound();
			round.setChosenWord(word);
			round.setChosenWordTime(new Date(System.currentTimeMillis()));
			
			JSONObject js = new JSONObject();
			js.put("command", command);
			js.put("chosen", hideTheWord(word));
			sendToEveryoneButMe(new Message(js), httpSession, session);
		} else {
			sendToEveryoneButMe(message, httpSession, session);
		}
	}

	private static String hideTheWord(String word) {
		String result = "";
		for (int i = 0; i < word.length(); i++) {
			result += "_";
			if (i != word.length() - 1)
				result += " ";
		}
		return result;
	}

	private static void sendToEveryoneButMe(Message message, HttpSession httpSession, Session session)
			throws IOException, EncodeException {
		String id = (String) httpSession.getAttribute("gameId");
		List<Session> peers = sessions.get(id);
		for (Session peer : peers) {
			if (!peer.equals(session)) {
				peer.getBasicRemote().sendObject(message);
			}
		}
	}

	private static void sendToEveryone(Message message, String id) {
		List<Session> peers = sessions.get(id);
		for (Session peer : peers) {
			try {
				peer.getBasicRemote().sendObject(message);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (EncodeException e) {
				e.printStackTrace();
			}
		}
	}

	public static void sendMessage(String gameId, Message message) {
		JSONObject json = message.getJson();
		String command = json.getString("command");

		if (command.equals("startturn") || command.equals("endturn")) {
			// System.out.println(command);
			sendOnlyToArtist(gameId, message);
		} else if (command.equals("chooseWord")) {
			try {
				sendDifferentCommands(gameId, message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (EncodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (command.equals("autochooseword")) {
			sendOnlyToArtist(gameId, message);
		} else {
			sendToEveryone(message, gameId);
		}
	}

	private static void sendDifferentCommands(String gameId, Message message) throws IOException, EncodeException {
		List<Session> peers = sessions.get(gameId);
		for (Session peer : peers) {
			if (((Player) ((HttpSession) peer.getUserProperties().get("HttpSession")).getAttribute("player"))
					.isArtist()) {
				JSONObject json = message.getJson();
				json.put("command", "chooseWord");
				peer.getBasicRemote().sendObject(message);
			} else {
				JSONObject json = message.getJson();
				json.put("command", "don't choose");
				peer.getBasicRemote().sendObject(message);
			}

		}
	}

	private static void sendOnlyToArtist(String gameId, Message message) {
		List<Session> peers = sessions.get(gameId);
		for (Session peer : peers) {
			if (((Player) ((HttpSession) peer.getUserProperties().get("HttpSession")).getAttribute("player"))
					.isArtist()) {
				try {
					peer.getBasicRemote().sendObject(message);
				} catch (IOException | EncodeException e) {
					e.printStackTrace();
				}
				return;
			}
		}
	}
}