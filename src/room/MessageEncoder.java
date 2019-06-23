package room;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text<Message> {
    @Override
    public String encode(Message figure) throws EncodeException {
        return figure.getJson().toString();
    }

    @Override
    public void init(EndpointConfig ec) {}

    @Override
    public void destroy() {}
}