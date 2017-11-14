package gameLogic;

import java.util.ArrayList;
import java.util.Random;

import objects.Enemy;
import objects.GameObject;
import objects.Ladder;
import objects.MovingGameObject;
import objects.Platform;
import objects.Player;
import objects.StaticGameObject;
import objects.TiltedPlatform;
import utils.Settings;

public class GameState {
	
	private Player player;
	private Enemy enemy;
	private ArrayList<MovingGameObject> movingGameObjects;
	private ArrayList<StaticGameObject> staticGameObjects;
	private ArrayList<Platform> platforms;
	
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
		for(int i = 0; i < Settings.numberOfPlatforms; i++){
			int tilt = (i%2 == 0)? -10 : 10;
			platforms.add(new TiltedPlatform(50.0+25*(i%2), 600/Settings.numberOfPlatforms*i+50.0, true, tilt));
			staticGameObjects.add(platforms.get(i));
				for (int j = 0; j < rand.nextInt(2)+1; j++){
					Double hpos = rand.nextDouble()*(Settings.tiltedPlatformLength-50)+75;
					staticGameObjects.add(new Ladder(hpos, 600/Settings.numberOfPlatforms*i+60.0, 600.0/Settings.numberOfPlatforms));	
			}
		}
		platforms.add(new Platform(-5.0, Settings.playerStartingPosY+30.1, true));
		staticGameObjects.add(platforms.get(Settings.numberOfPlatforms));
	}
	
	public boolean checkPlayerCollision(){
		if(player.isClimbing()) return false;
		for(int i = 0; i < platforms.size(); i++){
			if(player.getPolygon().getBoundsInParent().intersects(platforms.get(i).getPolygon().getBoundsInParent())){
				return true;
			}
		}
		return false;
	}
	
	public boolean canClimb(){
		for (StaticGameObject staticObject : staticGameObjects) {
			if(staticObject.getClass().equals(Ladder.class)){
				if(player.getPolygon().getBoundsInParent().intersects(staticObject.getPolygon().getBoundsInParent())){
					return true;
				}
			}
		}
		return false;
	}

	
}
