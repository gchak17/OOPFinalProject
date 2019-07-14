package dao;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class AccountTest {

	private static Avatar avatar;
	
	@BeforeAll
	static void init(){
		avatar = new Avatar(1, "", "");
	}
	
	
	@Test
	void testGetters() {
		Account account = new Account(1, "username", "password", avatar);
		assertEquals(1, account.getID());
		assertTrue("username".equals(account.getUsername()));
		assertTrue("password".equals(account.getPassword()));
		assertTrue(avatar.equals(account.getAvatar()));
	}
	
	@Test
	void TestSetter() {
		Account account = new Account(1, "username", "password", avatar);
		account.setUsername("newUsername");
		assertTrue("newUsername".equals(account.getUsername()));
	}
	
	@Test
	void TestEquals() {
		Avatar otherAv1 = new Avatar(1, "someFilename", "somePath");
		Avatar otherAv2 = new Avatar(2, "someFilename", "somePath");
		
		Account account = new Account(1, "username", "password", avatar);
		Account otherAcc1 = new Account(1, "username", "password", avatar);
		Account otherAcc2 = new Account(1, "username", "password", otherAv1);
		Account otherAcc3 = new Account(1, "username", "password", otherAv2);
		Account otherAcc4 = new Account(1, "otherUsername", "password", avatar);
		Account otherAcc5 = new Account(1, "username", "otherPassword", avatar);
		Account otherAcc6 = new Account(2, "username", "password", avatar);
		
		assertTrue(account.equals(otherAcc1));
		assertTrue(!account.equals(otherAcc2));
		assertTrue(!account.equals(otherAcc3));
		assertTrue(!account.equals(otherAcc4));
		assertTrue(!account.equals(otherAcc5));
		assertTrue(!account.equals(otherAcc6));
		
	}
	
	
	@Test
	void testAddFriend() {
		Account account = new Account(1, "username", "password", avatar);
		account.addFriend(Long.valueOf((long)1));
		account.addFriend(Long.valueOf((long)2));
		account.addFriend(Long.valueOf((long)10));
		
		assertTrue(!account.isFriendsWith(Long.valueOf((long)1)));
		assertTrue(account.isFriendsWith(Long.valueOf((long)2)));
		assertTrue(account.isFriendsWith(Long.valueOf((long)10)));
	}
	
	@Test 
	void testRemoveFriend(){
		Account account = new Account(1, "username", "password", avatar);
		account.addFriend(Long.valueOf((long)1));
		account.addFriend(Long.valueOf((long)2));
		account.addFriend(Long.valueOf((long)10));
		
		account.removeFriend(Long.valueOf((long)10));
		assertTrue(!account.isFriendsWith(Long.valueOf((long)10)));
		
		account.removeFriend(Long.valueOf((long)1));
		assertTrue(!account.isFriendsWith(Long.valueOf((long)1)));
	}

}
