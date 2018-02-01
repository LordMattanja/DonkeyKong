package game;

import java.util.ArrayList;
import java.util.Random;
import general.Game;
import general.Settings;
import general.XMLFileManager;
import gui.MainApplication;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import objects.Barrel;
import objects.GameObject;
import objects.Goal;
import objects.Ladder;
import objects.Platform;
import objects.Player;

public class GameState {
	
	//Instanz des Spielers für direkten Zugriff in verschiedenen Methoden
	private Player player;
	//Name den der Spieler am Anfang angibt und der am Ende in die Bestenliste eingetragen wird
	private String playerName;
	//Attribute für Punktzahl und Level
	private int score, level;
	//Listen aller Spielobjekte
	private ArrayList<GameObject> gameObjects;
	private ArrayList<Platform> platforms;
	private ArrayList<Barrel> barrels;
	private ArrayList<Ladder> ladders;
	private MainApplication main;
	//boolean-Attribute ob das Spiel läuft und die Steuerung aktiviert ist
	private boolean gameActive = false, controlsEnabled = false;
	
	public void setPlayerName(String name) {
		playerName = name;
	}
	
	public int getScore() {
		return score;
	}

	public int getLevel() {
		return level;
	}

	public boolean isGameActive() {
		return gameActive;
	}
		
	public void setGameActive(boolean active){
		gameActive = active;
	}
	
	public boolean isControlsEnabled() {
		return controlsEnabled;
	}

	public void setControlsEnabled(boolean controlsEnabled) {
		this.controlsEnabled = controlsEnabled;
	}

	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public ArrayList<Platform> getPlatforms() {
		return platforms;
	}
	public ArrayList<Barrel> getBarrels() {
		return barrels;
	}
	public ArrayList<Ladder> getLadders() {
		return ladders;
	}

	public ArrayList<GameObject> getGameObjects() {
		return gameObjects;
	}	

	//Konstruktor der Listen und Attribute initialisiert
	public GameState() {
		main = MainApplication.getMain();
		score = 0;
		level = 0;
		gameObjects = new ArrayList<>();
		platforms = new ArrayList<Platform>();
		ladders = new ArrayList<Ladder>();
		barrels = new ArrayList<Barrel>();
	}

	//Methode die ein neues Level initialisiert
	public void initLevel() {
		Random rand = new Random();
		Platform platform;
		level++;
		gameObjects.add(new Goal());
		for(int i = 0; i < Settings.numberOfPlatforms; i++){
			int tilt = (i%2 == 0)? -10 : 10; 
			int numOfLadders = (i!=Settings.numberOfPlatforms-1)? rand.nextInt(3)+1 : 1;
			platform = new Platform(25.0*(i%2), 500/Settings.numberOfPlatforms*i+100.0, Settings.tiltedPlatformLength, tilt, numOfLadders);
			platforms.add(platform);
			gameObjects.add(platforms.get(i));
			addLadder(platform.getLadders());
		}
		platform = new Platform(-5.0, Settings.playerStartingPosY+1.01, Settings.platformLength, 0, 1);
		platforms.add(platform);	
		gameObjects.add(platform);	
		addLadder(platform.getLadders());
		player = (player!=null)? player : new Player(Settings.playerStartingPosX, Settings.playerStartingPosY, this);
		gameObjects.add(player);
	}
	
	/*
	 * Die Methode überprüft, ob der Spieler das Ziel erreicht hat
	 */
	public synchronized boolean hasReachedGoal() {
		double x = player.gethPos(), y = player.getvPos();
		for(GameObject object : gameObjects) {
			if(object.getClass().equals(Goal.class)) {
				if(object.getShape().getBoundsInLocal().getMinY() - y > 30) continue;
				for(int i = 0; i < 2; i++) {
					x = player.gethPos();
					for(int j = 0; j < 2; j++) {
						if(!object.getShape().contains(x, y)){
						  return false;
						}
						x = player.gethPos() + 20;
					}
					y = player.getvPos() - 30;
				}
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Die Methode überprüft für jede Plattform ob das GameObject mit ihr kollididert und gibt falls ja die Plattform zurück
	 */
	public synchronized Platform getCurrentlyUsedPlatform(GameObject obj) {
		for(Platform platform : platforms) {
			if(platform.getShape().getBoundsInParent().intersects(obj.getShape().getBoundsInParent())) {
				return platform;
			}
		}
		return null;
	}

	/*
	 * Überprüft, ob der Spieler mit einem Fass kollidiert und gibt in dem Fall true zurück.
	 */
	public synchronized boolean checkForPlayerBarrelCollision(){
		for (Barrel barrel : barrels) {
			if(barrel.getShape().getBoundsInParent().getMinY() - player.getvPos() > 45)continue;
				Shape intersectingShape = Polygon.intersect((Shape)player.getShape(), (Shape)barrel.getShape());
				if(intersectingShape.getBoundsInParent().getHeight() > 0 && intersectingShape.getBoundsInParent().getWidth() > 0){
					removeBarrel(barrel);
					return true;
			}
		}
		return false;
	}

	/*
	 * Die Methode überprüft, ob der Spieler nah genug vor einer Leiter steht (also sich die Shapes weit genug überschneiden, 
	 * dass er klettern kann und gibt in dem Fall true zurück
	 */
	public synchronized boolean canClimb() {
		for (Ladder ladder : ladders) {
			if (ladder.getShape().getBoundsInParent().getMinY() - player.getvPos() > 120)
				continue;
			Shape intersecting = Shape.intersect(player.getShape(), ladder.getShape());
			if (intersecting.getBoundsInParent().getWidth() > 15 && intersecting.getBoundsInParent().getHeight() > 1) {
				if(!(player.getvPos() <= ladder.getvPos() + ladder.getHeight()) && player.isClimbing()) {
					if(player.isClimbing()) {
						player.setvPos(player.getvPos() - 3);
						player.setClimbing(false);
						player.setCanClimb(false);
					}					
					return false;
				}else {
					return true;
				}
			}
		}
		return false;
	}

	/*
	 * Gibt die Leiter zurück, die der Spieler gerade nutzt, indem sie für jede Leiter die "Kollision" überprüft.
	 */
	public synchronized Ladder getUsedLadder(GameObject object) {
		for (Ladder ladder : ladders) {
			if (object.equals(player) && ladder.getShape().getBoundsInParent().getMinY() - object.getvPos() > 120)
				continue;
			Shape intersecting = Shape.intersect(object.getShape(), ladder.getShape());
			if (intersecting.getBoundsInParent().getWidth() > 15 && intersecting.getBoundsInParent().getHeight() > 1
					&& (object.getShape().getBoundsInParent().getMaxY() <= ladder.getvPos() + ladder.getHeight())) {
				return ladder;
			}
		}
		return null;
	}

	/*
	 * Überprüft ob der Spieler mit einer Plattform kollidiert und gibt das zurück.
	 */
	public synchronized boolean playerPlatformCollision() {
		if(canClimb() && player.isClimbing()) return false;
		double x = player.gethPos(), y = player.getvPos();
		for(Platform platform : platforms) {
			if(platform.getShape().getBoundsInLocal().getMinY() - y > 30) continue;
			for(int i = 0; i < 2; i++) {
				x = player.gethPos();
				for(int j = 0; j < 2; j++) {
					if(platform.getShape().contains(x, y)){
					  return true;
					}
					x = player.gethPos() + 20;
				}
				y = player.getvPos() - 30;
			}
			y = player.getvPos();
		}
		return false;
	}
	
	/*
	 * Bekommt ein Array an Leitern übergeben und fügt diese dann zu allen nötigen Listen hinzu.
	 */
	public void addLadder(Ladder[] ladders) {
		for(Ladder ladder : ladders) {
			if(!this.ladders.contains(ladder)) {
				this.ladders.add(ladder);
			}
			if(!gameObjects.contains(ladder)) {
				gameObjects.add(ladder);
			}
		}
	}
	
	/*
	 * Fügt ein Fass zu den benötigten Listen hinzu.
	 */
	protected synchronized void addBarrel(){
		Random rand = new Random();
		Barrel barrel = new Barrel(Settings.barrelStartingPosX, Settings.barrelStartingPosY, true, Settings.barrelSize, rand.nextInt(3));
		barrels.add(barrel);
		gameObjects.add(barrel);
		main.getContrLevel().paintObject(barrel);
	}
	
	/*
	 * Entfernt ein Fass von allen Listen
	 */
	public synchronized void removeBarrel(Barrel barrel) {
		main.getContrLevel().removeObject(barrel);
		barrels.remove(barrel);
		gameObjects.remove(barrel);
	}
	
	/*
	 * Berechnet die Punktzahl für das abgeschlossene Level und fügt diese dem Attribut hinzu
	 */
	public void addToScore(int timeBonus) {
		score += 1000;
		score += level*100;
		score += timeBonus;
		score += player.getHealth()*200;
	}
	
	/*
	 * Aktualisiert die Lebenspunkte des Spielers
	 */
	public void updatePlayerHealth() {
		javafx.application.Platform.runLater(new Runnable() {
			@Override
			public void run() {
				player.setHealth(player.getHealth()-1);
				main.getContrLevel().updateHealth();
			}			
		});		
	}
	
	/*
	 * Beendet das Spiel und setzt den GameState zurück
	 */
	public void endGame(boolean gameover){
		main.getGameThread().pauseThread();
		gameActive = false;
		controlsEnabled = false;
		for(GameObject object : gameObjects) {
			main.getContrLevel().removeObject(object);
		}
		main.getContrLevel().removeObject(player);
		gameObjects.clear();
		barrels.clear();
		platforms.clear();
		ladders.clear();
		if(!gameover){
			main.startGame(true);
		} else {
			XMLFileManager.addNewGame(new Game(playerName, score, level));
			player = null;
			level = 0;
			score = 0;
		}
	}
	
	
	
}