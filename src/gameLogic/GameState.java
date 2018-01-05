package gameLogic;

import java.util.ArrayList;
import java.util.Random;

import gui.MainApplication;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import objects.Barrel;
import objects.Enemy;
import objects.GameObject;
import objects.Ladder;
import objects.MovingGameObject;
import objects.Platform;
import objects.Player;
import objects.StaticGameObject;
import objects.Platform;
import utils.Settings;

public class GameState {
	
	private Player player;
	private Enemy enemy;
	private ArrayList<MovingGameObject> movingGameObjects;
	private ArrayList<StaticGameObject> staticGameObjects;
	private ArrayList<Platform> platforms;
	private ArrayList<Barrel> barrels;
	private MainApplication main;
	
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	public Enemy getEnemy() {
		return enemy;
	}
	public void setEnemy(Enemy enemy) {
		this.enemy = enemy;
	}
	public ArrayList<Platform> getPlatforms() {
		return platforms;
	}
	public ArrayList<Barrel> getBarrels() {
		return barrels;
	}
	public ArrayList<StaticGameObject> getStaticGameObjects() {
		return staticGameObjects;
	}
	public void setStaticGameObjects(ArrayList<StaticGameObject> staticGameObjects) {
		this.staticGameObjects = staticGameObjects;
	}
	public ArrayList<MovingGameObject> getMovingGameObjects() {
		return movingGameObjects;
	}
	public void setMovingGameObjects(ArrayList<MovingGameObject> gameObjects) {
		this.movingGameObjects = gameObjects;
	}
	
	
	public GameState() {
		main = MainApplication.getMain();
		Random rand = new Random();
		player = new Player(Settings.playerStartingPosX, Settings.playerStartingPosY, this);
		movingGameObjects = new ArrayList<>();
		staticGameObjects = new ArrayList<>();
		platforms = new ArrayList<Platform>();
		barrels = new ArrayList<Barrel>();
		for(int i = 0; i < Settings.numberOfPlatforms; i++){
			int tilt = (i%2 == 0)? -10 : 10;
			Platform platform = new Platform(25.0*(i%2), 600/Settings.numberOfPlatforms*i+50.0, Settings.tiltedPlatformLength, true, tilt, rand.nextInt(2)+1);
			platforms.add(platform);
			staticGameObjects.add(platforms.get(i));
			addLadder(platform.getLadders());
		}
		platforms.add(new Platform(-5.0, Settings.playerStartingPosY+.01, Settings.platformLength, true, 0, 1));
		staticGameObjects.add(platforms.get(Settings.numberOfPlatforms));
	}
	
	public synchronized boolean checkObjectCollision(GameObject obj){
		if(obj == player && player.isClimbing()) return false;
		for(Platform platform : platforms){
			if(platform.getPolygon().getBoundsInParent().intersects(player.getPolygon().getBoundsInParent())) {
				Shape intersecting = Shape.intersect((Shape)obj.getPolygon(), (Shape)platform.getPolygon());
				if(intersecting.getBoundsInParent().getHeight() > 0 && intersecting.getBoundsInParent().getWidth() >0){
					return true;
				}
			}
		}
		return false;
	}
	
	public synchronized boolean checkPolygonCollision(Polygon poly){
		if(poly.getPoints().isEmpty()) return false;
		for(Platform platform : platforms){
			if(platform.getPolygon().getBoundsInParent().intersects(player.getPolygon().getBoundsInParent())) {
				if(platform.getPolygon().getPoints().isEmpty()) return false;
				Shape intersecting = Shape.intersect((Shape)poly, (Shape)platform.getPolygon());
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
//				if(platform.getPolygon().getPoints().isEmpty()) return null;
				if(platform.getPolygon().getBoundsInLocal().getMinY() - y > 30) continue;
				for(int i = 0; i < 2; i++) {
					x = player.gethPos();
					for(int j = 0; j < 2; j++) {
						if(platform.getPolygon().contains(x, y)){
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
	
	public synchronized boolean playerBarrelCollision(){
		double x = player.gethPos(), y = player.getvPos();
		for (Barrel barrel : barrels) {
			if(barrel.getPolygon().getBoundsInParent().getMinY() - y > 45) continue;
				for(int i = 0; i < 2; i++) {
					x = player.gethPos();
					for(int j = 0; j < 2; j++) {
						if(barrel.getPolygon().contains(x, y)){
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
	
	public synchronized boolean canClimb(){
		for (StaticGameObject staticObject : staticGameObjects) {
			if(staticObject.getClass().equals(Ladder.class)){
				Shape intersecting = Shape.intersect((Shape)player.getPolygon(), (Shape)staticObject.getPolygon());
				if(intersecting.getBoundsInParent().getWidth() > 10 && intersecting.getBoundsInParent().getHeight() > 1){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean playerPlatformCollision() {
		double x = player.gethPos(), y = player.getvPos();
		for(Platform platform : platforms) {
			if(platform.getPolygon().getBoundsInLocal().getMinY() - y > 30) continue;
			for(int i = 0; i < 2; i++) {
				x = player.gethPos();
				for(int j = 0; j < 2; j++) {
					if(platform.getPolygon().contains(x, y)){
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
	
	public void addLadder(Ladder[] ladders) {
		for(Ladder ladder : ladders) {
			if(!staticGameObjects.contains(ladder)) {
				staticGameObjects.add(ladder);
			}
		}
	}
	
	protected void addBarrel(){
		Barrel barrel = new Barrel(Settings.barrelStartingPosX, Settings.barrelStartingPosY, false, this);
		barrels.add(barrel);
		System.out.println("added barrel");
		movingGameObjects.add(barrel);
		main.getContrLevel().paintObject(barrel);
	}
	
	public void endGame(){
		main.setGameActive(false);
		System.out.println("game over");
	}
	
}