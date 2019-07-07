package managers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class AvatarIDGenerator extends SimpleIDGenerator {
	
	private static AvatarIDGenerator generator;

	private AvatarIDGenerator() {
		try {
			Connection conn = ConnectionManager.getDBConnection();
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select max(a.id) from avatars a;");
			
			while(rs.next()) {
				nextID = rs.getLong(1);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static AvatarIDGenerator getInstance() {
		if(generator == null) {
			synchronized(AccountIDGenerator.class) {
				if(generator == null) {
					try {
						generator = new AvatarIDGenerator();
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return generator;
	}

}
