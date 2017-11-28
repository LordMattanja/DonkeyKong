package gameLogic;

import java.util.ArrayList;
import java.util.Random;

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
		Random rand = new Random();
		player = new Player(Settings.playerStartingPosX, Settings.playerStartingPosY, this);
		movingGameObjects = new ArrayList<>();
		staticGameObjects = new ArrayList<>();
		platforms = new ArrayList<Platform>();
		barrels = new ArrayList<Barrel>();
		for(int i = 0; i < Settings.numberOfPlatforms; i++){
			int tilt = (i%2 == 0)? -10 : 10;
			platforms.add(new Platform(50.0+25*(i%2), 600/Settings.numberOfPlatforms*i+50.0, Settings.tiltedPlatformLength, true, tilt));
			staticGameObjects.add(platforms.get(i));
				for (int j = 0; j < rand.nextInt(2)+1; j++){
					Double hpos = rand.nextDouble()*(Settings.tiltedPlatformLength-50)+75;
					staticGameObjects.add(new Ladder(hpos, 600/Settings.numberOfPlatforms*i+60.0, 600.0/Settings.numberOfPlatforms));	
			}
		}
		platforms.add(new Platform(-5.0, Settings.playerStartingPosY+30.01, Settings.platformLength, true, 0));
		staticGameObjects.add(platforms.get(Settings.numberOfPlatforms));
		addBarrel();
	}
	
	public boolean checkObjectCollision(GameObject obj){
		if(obj == player && player.isClimbing()) return false;
		for(int i = 0; i < platforms.size(); i++){
			Shape intersecting = Shape.intersect(obj.getPolygon(), platforms.get(i).getPolygon());
			if(intersecting.getBoundsInParent().getHeight() > 0 && intersecting.getBoundsInParent().getWidth() >0){
				return true;
			}
		}
		return false;
	}
	
	public boolean checkPolygonCollision(Polygon poly){
		for(int i = 0; i < platforms.size(); i++){
			Shape intersecting = Shape.intersect(poly, platforms.get(i).getPolygon());
			if(intersecting.getBoundsInParent().getHeight() > 0 && intersecting.getBoundsInParent().getWidth() >0){
				return true;
			}
		}
		return false;
	}

	public Platform getCollidingPlatform(Polygon poly){
		for(int i = 0; i < platforms.size(); i++){
			Shape intersecting = Shape.intersect(poly, platforms.get(i).getPolygon());
			if(intersecting.getBoundsInParent().getHeight() > 0 && intersecting.getBoundsInParent().getWidth() >0){
				return platforms.get(i);
			}
		}
		return null;
	}
	
	public boolean canClimb(){
		for (StaticGameObject staticObject : staticGameObjects) {
			if(staticObject.getClass().equals(Ladder.class)){
				if(Shape.intersect(player.getPolygon(),staticObject.getPolygon()).getBoundsInParent().getWidth() > 10){
					return true;
				}
			}
		}
		return false;
	}
	
	
	protected void addBarrel(){
		Barrel barrel = new Barrel(Settings.barrelStartingPosX, Settings.barrelStartingPosY, false, this);
		barrels.add(barrel);
		movingGameObjects.add(barrel);
		new Thread(barrel).start();
	}
	
}
	