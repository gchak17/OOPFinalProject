package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import dao.Account;
import dao.Avatar;
import game.Player;
import game.Room;

public class RoomTest {
	private static Avatar avatar;

	@BeforeAll
	static void init() {
		avatar = new Avatar(1, "", "");
	}

	@Test
	void test1() {
		Account acc = new Account((long)1, "user", "pass", avatar);
		Player admin = new Player(acc);
		Room room = new Room(admin, 2, 20, 2);
		
		Account acc1 = new Account((long)2, "user1", "pass1", avatar);
		Player pla = new Player(acc1);
		assertTrue(room.addPlayer(pla));
		room.setRoomId("id1");
		
		Account acc2 = new Account((long)3, "user12", "pass2", avatar);
		Player pla2 = new Player(acc2);
		assertFalse(room.addPlayer(pla2));
		assertFalse(room.addPlayer(pla));
		
		ArrayList<Player> arr = new ArrayList<Player>();
		arr.add(admin);
		arr.add(pla);
		
		assertEquals(arr, room.getPlayers());
		
		room.removePlayer(pla);
	}

	@Test
	void test2() {
		Account acc = new Account((long)1, "user", "pass", avatar);
		Player admin = new Player(acc);
		
		Room room = new Room(admin, 2, 20, 2);
		
		room.removePlayer(admin);
		
		room.addPlayer(admin);
		assertEquals(admin, room.getAdmin());
		assertEquals(2, room.getRounds());
		assertEquals(20, room.getTurnDuration());
		
		room.setRoomId("id1");
		assertEquals("id1", room.getRoomId());
		
		String toStringVers = "Created by: " + admin.toString() + ", " + 1 + " players, \n" + "Number of Rounds: "
				+ 2 + ", Turn Duration: " + 20;
		assertEquals(toStringVers, room.toString());
		assertFalse(room.isEmpty());
		assertTrue(room.isWaitingRoom());
		
		room.setStarted();
		assertFalse(room.isWaitingRoom());
		
		assertFalse(room.addPlayer(admin));
		room.removePlayer(admin);
		assertTrue(room.isEmpty());
	}


	// games nairi testebi aqac
}
