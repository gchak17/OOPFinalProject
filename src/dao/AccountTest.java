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
		String accountToString = "User: " + "username" + " Pass: " + "password";
		assertTrue(accountToString.equals(account.toString()));
		
		Account acc = new Account(2, "myname", "myname", avatar, null);
		assertTrue("myname".equals(acc.getUsername()));
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
		
		assertTrue(account.equals(account));
		assertFalse(account.equals("bla"));
		assertTrue(account.equals(otherAcc1));
		
		assertFalse(account.equals(otherAcc2));
		assertFalse(account.equals(otherAcc3));
		assertFalse(account.equals(otherAcc4));
		assertFalse(account.equals(otherAcc5));
		assertFalse(account.equals(otherAcc6));
		
	}
	
	
	@Test
	void testAddFriend() {
		Account account = new Account(1, "username", "password", avatar);
		account.addFriend(Long.valueOf((long)1));
		account.addFriend(Long.valueOf((long)2));
		account.addFriend(Long.valueOf((long)10));
		
		assertFalse(account.isFriendsWith(Long.valueOf((long)1)));
		assertTrue(account.isFriendsWith(Long.valueOf((long)2)));
		assertTrue(account.isFriendsWith(Long.valueOf((long)10)));
		
		account.addFriend(Long.valueOf((long)0));
		assertFalse(account.isFriendsWith(Long.valueOf((long) 0 )));
	}
	
	@Test 
	void testRemoveFriend(){
		Account account = new Account(1, "username", "password", avatar);
		account.addFriend(Long.valueOf((long)1));
		account.addFriend(Long.valueOf((long)2));
		account.addFriend(Long.valueOf((long)10));
		
		account.removeFriend(Long.valueOf((long)10));
		assertFalse(account.isFriendsWith(Long.valueOf((long)10)));
		
		account.removeFriend(Long.valueOf((long)1));
		assertFalse(account.isFriendsWith(Long.valueOf((long)1)));
	}

}
