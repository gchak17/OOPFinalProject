package game;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import javax.servlet.http.HttpSession;
import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;
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

@ServerEndpoint(value = "/client.html", encoders = { MessageEncoder.class }, decoders = {
		MessageDecoder.class }, configurator = Configuration.class)
public class ChatSocket {
	// public static List<Session> sessionList = Collections.synchronizedList(new
	// ArrayList<Session>());
	private static ReentrantLock chatlock = new ReentrantLock();
	private static ConcurrentHashMap<String, List<Session>> sessions = new ConcurrentHashMap<>();

	@OnOpen
	public void onOpen(Session session, EndpointConfig config) {
		HttpSession httpSession = ((HttpSession) session.getUserProperties().get("HttpSession"));
		String gameId = (String) httpSession.getAttribute("gameId");

		Player user = (Player) httpSession.getAttribute("player");
		Account acc = user.getAccount();
		String username = acc.getUsername();// (String) httpSession.getAttribute("username");
		session.getUserProperties().put("username", username);

		List<Session> sessionLst;
		if (sessions.containsKey(gameId)) {
			sessionLst = sessions.get(gameId);
			sessionLst.add(session);
		} else {
			sessionLst = Collections.synchronizedList(new ArrayList<Session>());
			sessionLst.add(session);
			sessions.put(gameId, sessionLst);
		}

	}

	@OnClose
	public void onClose(Session session) {
		HttpSession httpSession = ((HttpSession) session.getUserProperties().get("HttpSession"));
		String gameId = (String) httpSession.getAttribute("gameId");
		
		if (sessions.containsKey(gameId)) {
			List<Session> sessionLst = sessions.get(gameId);
			sessionLst.remove(session);
		}
		//sessionList.remove(session);

	}

	@OnMessage
	public void sendMessage(Message msg, Session session) throws IOException, EncodeException {
		Date playerDate = new Date(System.currentTimeMillis());
		JSONObject json = msg.getJson();
		if (!json.getString("command").equals("receiveMessage"))
			return;
		String message = json.getString("message");
		if (message.toString().isEmpty())
			return;

		HttpSession httpSession = ((HttpSession) session.getUserProperties().get("HttpSession"));
		String gameId = (String) httpSession.getAttribute("gameId");

		Game game = GameManager.getInstance().getGame(gameId);

		Player user = (Player) httpSession.getAttribute("player");
		Account acc = user.getAccount();
		String username = acc.getUsername();
		Game.Round round = game.getRound();

		if (message.toString().equals(round.getChosenWord())) {// aq unda iyos sityvis shemowmeba
			Date roundDate = round.getDate();

			long diff = playerDate.getTime() - roundDate.getTime();
			try {
				if (!user.isArtist())
					round.smbdGuessed(user, diff);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		chatlock.lock();
		List<Session> sessionList = sessions.get(gameId);
		for (Session curSes : sessionList) {

			JSONObject js = new JSONObject();
			js.append("username", username);
			if (message.toString().equals(round.getChosenWord())) {// aq unda iyos sityvis shemowmeba
				if (!user.isArtist())
					js.append("message", "guessed the word");
			} else {
				js.append("message", message);
			}
			curSes.getBasicRemote().sendObject(new Message(js));
		}
		chatlock.unlock();
	}
}