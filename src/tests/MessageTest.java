package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;


import message.Message;

public class MessageTest {
	//json chavwere shevinaxe, sworad sheadara
	@Test
	void testGetters() {
		JSONObject json = new JSONObject();
		json.put("message", "hello");
		json.put("name", "someone");
		
		JSONObject json1 = new JSONObject();
		json.put("message", "hello");
		json.put("name", "someoneelse");
		
		Message msg = new Message(json);
		
		assertEquals(json, msg.getJson());
		assertFalse(json1.equals(msg.getJson()));
	}
	
	@Test
	void testSetters() {
		JSONObject json = new JSONObject();
		json.put("message", "hello");
		json.put("name", "someone");
		Message msg = new Message(json);
		
		JSONObject json1 = new JSONObject();
		json.put("message", "hello");
		json.put("name", "someoneelse");
		msg.setJson(json1);
		assertEquals(json1, msg.getJson());
		assertFalse(json.equals(msg.getJson()));
	}
	
	
	@Test
	void testToString() {
		JSONObject json = new JSONObject();
		json.put("message", "hello");
		json.put("name", "someone");
		Message msg = new Message(json);
		
		
		assertEquals(json.toString(), msg.toString());
		
		JSONObject json1 = new JSONObject();
		json.put("message", "hello");
		json.put("name", "someoneelse");
		assertFalse(json1.toString().equals(msg.toString()));
	}
	

}
