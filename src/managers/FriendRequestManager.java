package managers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

public class FriendRequestManager {
	private static FriendRequestManager manager;
	private Connection conn;
	
	private FriendRequestManager(){
		try {
			conn = ConnectionManager.getDBConnection();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static FriendRequestManager getInstance() {
		if(manager == null) {
			synchronized(FriendRequestManager.class) {
				if(manager == null) {
					try {
						manager = new FriendRequestManager();
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
	public void sendFriendRequest(long requestSenderId, long reciverUsernameId) {
		try {
			FriendRequestIDGenerator generator = FriendRequestIDGenerator.getInstance();
			Statement st = conn.createStatement();
			st.executeUpdate("insert into friend_requests(id, request_sender_id, request_reciever_id) values\n" + 
					"(" + generator.generateID() + ", " + requestSenderId + ", " + reciverUsernameId + ");");
			st.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 
	 */
	public Iterator<Long> getRequests(long accountId) {
		ArrayList<Long> requests = new ArrayList<>();
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select r.request_sender_id from friend_requests r where r.request_reciever_id = " + accountId + ";");
			
			while(rs.next()) {
				long currId = rs.getInt(1);
				requests.add(currId);
			}
			
			rs.close();
			st.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return requests.iterator();
	}
}
