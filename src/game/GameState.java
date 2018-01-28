package game;

import java.util.ArrayList;
import java.util.Random;
import general.Game;
import general.Settings;
import general.XMLFileWriter;
import gui.MainApplication;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import objects.Barrel;
import objects.GameObject;
import objects.Goal;
import objects.Ladder;
import objects.Platform;
import objects.Player;

public class GameState {
	
	private Player player;
	private String playerName;
	private int score, level;
	private ArrayList<GameObject> gameObjects;
	private ArrayList<Platform> platforms;
	private ArrayList<Barrel> barrels;
	private ArrayList<Ladder> ladders;
	private MainApplication main;
	private boolean gameActive = false, controlsEnabled = false;
	private IntegerProperty healthProperty;

	public IntegerProperty getHealthProperty() {
		return healthProperty;
	}
	
	
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
	
	public GameState() {
		main = MainApplication.getMain();
		score = 0;
		level = 0;
		gameObjects = new ArrayList<>();
		platforms = new ArrayList<Platform>();
		ladders = new ArrayList<Ladder>();
		barrels = new ArrayList<Barrel>();
		this.healthProperty = new SimpleIntegerProperty(Settings.playerHealth);
	}
	
	public void initLevel() {
		Random rand = new Random();
		Platform platform;
		level++;
		gameObjects.add(new Goal());
		for(int i = 0; i < Settings.numberOfPlatforms; i++){
			int tilt = (i%2 == 0)? -10 : 10, numOfLadders = (i!=Settings.numberOfPlatforms-1)? rand.nextInt(2)+1 : 1;
			platform = new Platform(25.0*(i%2), 500/Settings.numberOfPlatforms*i+100.0, Settings.tiltedPlatformLength, tilt, numOfLadders);
			platforms.add(platform);
			gameObjects.add(platforms.get(i));
			addLadder(platform.getLadders());
		}
		platform = new Platform(-5.0, Settings.playerStartingPosY+1.01, Settings.platformLength, 0, 1);
		platforms.add(platform);	
		gameObjects.add(platform);	
		addLadder(platform.getLadders());
		player = new Player(Settings.playerStartingPosX, Settings.playerStartingPosY, this);
		gameObjects.add(player);
	}

	public synchronized boolean checkForObjectCollision(GameObject obj){
		if(obj == player && player.isClimbing()) return false;
		for(Platform platform : platforms){
			if(platform.getShape().getBoundsInParent().intersects(player.getShape().getBoundsInParent())) {
				Shape intersecting = Shape.intersect((Shape)obj.getShape(), (Shape)platform.getShape());
				if(intersecting.getBoundsInParent().getHeight() > 0 && intersecting.getBoundsInParent().getWidth() >0){
					return true;
				}
			}
		}
		return false;
	}
	
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
	
	public synchronized boolean checkForPolygonCollision(Polygon poly){
		if(poly.getPoints().isEmpty()) return false;
		for(Platform platform : platforms){
			if(platform.getShape().getBoundsInParent().intersects(player.getShape().getBoundsInParent())) {
				Shape intersecting = Shape.intersect((Shape)poly, (Shape)platform.getShape());
				if(intersecting.getBoundsInParent().getHeight() > 0 && intersecting.getBoundsInParent().getWidth() >0){
					return true;
				}
			}
		}
		return false;
	}

	public synchronized Platform getCollidingPlatform(){
		double x = player.gethPos(), y = player.getvPos();
		for(Platform platform : platforms){
				if(platform.getShape().getBoundsInLocal().getMinY() - y > 30) continue;
				for(int i = 0; i < 2; i++) {
					x = player.gethPos();
					for(int j = 0; j < 2; j++) {
						if(platform.getShape().contains(x, y)){
						  return platform;
						}
						x = player.gethPos() + 20;
					}
					y = player.getvPos() - 30;
				}
				y = player.getvPos();
			}
		return null;
	}
	
	public synchronized Platform getCurrentlyUsedPlatform(GameObject obj) {
		for(Platform platform : platforms) {
			if(platform.getShape().getBoundsInParent().intersects(obj.getShape().getBoundsInParent())) {
				return platform;
			}
		}
		return null;
	}
	
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
	
	public synchronized void addLadder(Ladder[] ladders) {
		for(Ladder ladder : ladders) {
			if(!this.ladders.contains(ladder)) {
				this.ladders.add(ladder);
			}
			if(!gameObjects.contains(ladder)) {
				gameObjects.add(ladder);
			}
		}
	}
	
	protected synchronized void addBarrel(){
		Random rand = new Random();
		Barrel barrel = new Barrel(Settings.barrelStartingPosX, Settings.barrelStartingPosY, true, Settings.barrelSize, rand.nextInt(3));
		barrels.add(barrel);
		gameObjects.add(barrel);
		main.getContrLevel().paintObject(barrel);
	}
	
	public synchronized void removeBarrel(Barrel barrel) {
		main.getContrLevel().removeObject(barrel);
		barrels.remove(barrel);
		gameObjects.remove(barrel);
	}
	
	public void addToScore(int timeBonus) {
		score += 1000;
		score += level*100;
		score += timeBonus;
		score += healthProperty.intValue()*200;
	}
	

	public void updatePlayerHealth() {
		javafx.application.Platform.runLater(new Runnable() {
			@Override
			public void run() {
				healthProperty.setValue(healthProperty.intValue()-1);
			}			
		});		
	}
	
	public synchronized void endGame(boolean gameover){
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
		player = null;
		if(!gameover){
			main.startGame(true);
		} else {
			XMLFileWriter.addNewGame(new Game(playerName, score, level));
			level = 0;
			score = 0;
		}
	}
	
	
	
}