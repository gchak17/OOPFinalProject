package dao;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class AvatarTest {

	
	@Test
	void testGetters() {
		Avatar avatar = new Avatar(1, "someFilename", "somePath");
		assertEquals(1, avatar.getID());
		assertTrue("someFilename".equals(avatar.getFilename()));
		assertTrue("somePath".equals(avatar.getPath()));
		assertTrue("somePath/someFilename".equals(avatar.getFullPath()));
	}
	
	@Test
	void testSetters() {
		Avatar avatar = new Avatar(1, "someFilename", "somePath");
		avatar.setFilename("otherFilename");
		assertTrue("otherFilename".equals(avatar.getFilename()));
		avatar.setPath("otherPath");
		assertTrue("otherPath".equals(avatar.getPath()));
		assertTrue("otherPath/otherFilename".equals(avatar.getFullPath()));
	}
	
	@Test
	void testEquals() {
		Avatar avatar = new Avatar(1, "someFilename", "somePath");
		Avatar otherAv1 = new Avatar(1, "someFilename", "somePath");
		Avatar otherAv2 = new Avatar(2, "someFilename", "somePath");
		Avatar otherAv3 = new Avatar(1, "someFilename", "otherPath");
		Avatar otherAv4 = new Avatar(1, "otherFilename", "somePath");
		
		assertTrue(avatar.equals(otherAv1));
		assertTrue(!avatar.equals(otherAv2));
		assertTrue(!avatar.equals(otherAv3));
		assertTrue(!avatar.equals(otherAv4));
	}

}
