package managers;

import java.sql.*;

public class IDGenerator {
	
	private static IDGenerator generator;
	private long nextID;
	
	private IDGenerator() {
		try {
			Connection conn = ConnectionManager.getDBConnection();
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select max(id) from accounts;");
			
			while(rs.next()) {
				nextID = rs.getLong(1);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static IDGenerator getInstance() {
		if(generator == null) {
			synchronized(IDGenerator.class) {
				if(generator == null) {
					try {
						generator = new IDGenerator();
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return generator;
	}
	
	
	public long generateID() {
		return ++nextID;
	}
}
