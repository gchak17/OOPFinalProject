package game;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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

@ServerEndpoint(value = "/client.jsp", configurator = GameSocketConfig.class)
public class ChatSocket {

	public static List<Session> sessionList = Collections.synchronizedList(new ArrayList<Session>());
	private static ReentrantLock chatlock = new ReentrantLock();

	@OnOpen
	public void onOpen(Session session, EndpointConfig config) {
		HttpSession httpSession = ((HttpSession) session.getUserProperties().get("HttpSession"));
		Player user = (Player) httpSession.getAttribute("player"); 
		Account acc = user.getAccount();
		String username = acc.getUsername();// (String) httpSession.getAttribute("username");
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
		Date playerDate = new Date(System.currentTimeMillis());
		
		HttpSession httpSession = ((HttpSession)session.getUserProperties().get("HttpSession"));
		String gameId = (String) httpSession.getAttribute("gameId");
		
		Game game = GameManager.getInstance().getGame(gameId);
		
        Player user = (Player) httpSession.getAttribute("player");
        Account acc = user.getAccount();
		String username = acc.getUsername();
		Game.Round round = game.getRound();
		
		if (message.equals(round.getChosenWord())) {//aq unda iyos sityvis shemowmeba
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
		for (Session curSes : sessionList) {

			JSONObject js = new JSONObject();
			js.append("username", username);
			if (message.equals(round.getChosenWord())) {//aq unda iyos sityvis shemowmeba
				if (!user.isArtist())
					js.append("message", "guessed the word");
			} else {
				js.append("message", message);
			}
			curSes.getBasicRemote().sendText(js.toString());
		}
		chatlock.unlock();
	}
}