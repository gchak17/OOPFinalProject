package dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import managers.AccountData;
import managers.PasswordHasher;

public class Account {
	private Long userID;
	private String username;
	private String password;
	private Avatar avatar;
	private ArrayList<Long> friendList;
	
	
	public Account(long userID, String username, String password, Avatar avatar) {
		this.userID = userID;
		this.username = username;
		this.password = password;
		this.avatar = avatar;
		friendList = new ArrayList<Long>();
	}
	
	public Account(long userID, String username, String password, Avatar avatar, ArrayList<Long> friends) {
		this.userID = userID;
		this.username = username;
		this.password = password;
		this.avatar = avatar;
		friendList = friends;
	}
	
	
	
	public Long getID() {
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
	
	public void addFriend(Long friendID) {
		if(friendID > 0 && friendID != this.getID()) {
			friendList.add(friendID);
		}
	}
	
	public void removeFriend(Long friendID) {
		friendList.remove(friendID);
	}
	
	
	public boolean isFriendsWith(Long friendID) {
		return  friendList.contains(friendID);
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
		
		return this.username.equals(otherAcc.username) && this.userID == otherAcc.userID && 
							this.password.equals(otherAcc.password) && this.avatar.equals(otherAcc.avatar);
	}
	
	public String toString() {
		return "User: " + this.username + " Pass: " + this.password;
	}
}