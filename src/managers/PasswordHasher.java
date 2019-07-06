package managers;

import java.security.MessageDigest;

public class PasswordHasher{
	public static String passwordToHash(String password){
		MessageDigest messageDigest = null;
		try{
			messageDigest = MessageDigest.getInstance("SHA");
		}catch(Exception e){}
		messageDigest.update(password.getBytes());
		return hexToString(messageDigest.digest());
	}
	
	public static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  
			if (val<16) buff.append('0');
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}
}