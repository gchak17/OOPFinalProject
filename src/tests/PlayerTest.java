package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import dao.Account;
import dao.Avatar;
import game.Game;
import game.Player;
import game.Room;

public class PlayerTest {
	@Test
	public void test1(){
		Player player1 = new Player(new Account(1, "user1", "pass1", new Avatar(1, "someFilename1", "somePath1")));
		Player player2 = new Player(new Account(2, "user2", "pass2", new Avatar(2, "someFilename2", "somePath2")));
		assertFalse(player1.equals(player2));
		assertTrue(player1.equals(player1));
		assertFalse(player1.equals(new Account(3, "user3", "pass3", new Avatar(3, "someFIlename3", "somePath3"))));
	}

	@Test
	public void test2() {
		Player player1 = new Player(new Account(1, "user1", "pass1", new Avatar(1, "someFilename1", "somePath1")));
		Player player2 = new Player(new Account(2, "user2", "pass2", new Avatar(2, "someFilename2", "somePath2")));
		ArrayList<Player> arrayList1 = new ArrayList<>();
		arrayList1.add(player1);
		arrayList1.add(player2);
		
		//Game game1 = new Game(arrayList1, 2, 20, "gameid1");
		//player1.setGame(new );
		//assertTrue(player1.getGame().equals(game1));
		player1.setGame(null);
		player2.setGame(null);
		
		assertEquals(player1.getGame(), player2.getGame());
		assertFalse(player1.getAccount().equals(player2.getAccount()));
	}
	
	@Test
	public void test3() {
		Player player1 = new Player(new Account(1, "user1", "pass1", new Avatar(1, "someFilename1", "somePath1")));
		Room room = new Room(player1, 2, 20, 3);
		player1.setRoom(room);
		assertTrue(player1.getRoom().equals(room));
	}
	
	@Test
	public void test4() {
		Player player1 = new Player(new Account(1, "user1", "pass1", new Avatar(1, "someFilename1", "somePath1")));
		player1.shouldBeArtist(true);
		assertTrue(player1.isArtist());
		player1.shouldBeArtist(false);
		assertFalse(player1.isArtist());
		
		player1.addScore(100);
		assertEquals(player1.getScore(), 100);
	}
	
	@Test
	public void test5() {
		Player player1 = new Player(new Account(1, "user1", "pass1", new Avatar(1, "someFilename1", "somePath1")));
		assertTrue(player1.toString().equals("user1"));
		assertFalse(player1.toString().equals("user2"));
	}
}