package game;

import java.util.Timer;

import account.Account;

public class Player {
	private Account account;
	private long timeLeft;
	private boolean isArtist;
	private Game game;
	
	public Player(Account account) {
		this.account = account;
		isArtist = false;
		timeLeft = 0;
	}
	
	public void startDrawing() {
		
	}
	
	public void endDrawing() {
		
	}
	
	public void setGame(Game game) {
		this.game = game;
	}
	
	public Account getAccount(){
        return account;
    }
}