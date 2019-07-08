package managers;


import java.sql.*;
import java.util.HashMap;

import dao.Avatar;

public class AvatarManager {
	
	private static AvatarManager manager;
	private Connection conn;
	
	private AvatarManager(){
		try {
			conn = ConnectionManager.getDBConnection();
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
		Avatar avatar = null;
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select a.* from avatars a where a.id = " + avatarID + ";");
			
			while(rs.next()) {
				avatar = new Avatar(rs.getInt(1), rs.getString(2), rs.getString(3));
			}
			
			rs.close();
			st.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return avatar;
	}
	
	public boolean addAvatar(Avatar avatar) {
		int res = 0;
		try {
			Statement st1 = conn.createStatement();


			res = st1.executeUpdate("insert into avatars values (" + 
											avatar.getID() + ", " + 
											avatar.getFilename() + ", " + 
											avatar.getPath() +");");
			st1.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return res != 0;
	}
}
