package room;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/websocket", encoders = {MessageEncoder.class}, decoders = {MessageDecoder.class})
public class Endpoint {
    private static List<Session> peers = Collections.synchronizedList(new ArrayList<Session>());
    
    @OnOpen
    public void onOpen(Session peer) {
    	peers.add(peer);
    }

    @OnClose
    public void onClose(Session peer) {
        peers.remove(peer);
    }

    @OnMessage
    public void sendMessage(Message message, Session session) throws IOException, EncodeException {
        for (Session peer : peers) {
            if (!peer.equals(session)) {
                peer.getBasicRemote().sendObject(message);
            }
        }
    }
}