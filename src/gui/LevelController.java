package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.sun.media.jfxmedia.events.PlayerStateEvent;

import gameLogic.GameState;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
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
	private ArrayList<MovingGameObject> movingObjects;
	private IntegerProperty playerHealthProperty;

	private boolean isPressedKeyRight, isPressedKeyLeft;
	
	public boolean isPressedKeyRight() {
		return isPressedKeyRight;
	}

	public boolean isPressedKeyLeft() {
		return isPressedKeyLeft;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		window = main.getWindow();
		gameState = main.getGamestate();
		
		playerPolygon = player.getPolygon();
		
		gamePane.getChildren().add(playerPolygon);
		
		movingObjects = gameState.getMovingGameObjects();
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
		
		scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
				if(event.getCode() == KeyCode.LEFT) {
					player.setPressedKeyLeft(true);
					System.out.println("left");
				} else if(event.getCode() == KeyCode.RIGHT) {
					player.setPressedKeyRight(true);
					System.out.println("right");
				}
				if(event.getCode() == KeyCode.SPACE) {
					if(player.isGrounded()){
					  player.setVSpeed(-11.0);
					  player.setClimbing(false);
					  System.out.println("jump");
					}
				}	
				if(event.getCode() == KeyCode.UP){
						player.setPressedKeyUp(true);
				} else if(event.getCode() == KeyCode.DOWN){
						player.setPressedKeyDown(true);
				}
		});
		
		
		scene.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
				if(event.getCode() == KeyCode.LEFT) {
					player.setPressedKeyLeft(false);
				}
				if (event.getCode() == KeyCode.RIGHT) {
					player.setPressedKeyRight(false);
				}	
				if(event.getCode() == KeyCode.UP){
					player.setPressedKeyUp(false);
			    } else if(event.getCode() == KeyCode.DOWN){
					player.setPressedKeyDown(false);
			    }
		});
	}
	
	public synchronized void repaint(){
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				while(main.isGameActive()){
				  movingObjects = gameState.getMovingGameObjects();
				  gamePane.getChildren().remove(playerPolygon);
				  gamePane.getChildren().add(playerPolygon);	
				  for(int i = 0; i < movingObjects.size(); i++){
					  if(movingObjects.get(i).getPolygon() != null){
						  gamePane.getChildren().remove(movingObjects.get(i).getPolygon());
					      gamePane.getChildren().add(movingObjects.get(i).getPolygon());				
					  }
				  }
				}
			try {
				Thread.sleep(33);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			
		});		
	}
	

}
