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
import utils.Settings;

public class GameState {
	
	private Player player;
	private Enemy enemy;
	private ArrayList<MovingGameObject> movingGameObjects;
	private ArrayList<StaticGameObject> staticGameObjects;
	
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
		player = new Player(Settings.playerStartingPosX, Settings.playerStartingPosY);
		movingGameObjects = new ArrayList<>();
		staticGameObjects = new ArrayList<>();
		for(int i = 0; i < Settings.numberOfPlatforms; i++){
			int tilt = (i%2 == 0)? -10 : 10;
			staticGameObjects.add(new Platform(50.0+25*(i%2), 600/Settings.numberOfPlatforms*i+50.0, true, tilt));
			if(i < Settings.numberOfPlatforms-1){
				for (int j = 0; j < rand.nextInt(2)+2; j++){
					Double hpos = rand.nextDouble()*(Settings.platformLength-50)+75;
					staticGameObjects.add(new Ladder(hpos, 600/Settings.numberOfPlatforms*i+60.0, 600.0/Settings.numberOfPlatforms));					
				}
			}
		}
		staticGameObjects.add(new Platform(-5.0, Settings.playerStartingPosY+30, true, 0));
	}

	
}
