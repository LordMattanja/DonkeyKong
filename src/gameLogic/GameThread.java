package gameLogic;

import gui.MainApplication;
import objects.Player;

public class GameThread extends Thread{
	
	private Player player;
	private MainApplication main;
	
	@Override
	public void run() {
		main = MainApplication.getMain();
		player = main.getGamestate().getPlayer();
		
		while (main.isGameActive()) {
			if(player.gethSpeed() != 0) {
				player.move();
			}
			
			try {
				sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}

}
