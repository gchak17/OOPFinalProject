package managers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class FriendRequestIDGenerator extends SimpleIDGenerator{

	private static FriendRequestIDGenerator generator;
	
	private FriendRequestIDGenerator() {
		try {
			Connection conn = ConnectionManager.getDBConnection();
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select max(r.id) from friend_requests r;");
			
			while(rs.next()) {
				nextID = rs.getLong(1);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static FriendRequestIDGenerator getInstance() {
		if(generator == null) {
			synchronized(FriendRequestIDGenerator.class) {
				if(generator == null) {
					try {
						generator = new FriendRequestIDGenerator();
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return generator;
	}
	
}
