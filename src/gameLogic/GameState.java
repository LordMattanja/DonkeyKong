package gameLogic;

import java.util.ArrayList;

import objects.Enemy;
import objects.GameObject;
import objects.Player;
import utils.Settings;

public class GameState {
	
	private Player player;
	private Enemy enemy;
	private ArrayList<GameObject> gameObjects;
	
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
	public ArrayList<GameObject> getGameObjects() {
		return gameObjects;
	}
	public void setGameObjects(ArrayList<GameObject> gameObjects) {
		this.gameObjects = gameObjects;
	}

	
	public GameState() {
		player = new Player(Settings.playerStartingPosX, Settings.playerStartingPosY);
	}

	
}
