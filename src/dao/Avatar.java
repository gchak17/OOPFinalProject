package dao;

public class Avatar {
	
	private long avatarID;
	private String path;
	private String filename;
	
	public Avatar(long avatarID, String filename, String path) {
		this.avatarID = avatarID;
		this.path = path;
		this.filename = filename;
	}
	
	public long getID() {
		return avatarID;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public String getPath() {
		return path;
	}
	
	public String getFullPath() {
		return path + "/" + filename;
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	@Override
	public boolean equals(Object other) {
		if(this == other) {
			return true;
		}
		
		if(!(other instanceof Avatar)) {
			return false;
		}
		
		Avatar otherAv = (Avatar)other;
		return this.avatarID == otherAv.avatarID && this.filename.equals(otherAv.filename)
							&& this.path.equals(otherAv.path);
	}
	
	
	
}
