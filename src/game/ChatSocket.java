package game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

@ServerEndpoint(value = "/chat.html", configurator = GameSocketConfig.class)
public class ChatSocket {

	public static List<Session> sessionList = Collections.synchronizedList(new ArrayList<Session>());
	private static ReentrantLock chatlock = new ReentrantLock();

	@OnOpen
	public void onOpen(Session session, EndpointConfig config) {
		HttpSession httpSession = ((HttpSession) session.getUserProperties().get("HttpSession"));
		String username = (String) httpSession.getAttribute("username");
		session.getUserProperties().put("username", username);
		sessionList.add(session);
	}

	@OnClose
	public void onClose(Session session) {
		sessionList.remove(session);
	}

	@OnMessage
	public void sendMessage(String message, Session session) throws IOException, EncodeException {
		if (message == "")
			return;
		
		HttpSession httpSession = ((HttpSession)session.getUserProperties().get("HttpSession"));
        Player user = (Player) httpSession.getAttribute("player");
        Account acc = user.getAccount();
		String username = acc.getUsername();//(String) session.getUserProperties().get("username");

		chatlock.lock();
		for (Session curSes : sessionList) {

			JSONObject js = new JSONObject();
			js.append("username", username);
			js.append("message", message);
			curSes.getBasicRemote().sendText(js.toString());
		}
		chatlock.unlock();
	}
}
