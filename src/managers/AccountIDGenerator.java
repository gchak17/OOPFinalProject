package managers;

import java.sql.*;

public class AccountIDGenerator extends SimpleIDGenerator{
	
	private static AccountIDGenerator generator;
	
	private AccountIDGenerator() {
		try {
			Connection conn = ConnectionManager.getDBConnection();
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select max(a.id) from accounts a;");
			
			while(rs.next()) {
				nextID = rs.getLong(1);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static AccountIDGenerator getInstance() {
		if(generator == null) {
			synchronized(AccountIDGenerator.class) {
				if(generator == null) {
					try {
						generator = new AccountIDGenerator();
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return generator;
	}
	
}
