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

import message.Message;
import message.MessageDecoder;
import message.MessageEncoder;

@ServerEndpoint(value = "/EnterChatServlet", configurator = GameSocketConfig.class)
public class ChatSocket {

	public static List<Session> sessionList = Collections.synchronizedList(new ArrayList<Session>());
	private static ReentrantLock chatlock = new ReentrantLock();

	@OnOpen
	public void onOpen(Session session, EndpointConfig config) {
		System.out.println("sth");
		HttpSession httpSession = ((HttpSession)session.getUserProperties().get("HttpSession"));
		System.out.println(httpSession);
		String username = (String) httpSession.getAttribute("username");
		session.getUserProperties().put("username", username);
		sessionList.add(session);
	}

	@OnClose
	public void onClose(Session session) {
		System.out.println("cs");
		sessionList.remove(session);
	}

	@OnMessage
	public void sendMessage(String message, Session session) throws IOException, EncodeException {
		System.out.println("came in here");
		if (message == "") return;
		String username = (String) session.getUserProperties().get("username");
		chatlock.lock();
		for (Session curSes : sessionList) {
            if (!curSes.equals(session)) {
            	JSONObject js = new JSONObject();
            	js.append("username", username);
            	js.append("message", message);
            	curSes.getBasicRemote().sendText(js.toString());
            }
        }
		chatlock.unlock();
	}
}
