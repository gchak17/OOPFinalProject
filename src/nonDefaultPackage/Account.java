package nonDefaultPackage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Account {
	private String username;
	private String password;
	private String avatarLocation;
	private boolean online;
	private List<Account> friendList;
	
	public Account(String username, String password, String avatar) {
		this.username = username;
		this.password = password;
		online = true;
		friendList = new ArrayList<Account>();
		avatarLocation = avatar;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void changePassword(String newPassword) {
		password = newPassword;
	}
	
	public void changeAvatar(String newAvatar) {
		avatarLocation = newAvatar;
	}
	
	public String getAvatar() {
		return avatarLocation;
	}
	
	public void addFriend(Account friend) {
		friendList.add(friend);
	}
	
	public Iterator<Account> getFriendsList() {
		return friendList.iterator();
	}
}