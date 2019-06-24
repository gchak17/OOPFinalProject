package message;

import org.json.*;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class MessageDecoder implements Decoder.Text<Message> {
    @Override
    public Message decode(String string) throws DecodeException {
        JSONObject json = new JSONObject(string);
        return new Message(json);
    }

    @Override
    public boolean willDecode(String string) {
        try {
        	new JSONObject(string).toString();
            return true;
        } catch (JSONException ex) {
            return false;
        }
    }

	@Override
	public void destroy() {}

	@Override
	public void init(EndpointConfig arg0) {}
}