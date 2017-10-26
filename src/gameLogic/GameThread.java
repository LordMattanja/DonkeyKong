package gameLogic;

import gui.LevelController;
import gui.MainApplication;
import objects.Player;

public class GameThread extends Thread{
	
	private Player player;
	private MainApplication main;
	private LevelController contrLvl;
	private GameState gameState;
	
    public void initGameThread(){
		main = MainApplication.getMain();
		player = main.getGamestate().getPlayer();
		contrLvl = main.getContrLevel();
		gameState = main.getGamestate();
		
    	
    }
	
	@Override
	public synchronized void run() {
		System.out.println("running");
		
		while (main.isGameActive()) {
			if(player.gethSpeed() != 0) {
				player.move();
			}
			contrLvl.repaint();
			
			
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}

}
