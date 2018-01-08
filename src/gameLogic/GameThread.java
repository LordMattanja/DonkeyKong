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
	private long startTime = 0;
	
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
	
    public void startTimer() {
    	startTime = System.currentTimeMillis();
    }
    
    public int stopTimer() {
    	return (int)(System.currentTimeMillis() - startTime)/1000;
    }
    
    private int calcTimeBonus(int time) {
    	if(30-time > 0) {
    		return (30-time)*50;
    	} else {
    		return 0;
    	}
    }
    
	@Override
	public synchronized void run() {
		Random rand = new Random();
		startTimer();
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
				gameState.addToScore(calcTimeBonus(stopTimer()));
				gameState.endGame(false);
				paused = true;
			}
		
			if(player.getHealthProperty().intValue() == 0) {
				main.getContrLevel().gameOver(gameState.getScore());
				gameState.endGame(true);
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
			count = -1;
			startTimer();
			pauseLock.notifyAll();
		}
	}

}
