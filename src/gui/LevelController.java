package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import gameLogic.GameState;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import objects.MovingGameObject;
import objects.Player;
import objects.StaticGameObject;

public class LevelController implements Initializable{
	
	private MainApplication main;
	private GameState gameState;
	private Player player;
	private Polygon playerPolygon;
	@FXML
	private Pane gamePane;
	private Stage window;
	private Scene scene;
	
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		window = main.getWindow();
		gameState = main.getGamestate();
		
		playerPolygon = player.getPolygon();
		
		gamePane.getChildren().add(playerPolygon);
		
		ArrayList<MovingGameObject> movingObjects = gameState.getMovingGameObjects();
		ArrayList<StaticGameObject> staticObjects = gameState.getStaticGameObjects();
		
		for(int i = 0; i < movingObjects.size(); i++){
			gamePane.getChildren().add(movingObjects.get(i).getPolygon());
		}		
		for (int i = 0; i < staticObjects.size(); i++){
			gamePane.getChildren().add(staticObjects.get(i).getPolygon());
		}
		
	}
	
	public LevelController () {
		main = MainApplication.getMain();
		player = main.getGamestate().getPlayer();
		
		
	}
	
	
	public void initGame() {
		scene = main.getLevelScene();
		
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.LEFT) {
					player.sethSpeed(-.5);
					System.out.println("left");
				} else if(event.getCode() == KeyCode.RIGHT) {
					player.sethSpeed(.5);
					System.out.println("right");
				}
			}
			
		});
		
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT) {
					player.sethSpeed(.0);
				}
			}
			
		});
	}
	
	public synchronized void repaint(){
		gamePane.getChildren().remove(playerPolygon);
		System.out.println("repainting");
		
		gamePane.getChildren().add(playerPolygon);		
	}
	

}
