package dao;

public class Avatar {
	
	private String path;
	private String filename;
	
	public Avatar(String filename, String path) {
		this.path = path;
		this.filename = filename;
	}
	
	
	public String getFilename() {
		return filename;
	}
	
	public String getPath() {
		return path;
	}
	
	public String getFullPath() {
		return path + "\\" + filename;
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	
	
}
