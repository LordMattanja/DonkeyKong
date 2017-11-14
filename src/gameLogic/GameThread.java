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
			if(player.isPressedKeyLeft() || player.isPressedKeyRight() || (player.getvSpeed() != 0-.0 && player.getvPos() < 800)) {
				player.move();
			}
			if(gameState.canClimb()){
				player.setCanClimb(true);
				if(player.isPressedKeyUp() || player.isPressedKeyDown()){
				  player.climb();
				}
			} else {
				player.setCanClimb(false);
				player.setClimbing(false);
			}
			player.applyGravity();
							
			contrLvl.repaint();
			
			
			try {
				sleep(33);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}

}
