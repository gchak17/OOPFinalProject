package nonDefaultPackage;

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

import db.MyDBInfo;

public class AccountData {
	
	private Connection conn;
	private static AccountData manager;
	
	
	private AccountData() throws SQLException{
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + MyDBInfo.MYSQL_DATABASE_NAME,
					MyDBInfo.MYSQL_USERNAME, MyDBInfo.MYSQL_PASSWORD);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public static AccountData getInstance() throws SQLException {
		if(manager == null) {
			synchronized(AccountData.class) {
				if(manager == null) {
					manager = new AccountData();
				}
			}
		}
		return manager;
	}

	
	
	/*
	 * 
	 */
	public int addAccount(String userName, String password, String avatar) {
		//check if the account is already registered
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select * from acc_info where binary user_name = '" + userName + "';");
			boolean isEmpty = true;
			while(rs.next()) {
				isEmpty = false;
			}
			if(!isEmpty)
				return -1;
		}catch(SQLException e) {
			e.printStackTrace();
		}
				
		/* 
		 * insert into database  
		 */
		String avatar_file = "./avatars/1.png";
		try {
			Statement st  = conn.createStatement();
			st.executeUpdate("insert into accounts(user_name, authentication_string, avatar_id) values\n" + 
					"('"+ userName + "', '" + password + "', (select id from avatars where binary avatar_filename = '"+ avatar + "'));");
			st.close();
			
			Statement st1 = conn.createStatement();
			ResultSet rs = st1.executeQuery("select * from acc_info where binary user_name = '" + userName + "' and binary authentication_string = '" + password + "';");
			
			while(rs.next()) {
				avatar_file = rs.getString(3);
			}
			
			rs.close();
			st1.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 1;
	}
	
	
	/*
	 * 
	 */
	public boolean removeAccount(String username) {
		try {
			Statement st = conn.createStatement();
			int res = st.executeUpdate("delete from accounts where user_name = '" + username + "';");
			
			st.close();
			System.out.println(username);
			return res != 0;
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		
	}
	
	
	/*
	 * 
	 */
	public Account getAccount(String userName, String password) {
		Account acc = null;
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select * from acc_info where binary user_name = '" 
										+ userName + "' and binary authentication_string ='" + password + "';");
			int count = 0;
			while(rs.next()) {
				count++;
				acc = new Account(rs.getString(1), rs.getString(2), rs.getString(3));
			}
			if(count > 1) {
				System.out.println("More than one row was returned");
				return null;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return acc;
	}
	
	
	public List<Account> getFriendsFor(String userName){
		List<Account> res = new ArrayList<Account>();
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select a.user_name, a.authentication_string, concat(ap.pathname, '/', av.avatar_filename) avatar\n" + 
					"	from accounts a \n" + 
					"	left join avatars av\n" + 
					"    on a.avatar_id = av.id\n" + 
					"    join avatar_paths ap\n" + 
					"    on av.relative_path_id = ap.id\n" + 
					"    where a.id in \n" + 
					"(select user2_id from friendships where user1_id = (select id from accounts where user_name = '" + userName + "'));");
					
			while(rs.next()) {
				res.add(new Account(rs.getString(1), rs.getString(2), rs.getString(3)));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	
	/*
	 * 
	 */
	public int numAccounts() {
		int count = 0;
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select id from accounts");
			
			while(rs.next()) {
				count++;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

}
