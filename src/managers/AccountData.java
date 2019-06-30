package managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;
import java.util.ArrayList;
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
	private HashMap<Long, Account> accounts;
	
	
	private AccountData() throws SQLException{
		accounts = new HashMap<Long, Account>();
		try {
			conn = ConnectionManager.getDBConnection();
			avatarManager = AvatarManager.getInstance();
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select * from accounts;");
			while(rs.next()) {
				long userID = rs.getLong(1);// id
				String username = rs.getString(2);// username
				String password = rs.getString(3);// password
				long avatarID = rs.getLong(5);// avatarID
				
				accounts.put(userID, new Account(userID, username, password, avatarManager.getAvatarByID(avatarID)));
			}
			rs.close();
			st.close();
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
	public int addAccount(Account account) {
		//TODO check if the account is already registered
		/* 
		 * insert into database  
		 */
		try {
			Statement st  = conn.createStatement();
			st.executeUpdate("insert into accounts(id, user_name, authentication_string, avatar_id) values\n" + 
					"(" + account.getID() + ", '" + account.getUsername() + "', '" + account.getPassword() + "', " + account.getAvatar().getID() + ");");
						
			st.close();
			accounts.put(account.getID(), account);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 1;
	}
	
	
	/*
	 * 
	 */
	public boolean removeAccount(Account account) {
		try {
			Statement st = conn.createStatement();
			int res = st.executeUpdate("delete from accounts where id = '" + account.getID() + "';");
			accounts.remove(account.getID());
			st.close();
			System.out.println(account.getUsername());
			return res != 0;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	/*
	 * 
	 */
	public Account getAccountByID(long userID) {
		//TODO
		if(accounts.containsKey(userID)) {
			return accounts.get(userID);
		}
		return null;
	}
	
	/*
	 * 
	 */
	public Account authenticate(String username, String password) {
		//TODO
		for(Account a : accounts.values()) {
			if(a.getUsername().equals(username) && a.getPassword().equals(password)) {
				return a;
			}
		}
		return null;
	}
	
	/*
	 * 
	 */
	public boolean nameInUse(String username) {
		for(Account a : accounts.values()) {
			if(a.getUsername().equals(username)) {
				return true;
			}
		}
		return true;
	}
	
	/*
	 * 
	 */
	public int numAccounts() {
		return accounts.size();
	}

}
