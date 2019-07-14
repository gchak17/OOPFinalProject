package managers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ReviewIDGenerator extends SimpleIDGenerator{
	private static ReviewIDGenerator generator;
	
	private ReviewIDGenerator() {
		try {
			Connection conn = ConnectionManager.getDBConnection();
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select max(r.id) from reviews r;");
			
			while(rs.next()) {
				nextID = rs.getLong(1);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static ReviewIDGenerator getInstance() {
		if(generator == null) {
			synchronized(ReviewIDGenerator.class) {
				if(generator == null) {
					try {
						generator = new ReviewIDGenerator();
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return generator;
	}
}
