package game;

import java.util.Random;
import gui.MainApplication;
import objects.Player;

public class GameThread extends Thread{
	
	private Player player;
	private MainApplication main;
	private GameState gameState;
	//Objekt auf das der Thread warten kann wenn er pausiert wird
	private final Object pauseLock = new Object();
	//boolean-flag, ob der Thread gerade pausiert ist
	private boolean paused = false;
	//int-Attribut das zum Zählen und Fässer-hinzufügen verwendet wird
	private int count = 0;
	//Variable für die Startzeit des Timers der für die Berechnung der Bonuspunkte verwendet wird 
	private long startTime = 0;
	
    public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public GameThread(){
		main = MainApplication.getMain();
		gameState = main.getGamestate();   
		player = gameState.getPlayer();
    }
	
    public void startTimer() {
    	startTime = System.currentTimeMillis();
    }
    
    public int stopTimer() {
    	return (int)(System.currentTimeMillis() - startTime)/1000;
    }
    
    /*
     * berechnet den Zeitbonus den der Spieler für das Level erhält
     */
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
			//Fügt unregelmäßig neue Fässer zum Spiel hinzu
			if(count % (135- gameState.getLevel()*15) == 0){
				gameState.addBarrel();
				count = rand.nextInt(20)+1;
			}
			//überprüft Vorraussetzungen zur Bewegung des Spielers und initiiert diese
			if(!player.isClimbing() && gameState.isControlsEnabled() && (player.isPressedKeyLeft() || player.isPressedKeyRight() || (player.getvSpeed() != 0.0 && player.getvPos() < 800))) {
				player.move();
				if(count % 7 == 0) {
					 player.switchPlayerImg(true);
				 }
			} else {
				 player.switchPlayerImg(false);
			}
			//überprüft Vorraussetzungen zum Klettern und ruft die passende Methode im Player auf
			if(gameState.isControlsEnabled() && gameState.canClimb()){
				player.setCanClimb(true);
				if(player.isPressedKeyUp() || player.isPressedKeyDown()){
				  player.climb();
				}
			} else {
				player.setCanClimb(false);
				player.setClimbing(false);
			}
			//bei Kollision mit einem Fass wird das Leben des Spielers verringert
			if(gameState.checkForPlayerBarrelCollision()) {
				gameState.updatePlayerHealth();
			}
			//wenn der Spieler das Ziel erreicht hat verschwindet er
			if(gameState.hasReachedGoal()) {
				player.disappear();
			}
			//Wenn der Spieler kein Leben mehr hat, wird eine Meldung angezeigt und das Spiel wird beendet
			if(player.getHealth() <= 0) {
				main.getContrLevel().gameOver(gameState.getScore());
				gameState.endGame(true);
				paused = true;
			}
			//Thread wird pausiert wenn paused == true
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
	}
	
	/*
	 * Berechnet score und beendet das Level
	 */
	public void endLevel() {
		gameState.addToScore(calcTimeBonus(stopTimer()));
		gameState.endGame(false);
	}
	
	/*
	 * setzt paused = true und pausiert damit den Thread
	 */
	public void pauseThread() {
		paused = true;
	}
	
	/*
	 * Lässt Thread weiterlaufen
	 */
	public void resumeThread() {
		synchronized (pauseLock) {
			paused = false;
			count = -1;
			startTimer();
			pauseLock.notifyAll();
		}
	}

}
