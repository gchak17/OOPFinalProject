package dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import managers.AccountData;
import managers.PasswordHasher;

public class Account {
	private long userID;
	private String username;
	private String password;
	private Avatar avatar;
	private ArrayList<Long> friendList;
	private AccountData accountData;
	
	
	public Account(long userID, String username, String password, Avatar avatar) {
		this.userID = userID;
		this.username = username;
		this.password = password;
		this.avatar = avatar;
		accountData = AccountData.getInstance();
	}
	
	public Account(long userID, String username, String password, Avatar avatar, ArrayList<Long> friends) {
		this.userID = userID;
		this.username = username;
		this.password = password;
		this.avatar = avatar;
		friendList = friends;
		accountData = AccountData.getInstance();
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
	
	public void addFriend(Long friendID) {
		friendList.add(friendID);
		accountData.addFriend(this, friendID);
	}
	
	public Account getFriendByID(long userID) {
		for(int i = 0; i < friendList.size(); i++) {
			if(friendList.get(i) == userID) {
				return accountData.getAccountByID(userID);
			}
		}
		return null;
	}
	
	public Account getFriendByUsername(String username) {
		for(int i = 0; i < friendList.size(); i++) {
			Account account = accountData.getAccountByID(friendList.get(i));
			if(account.getUsername().equals(username)) {
				return account;
			}
		}
		return null;
	}
	
	public Iterator<Account> getFriendList(){
		List<Account> res = new ArrayList<>();
		for(int i = 0; i < friendList.size(); i++) {
			res.add(accountData.getAccountByID(friendList.get(i)));
		}
		return res.iterator();
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