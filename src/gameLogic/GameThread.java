package gameLogic;

import java.util.Random;

import gui.LevelController;
import gui.MainApplication;
import objects.Player;

public class GameThread extends Thread{
	
	private Player player;
	private MainApplication main;
	private LevelController contrLvl;
	private GameState gameState;
	private final Object pauseLock = new Object();
	private boolean paused = false;
	private int count = 0;
	
    public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public void initGameThread(){
		main = MainApplication.getMain();
		contrLvl = main.getContrLevel();
		gameState = main.getGamestate();   
		player = gameState.getPlayer();
    }
    
    public void updatePlayer() {
    	player = gameState.getPlayer();
    }
	
	@Override
	public synchronized void run() {
		Random rand = new Random();
		while (gameState.isGameActive()) {
			if(count % 100 == 0){
				gameState.addBarrel();
				count = rand.nextInt(20)+1;
			}
			if(gameState.isControlsEnabled() && (player.isPressedKeyLeft() || player.isPressedKeyRight() || (player.getvSpeed() != 0.0 && player.getvPos() < 800))) {
				player.move();
			}
			if(gameState.isControlsEnabled() && gameState.canClimb()){
				player.setCanClimb(true);
				if(player.isPressedKeyUp() || player.isPressedKeyDown()){
				  player.climb();
				}
			} else {
				player.setCanClimb(false);
				player.setClimbing(false);
			}
			
			gameState.checkForPlayerBarrelCollision();
			if(gameState.stageClear()) {
				gameState.endGame(false);
				paused = true;
			}
		
			if(player.getHealthProperty().intValue() == 0) {
				gameState.endGame(true);
				main.getContrLevel().gameOver();
				paused = true;
			}
			synchronized (pauseLock) {
				if (paused) {
					try {
						pauseLock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
			count++;
			
			try {
				sleep(33);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
			
		}
		System.out.println("Thread was stopped");
		
	}
	
	public void pauseThread() {
		paused = true;
	}
	
	public void resumeThread() {
		synchronized (pauseLock) {
			paused = false;
			count = 0;
			pauseLock.notifyAll();
		}
	}

}
