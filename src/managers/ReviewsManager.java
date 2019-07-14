package managers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ReviewsManager {
	private static ReviewsManager manager;
	private Connection conn;
	
	private ReviewsManager(){
		try {
			conn = ConnectionManager.getDBConnection();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static ReviewsManager getInstance() {
		if(manager == null) {
			synchronized(ReviewsManager.class) {
				if(manager == null) {
					try {
						manager = new ReviewsManager();
					}catch(Exception e) {
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
	public void addReview(long reviewRecieverId, int review) {
		try {
			ReviewIDGenerator generator = ReviewIDGenerator.getInstance();
			Statement st = conn.createStatement();
			st.executeUpdate("insert into reviews(id, review_reciever_id, review_point) values\n" + 
					"(" + generator.generateID() + ", " + reviewRecieverId + ", " + review + ");");
			st.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 
	 */
	public double getAvgReviewPoint(long reviewRecieverId) {
		double avgPoint = -1; //if no reviews recieved yet
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select r.review_point from reviews r where r.review_reciever_id = " + reviewRecieverId + ";");
			
			if(!rs.next()) {
				rs.close();
				st.close();
				return avgPoint;
			}
			rs = st.executeQuery("select avg(r.review_point) from reviews r where r.review_reciever_id = " + reviewRecieverId + ";");
			
			if(rs.next()) {

				avgPoint = rs.getDouble(1);
			}
			
			rs.close();
			st.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return avgPoint;
	}
}
