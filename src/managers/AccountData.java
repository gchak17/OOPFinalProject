package managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.mysql.jdbc.Driver;

import dao.Account;
import dao.Avatar;
import db.MyDBInfo;

public class AccountData {
	
	private Connection conn;
	private static AccountData manager;
	private AvatarManager avatarManager;
	
	private AccountData() throws SQLException{
		try {
			conn = ConnectionManager.getDBConnection();
			avatarManager = AvatarManager.getInstance();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public static AccountData getInstance() //throws SQLException 
	{
		if(manager == null) {
			synchronized(AccountData.class) {
				if(manager == null) {
					try {
						manager = new AccountData();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return manager;
	}

	
	
	/*
	 * 
	 */
	public boolean addAccount(Account account) {
		//TODO check if the account is already registered
		/* 
		 * insert into database  
		 */
		int res = 0;
		try {
			Statement st  = conn.createStatement();
			res = st.executeUpdate("insert into accounts(id, user_name, authentication_string, avatar_id) values\n" + 
					"(" + account.getID() + ", '" + account.getUsername() + "', '" + PasswordHasher.passwordToHash(account.getPassword()) + "', " + account.getAvatar().getID() + ");");
						
			st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return res != 0;
	}
	
	
	/*
	 * 
	 */
	public boolean removeAccount(Account account) {
		int res = 0;
		try {
			Statement st = conn.createStatement();
			res = st.executeUpdate("delete from accounts where id = " + account.getID() + ";");
			st.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return res != 0;
	}


	/*
	 * 
	 */
	public void changeAccountAvatar(Account account, Avatar avatar) {
		try {
			Statement st = conn.createStatement();
			st.executeUpdate("update accounts set avatar_id = " + avatar.getID() + " where accounts.id = " + account.getID() + ";");
			st.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 
	 */
	public void addFriend(Account account, long friendId) {
		if(friendId <= 0 || account.getID() == friendId)return;
		try {
			FriendConnectionsIDGenerator generator = FriendConnectionsIDGenerator.getInstance();
			Statement st = conn.createStatement();
			st.executeUpdate("insert into friend_connections(id, user1_id, user2_id) values\n" + 
					"(" + generator.generateID() + ", " + account.getID() + ", " + friendId + ");");
			st.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 
	 */
	public ArrayList<Long> getFriendIDs(long id) {
		ArrayList<Long> res = new ArrayList<>();
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select f.user1_id from friend_connections f where f.user2_id = " + id + " union select fr.user2_id from friend_connections fr where fr.user1_id = " + id + ";");
			
			while(rs.next()) {
				long currId = rs.getInt(1);
				res.add(currId);
			}
			
			rs.close();
			st.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public Iterator<Account> getFriendAccounts(long id){
		ArrayList<Account> res = new ArrayList<>();
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select f.user1_id from friend_connections f where f.user2_id = " + id + " union select fr.user2_id from friend_connections fr where fr.user1_id = " + id + ";");
			
			while(rs.next()) {
				long currId = rs.getInt(1);
				res.add(getAccountByID(currId));
			}
			
			rs.close();
			st.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return res.iterator();
	}
	

	/*
	 * 
	 */
	public Account getAccountByID(long userID) {
		//TODO
		Account account = null;
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select a.id, a.user_name, a.authentication_string, a.avatar_id from accounts a where a.id = "  + userID + ";");
			while(rs.next()) {
				account = new Account(rs.getInt(1), rs.getString(2), rs.getString(3), avatarManager.getAvatarByID(rs.getInt(4)), getFriendIDs(rs.getInt(1)));
			}
			
			rs.close();
			st.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return account;
	}
	
	/*
	 * 
	 */
	public Account getAccountByUsername(String username) {
		//TODO
		Account account = null;
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select a.id, a.user_name, a.authentication_string, a.avatar_id from accounts a where a.user_name = '"  + username + "';");
			while(rs.next()) {
				account = new Account(rs.getInt(1), rs.getString(2), rs.getString(3), avatarManager.getAvatarByID(rs.getInt(4)), getFriendIDs(rs.getInt(1)));
			}
			
			rs.close();
			st.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return account;
	}
	
	/*
	 * 
	 */
	public Account authenticate(String username, String password) {
		//TODO
		Account account = null;
		String hashedPassword = PasswordHasher.passwordToHash(password);
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select a.id, a.user_name, a.authentication_string, a.avatar_id from accounts a " + 
												"where a.user_name = '" + username + "' and a.authentication_string = '" + hashedPassword + "';");
			
			while(rs.next()) {
				account = new Account(rs.getInt(1), rs.getString(2), rs.getString(3), avatarManager.getAvatarByID(rs.getInt(4)), getFriendIDs(rs.getInt(1)));
			}
			
			rs.close();
			st.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return account;
	}
	
	/*
	 * 
	 */
	public boolean nameInUse(String username) {
		boolean res = false;
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select a.id from accounts a where a.user_name = '" + username + "';");
			
			while(rs.next()) {
				res = true;
			}
			
			rs.close();
			st.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return res;
	}
}