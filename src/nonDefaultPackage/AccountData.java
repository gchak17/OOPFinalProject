package nonDefaultPackage;

import java.util.HashMap;
import java.util.Iterator;

public class AccountData {
	private HashMap<String, Account> data;
	
	public AccountData() {
		data = new HashMap<>();
	}
	
	public AccountData(HashMap<String, Account> newData) {
		data = newData;
	}
	
	public int addAccount(String userName, String password, String avatar) {
		if(data.containsKey(userName))
			return -1;
		data.put(userName, new Account(userName, password, avatar));
		return 1;
	}
	
	public Account getAccount(String userName, String password) {
		Account account = data.get(userName);
		if(account != null && account.getPassword().equals(password))
			return account;
		return null;
	}
	
	public int numAccounts() {
		return data.size();
	}
}
