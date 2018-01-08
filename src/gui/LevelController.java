package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.sun.media.jfxmedia.events.PlayerStateEvent;

import gameLogic.GameState;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PageLayout;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.stage.Stage;
import javafx.util.Duration;
import objects.Barrel;
import objects.GameObject;
import objects.Ladder;
import objects.MovingGameObject;
import objects.Player;
import objects.StaticGameObject;
import utils.Settings;

public class LevelController implements Initializable{
	
	private MainApplication main;
	private GameState gameState;
	private Player player;
	private Polygon playerPolygon;
	@FXML
	private AnchorPane gamePane;
	private Stage window;
	private Scene scene;
	private ArrayList<MovingGameObject> movingObjects;
	private IntegerProperty playerHealthProperty = new SimpleIntegerProperty();
	@FXML 
	private Label playerHealthLabel;
	@FXML 
	private Label levelLabel;

	private boolean isPressedKeyRight,  isPressedKeyLeft;
	
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
		
		
	}
	
	public LevelController () {
		main = MainApplication.getMain();	
	}
	
	
	public void initGame() {	
		System.out.println("initializing level");

		player = main.getGamestate().getPlayer();	
		scene = main.getLevelScene();
		
		levelLabel.setText("Level: " + main.getGamestate().getLevel());
		
		playerPolygon = player.getPolygon();
		playerHealthProperty.bind(player.getHealthProperty());
		playerHealthLabel.textProperty().bind(playerHealthProperty.asString());
		
			
		movingObjects = gameState.getMovingGameObjects();
		ArrayList<StaticGameObject> staticObjects = gameState.getStaticGameObjects();
		
//		for(int i = 0; i < movingObjects.size(); i++){
//			gamePane.getChildren().add(movingObjects.get(i).getPolygon());
//		}		
		for (int i = 0; i < staticObjects.size(); i++){
			gamePane.getChildren().add(staticObjects.get(i).getPolygon());
		}

		TranslateTransition playerEnterTransition = new TranslateTransition();
		
		playerEnterTransition.setCycleCount(1);
		playerEnterTransition.setDuration(Duration.seconds(3));
		playerEnterTransition.setNode(playerPolygon);
		playerEnterTransition.setFromX(0);
		playerEnterTransition.setToX(0);
		playerEnterTransition.setFromY(100);
		playerEnterTransition.setToY(0);
		playerEnterTransition.setOnFinished(e -> gameState.setControlsEnabled(true));
		playerEnterTransition.play();

		gamePane.getChildren().add(playerPolygon);
		System.out.println(playerPolygon.getBoundsInParent().getMinX() + " , "+ playerPolygon.getBoundsInParent().getMinY());
		
		scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
				if(event.getCode() == KeyCode.LEFT) {
					player.setPressedKeyLeft(true);
				} else if(event.getCode() == KeyCode.RIGHT) {
					player.setPressedKeyRight(true);
				}
				if(event.getCode() == KeyCode.SPACE) {
					if(player.isGrounded() || player.isClimbing()){
					  player.setVSpeed(-8.5);
					  player.setClimbing(false);
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
	
	public void createBarrelPath(Barrel barrel, int speed) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				PathTransition transition = new PathTransition();
				Polyline path = new Polyline();
				double verticalValue = barrel.getvPos();
				double horizontalValue = barrel.gethPos();
				for (int i = 0; i < Settings.numberOfPlatforms * 2; i++) {
					Double[] array = new Double[] { horizontalValue, verticalValue, };
					if (i % 4 == 0) {
						horizontalValue += Settings.tiltedPlatformLength;
						verticalValue += 20;
					} else if (i % 2 != 0) {
						verticalValue += 500 / Settings.numberOfPlatforms - 20;
					} else if (i % 2 == 0) {
						horizontalValue -= Settings.tiltedPlatformLength;
						verticalValue += 20;
					}
					path.getPoints().addAll(array);
				}
				verticalValue = Settings.playerStartingPosY-10;
				path.getPoints().addAll(new Double[] { horizontalValue, verticalValue,
						-20.0, verticalValue });
				transition.setCycleCount(1);
				transition.setDuration(Duration.seconds(32+speed));
				transition.setInterpolator(Interpolator.EASE_OUT);
				transition.setNode(barrel.getPolygon());
				transition.setPath(path);
				transition.setOnFinished(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						MainApplication.getMain().getContrLevel().removeObject(barrel);
					}
				});
				transition.play();
				barrel.setTransition(transition);
			}
		});
	}
	
//	public synchronized void repaint(){
////		System.out.println("repaintinG?");
//		Platform.runLater(new Runnable(){
//			@Override
//			public void run() {
//				while(main.isGameActive()){
//				  movingObjects = gameState.getMovingGameObjects();
//				  gamePane.getChildren().remove(playerPolygon);
//				  gamePane.getChildren().add(playerPolygon);	
//				  for(int i = 0; i < movingObjects.size(); i++){
//					  if(movingObjects.get(i).getPolygon() != null){
//						  gamePane.getChildren().remove(movingObjects.get(i).getPolygon());
//					      gamePane.getChildren().add(movingObjects.get(i).getPolygon());				
//					  }
//				  }
//				}
//				try {
//					Thread.sleep(33);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			
//		});		
//	}
	
	public void paintObject(GameObject obj) {
		if (obj != null && obj.getPolygon() != null) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					gamePane.getChildren().add(obj.getPolygon());
				}

			});
		}
	}
	
	public void removeObject(GameObject obj) {
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				gamePane.getChildren().remove(obj.getPolygon());
			}
		});
	}
	
	public void gameOver(int score) {
		Platform.runLater(new Runnable() {			
			@Override
			public void run() {
				Alert gameOverAlert = new Alert(AlertType.INFORMATION);
				gameOverAlert.setTitle("Game Over!");
				gameOverAlert.setContentText("Game Over \nScore: " + score);
				gameOverAlert.show();
				main.setMenuScene();
			}
		});
	}
	

}
