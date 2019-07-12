package game;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import javax.servlet.http.HttpSession;
import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.json.JSONObject;
import dao.Account;
import message.Message;
import message.MessageDecoder;
import message.MessageEncoder;

@ServerEndpoint(value = "/client/chat", encoders = { MessageEncoder.class }, decoders = {
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
		// sessionList.remove(session);

	}

	@OnMessage
	public void sendMessage(Message msg, Session session) throws IOException, EncodeException {
		Date playerDate = new Date(System.currentTimeMillis());
		JSONObject json = msg.getJson();
		if (!json.getString("command").equals("receiveMessage"))
			return;
		String message = json.getString("message");
		if (message.isEmpty())
			return;

		HttpSession httpSession = ((HttpSession) session.getUserProperties().get("HttpSession"));
		String gameId = (String) httpSession.getAttribute("gameId");

		Game game = GameManager.getInstance().getGame(gameId);

		Player user = (Player) httpSession.getAttribute("player");
		Account acc = user.getAccount();
		String username = acc.getUsername();
		Game.Round round = game.getRound();
		if (round != null && !round.isTurnEnded()) {
			Map<Player, Long> times = round.guessedTimes();
			if (times.get(user) == (long) game.secsPerTurn()) {
				if (message.toLowerCase().equals(round.getChosenWord().toLowerCase())) {
					Date roundDate = round.getDate();

					long diff = playerDate.getTime() - roundDate.getTime();
					try {
						if (!user.isArtist()) {
							round.smbdGuessed(user, diff);
							System.out.println(diff);
							message = "guessed the word";
						} else {
							prevetnOthersFromSeeing(session, "The artist shouldn't reveal the word", username);
							return;
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (stringsAreSimilar(message.toLowerCase(), round.getChosenWord().toLowerCase())) {
					if (!user.isArtist()) {
						message = "You are close to the answer";
					} else {
						prevetnOthersFromSeeing(session, "The artist shouldn't give hints in the chat", username);
						return;
					}
				}
			} else if (message.toLowerCase().equals(round.getChosenWord().toLowerCase())) {
				prevetnOthersFromSeeing(session, "already guessed the word", username);
				return;
			}
		}

		chatlock.lock();
		List<Session> sessionList = sessions.get(gameId);
		for (Session curSes : sessionList) {

			JSONObject js = new JSONObject();
			js.append("username", username);
			js.append("message", message);
			curSes.getBasicRemote().sendObject(new Message(js));
		}
		chatlock.unlock();
	}
	
	private void prevetnOthersFromSeeing(Session session, String message, String username) {
		JSONObject jas = new JSONObject();
		jas.append("username", username);
		jas.append("message", message);
		try {
			session.getBasicRemote().sendObject(new Message(jas));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EncodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * returns true if two strings don't mismatch by more than two characters;
	 */
	private boolean stringsAreSimilar(String message, String chosenWord) {
		int compLengths = message.length() - chosenWord.length();
		if (compLengths > 2 || compLengths < -2)
			return false;

		if (compLengths == 0) {
			return checkForSameSize(message, chosenWord);
		} else if (compLengths > 0) {
			return checkForSizeMismatch(message, chosenWord, compLengths);
		} else {
			return checkForSizeMismatch(chosenWord, message, compLengths * -1);
		}
	}

	/*
	 * de deep This method compares two strings that have different lengths
	 * (difference equals to or is less then two). It goes over the bigger word and
	 * cuts out the number of charcters that two string are mismatched by and then
	 * compares the said word to another, smaller word. If strings match after
	 * cutting out the characters, the method return true;
	 */
	private boolean checkForSizeMismatch(String biggerWord, String smallerWord, int num) {
		for (int i = 0; i < biggerWord.length(); i++) {
			String cutDownWord = biggerWord.substring(0, i);
			if (i < biggerWord.length() - 1)
				cutDownWord += biggerWord.substring(i + 1);

			if (num == 2) {
				for (int j = 0; j < cutDownWord.length(); j++) {
					String twoCuts = cutDownWord.substring(0, j);
					if (j < cutDownWord.length() - 1)
						twoCuts += cutDownWord.substring(j + 1);

					if (twoCuts.equals(smallerWord))
						return true;
				}

			} else if (cutDownWord.equals(smallerWord))
				return true;
		}
		return false;
	}

	private boolean checkForSameSize(String message, String chosenWord) {
		int mismatchCounter = 0;
		for (int i = 0; i < message.length(); i++) {
			if (mismatchCounter > 2)
				return false;

			if (message.charAt(i) != chosenWord.charAt(i))
				mismatchCounter++;
		}
		return true;
	}

	@OnError
	public void onError(Throwable t) {
		System.out.println(t.getMessage());
	}
}