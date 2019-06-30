package managers;


import java.sql.*;
import java.util.HashMap;

import dao.Avatar;

public class AvatarManager {
	
	private static AvatarManager manager;
	private Connection conn;
	private HashMap<Long, Avatar> avatars;
	
	private AvatarManager() throws SQLException{
		try {
			conn = ConnectionManager.getDBConnection();
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select * from avatar_info;");
			
			//TODO populate avatars HashMap
			while(rs.next()) {
				
				long id = rs.getLong(1);// id
				String filename = rs.getString(2);// filename
				String path = rs.getString(3);// path
				avatars.put(id, new Avatar(id, filename, path));
			}
			 
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static AvatarManager getInstance() {
		if(manager == null) {
			synchronized(AvatarManager.class) {
				if(manager == null) {
					try {
						manager = new AvatarManager();
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return manager;
	}
	
	public Avatar getAvatarByID(long avatarID) {
		if(avatars.containsKey(avatarID)) {
			return avatars.get(avatarID);
		}
		return null;
	}
	
	public void addAvatar(Avatar avatar) {
		if(!avatars.containsKey(avatar.getID())) {
			avatars.put(avatar.getID(), avatar);
		}
	}
}
