package dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Account {
	private long userID;
	private String username;
	private String password;
	private Avatar avatar;
	private List<Account> friendList;
	
	public Account(long userID, String username, String password, Avatar avatar) {
		this.userID = userID;
		this.username = username;
		this.password = password;
		this.avatar = avatar;
		friendList = new ArrayList<Account>();
	}
	
	public long getID() {
		return userID; 
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public Avatar getAvatar() {
		return avatar;
	}
	
	public void setUsername(String newUserName) {
		username = newUserName;
	}
	
	public void setPassword(String newPassword) {
		password = newPassword;
	}
	
	
	public void addFriend(Account friend) {
		friendList.add(friend);
	}
	
	public Account getFriendByID(long userID) {
		for(int i = 0; i < friendList.size(); i++){
			if(friendList.get(i).getID() == userID) {
				return friendList.get(i);
			}
		}
		return null;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		
		if(!(other instanceof Account)) {
			return false;
		}
		
		Account otherAcc = (Account) other;
		
		return this.username == otherAcc.username;
	}
	
	public String toString() {
		return "User: " + this.username + " Pass: " + this.password;
	}
}