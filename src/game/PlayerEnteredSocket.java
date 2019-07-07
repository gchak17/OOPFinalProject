package game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONObject;

import message.Message;
import message.MessageDecoder;
import message.MessageEncoder;

@ServerEndpoint(value = "/PublishRoom", encoders = { MessageEncoder.class }, decoders = {
		MessageDecoder.class }, configurator = GameSocketConfig.class)
public class PlayerEnteredSocket {

	private static ConcurrentHashMap<String, ArrayList<Session>> sessions = new ConcurrentHashMap<>();
	private static boolean gameIsNotStarted = true;

	@OnOpen
	public void onOpen(Session peer) throws IOException, EncodeException {
		System.out.println("oh henlo fren");

		HttpSession httpSession = (HttpSession) peer.getUserProperties().get("HttpSession");
		ohHenloFrens(httpSession, peer);
	}

	@OnClose
	public void onClose(Session peer) throws IOException, EncodeException {

		HttpSession httpSession = (HttpSession) peer.getUserProperties().get("HttpSession");
		String id = (String) httpSession.getAttribute("gameId");
		Room r = GameManager.getInstance().getRoomById(id);

		Player user = (Player) httpSession.getAttribute("player");

		if (gameIsNotStarted && user.equals(
				GameManager.getInstance().getRoomById((String) httpSession.getAttribute("gameId")).getAdmin())) {

			removeEveryone(peer, id);

			System.out.println("waishala roomi");
		}
		ArrayList<Session> sess = sessions.get(id);
		sess.remove(peer);
		sessions.put(id, sess);

		r.removePlayer(user);
	}

	private void removeEveryone(Session peer, String id) throws IOException, EncodeException {
		JSONObject json = new JSONObject();// es unda iyos show roomze rom gadartos egeti

		Message message = new Message(json);
		ArrayList<Session> peers = sessions.get(id);
		for (Session s : peers) {
			if (!s.equals(peer))
				s.getBasicRemote().sendObject(message);
		}
		sessions.remove(id);
		GameManager.getInstance().removeRoom(id);

	}

	@OnMessage
	public void sendMessage(Message message, Session session) throws IOException, EncodeException {
		JSONObject json = message.getJson();

		if (message.getJson().getString("type").equals("start")) {

			HttpSession httpSession = (HttpSession) session.getUserProperties().get("HttpSession");
			String id = (String) httpSession.getAttribute("gameId");

			Room r = GameManager.getInstance().getRoomById(id);

			Player admin = r.getAdmin();
			Player curPlayer = (Player) (httpSession.getAttribute("player"));
			if (curPlayer.equals(admin)) {
				json.put("admin", true);
				if (r.getPlayers().size() < 2) {
					session.getBasicRemote().sendObject(new Message(json));
				} else {
					Game g = new Game(r.getPlayers(), r.getRounds(), r.getTime(), id);
					GameManager.getInstance().addGame(g);
					gameIsNotStarted = false;

					json.put("forward", true);

					ArrayList<Session> peers = sessions.get(id);
					for (Session peer : peers) {
						peer.getBasicRemote().sendObject(message);
					}
				}
			} else {
				json.put("admin", false);
				session.getBasicRemote().sendObject(new Message(json));
				// only admin can start
			}

		}

	}

	private void ohHenloFrens(HttpSession httpSession, Session curS) throws IOException, EncodeException {

		String RoomId = (String) httpSession.getAttribute("gameId");
		Room r = GameManager.getInstance().getRoomById(RoomId);
		ArrayList<Player> players = r.getPlayers();

		ArrayList<Session> ses;
		if (sessions.get(RoomId) == null) {
			ses = new ArrayList<Session>();
		} else {
			ses = sessions.get(RoomId);
		}

		ses.add(curS);
		sessions.put(RoomId, ses);

		JSONObject json = generateJsonForPlayers(players);
		Message newM = new Message(json);

		for (Session s : ses) {
			s.getBasicRemote().sendObject(newM);
		}
	}

	private JSONObject generateJsonForPlayers(ArrayList<Player> players) {
		JSONObject json = new JSONObject();
		String Players = "";
		for (int i = 0; i < players.size(); i++) {
			Players += players.get(i).toString() + " ";
		}

		json.put("players", Players);
		json.put("type", "playersList");

		return json;
	}

}