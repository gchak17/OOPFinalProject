package account;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Account {
	private String username;
	private String password;
	private String avatarLocation;
	
	public Account(String username, String password, String avatar) {
		this.username = username;
		this.password = password;
		avatarLocation = avatar;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	
	public String getAvatar() {
		return avatarLocation;
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
	
}