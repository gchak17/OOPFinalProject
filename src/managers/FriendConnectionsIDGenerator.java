package managers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class FriendConnectionsIDGenerator extends SimpleIDGenerator{
	
	private static FriendConnectionsIDGenerator generator;
	
	private FriendConnectionsIDGenerator() {
		try {
			Connection conn = ConnectionManager.getDBConnection();
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select max(f.id) from friend_connections f;");
			
			while(rs.next()) {
				nextID = rs.getLong(1);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static FriendConnectionsIDGenerator getInstance() {
		if(generator == null) {
			synchronized(FriendConnectionsIDGenerator.class) {
				if(generator == null) {
					try {
						generator = new FriendConnectionsIDGenerator();
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return generator;
	}
	
}
