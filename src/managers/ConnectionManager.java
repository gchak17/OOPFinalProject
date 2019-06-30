package managers;

import java.sql.*;

import db.MyDBInfo;

public class ConnectionManager {
	
	private static Connection conn;
	
	private static void initializeConnection() throws ClassNotFoundException, SQLException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + MyDBInfo.MYSQL_DATABASE_NAME,
					MyDBInfo.MYSQL_USERNAME, MyDBInfo.MYSQL_PASSWORD);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getDBConnection() {
		if(conn == null) {
			synchronized(conn) {
				if(conn == null) {
					try {
						initializeConnection();
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return conn;
	}
	
}
