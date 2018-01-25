package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import game.GameState;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import objects.Barrel;
import objects.GameObject;
import objects.Ladder;
import objects.Player;
import utils.Settings;

public class LevelController implements Initializable{
	
	private MainApplication main;
	private GameState gameState;
	private Player player;
	@FXML
	private AnchorPane gamePane;
	private Scene scene;
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
		
		playerHealthProperty.bind(player.getHealthProperty());
		playerHealthLabel.textProperty().bind(playerHealthProperty.asString());
		
		ArrayList<GameObject> staticObjects = gameState.getGameObjects();
		
//		for(int i = 0; i < movingObjects.size(); i++){
//			gamePane.getChildren().add(movingObjects.get(i).getShape());
//		}		
		for (int i = 0; i < staticObjects.size(); i++){
			gamePane.getChildren().add(staticObjects.get(i).getShape());
		}

		TranslateTransition playerEnterTransition = new TranslateTransition();
		
		playerEnterTransition.setCycleCount(1);
		playerEnterTransition.setDuration(Duration.seconds(3));
		playerEnterTransition.setNode(player.getShape());
		playerEnterTransition.setFromX(0);
		playerEnterTransition.setToX(0);
		playerEnterTransition.setFromY(100);
		playerEnterTransition.setToY(0);
		playerEnterTransition.setOnFinished(e -> gameState.setControlsEnabled(true));
		playerEnterTransition.play();
		
		scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
				if(event.getCode() == KeyCode.LEFT) {
					player.setPressedKeyLeft(true);
				} else if(event.getCode() == KeyCode.RIGHT) {
					player.setPressedKeyRight(true);
				}
				if(event.getCode() == KeyCode.SPACE && gameState.isGameActive() && gameState.isControlsEnabled()) {
					if(player.isGrounded() || player.isClimbing()){
					  player.setVSpeed(-8.8);
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
	
	public void createNewBarrelPath(Barrel barrel, int speed, boolean rolling) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				createNextBarrelPath(barrel, speed, rolling, null);
			}
		}
		);
	}
	
	private synchronized void createNextBarrelPath(Barrel barrel, int speed, boolean rolling, Ladder usedLadder) {
		TranslateTransition transition = new TranslateTransition();
		RotateTransition rotate = new RotateTransition();
		transition.setNode(barrel.getShape());
		transition.setFromX(barrel.getShape().getBoundsInParent().getMinX());
		transition.setFromY(barrel.getShape().getBoundsInParent().getMaxY()- barrel.getvPos());
		transition.setInterpolator(Interpolator.LINEAR);
		transition.setCycleCount(1);
		if (rolling) {
			rotate = new RotateTransition(Duration.seconds(1), barrel.getShape());
			rotate.setByAngle((gameState.getCurrentlyUsedPlatform(barrel) != null)? gameState.getCurrentlyUsedPlatform(barrel).getTilt()*-36 : -360);
			rotate.setCycleCount(RotateTransition.INDEFINITE);
			rotate.setInterpolator(Interpolator.LINEAR);
			rotate.play();objects.Platform usedPlatform = gameState.getCurrentlyUsedPlatform(barrel);
			if (usedPlatform != null) {
				transition.setToX((usedPlatform.getTilt() < 0) ? 600 : 5);
				transition.setToY((usedPlatform.getTilt() < 0) ? usedPlatform.getvPos() - barrel.getvPos() - usedPlatform.getTilt() : usedPlatform.getvPos() - barrel.getvPos() + usedPlatform.getTilt());
				Random rand = new Random();
				int x = rand.nextInt(5);
				if (x < usedPlatform.getLadders().length && usedPlatform.getLadders()[x].getvPos() > barrel.getShape()
						.getBoundsInParent().getMaxY()) {
					usedLadder = usedPlatform.getLadders()[x];
					transition.setToX(usedLadder.gethPos());
					transition.setDuration(Duration
							.seconds((5.0)* Math.abs(((transition.getFromX()-transition.getToX())/Settings.tiltedPlatformLength))));
					transition.setToY(usedLadder.getvPos()-barrel.getvPos());
				} else {
					transition.setDuration(Duration.seconds((usedLadder == null)? 5 + speed / 10.0 : (5.0)* Math.abs(((transition.getFromX()-transition.getToX())/Settings.tiltedPlatformLength))));			
					usedLadder = null;
					}
			} else {
				usedLadder = null;
				transition.setToX(-25);
				transition.setInterpolator(Interpolator.EASE_OUT);
				transition.setDuration(Duration.seconds(5.0));
			}
		} else {
			transition.setDuration(Duration.seconds(1 + speed / 15.0));
			if (usedLadder != null) {
				transition.setByY(usedLadder.getHeight());
			} else {
				transition.setToY((barrel.getShape().getBoundsInParent().getMaxY() < Settings.playerStartingPosY - 100)
						? transition.getFromY() + 500 / Settings.numberOfPlatforms - 20
						: Settings.playerStartingPosY - barrel.getvPos());
			}
		}
		final RotateTransition rotateFinal = rotate;
		final Ladder finalLadder = usedLadder;
		transition.setOnFinished(e -> {
			if (barrel.getShape().getBoundsInParent().getMinX() < 0) {
				MainApplication.getMain().getGamestate().removeBarrel(barrel);
			} else {
				if (rolling) {
					rotateFinal.stop();
				}
				createNextBarrelPath(barrel, speed, !rolling, finalLadder);
			}
		});
		transition.play();
		barrel.setTransition(transition);
	}
	
	public void paintObject(GameObject obj) {
		if (obj != null && obj.getShape() != null) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					gamePane.getChildren().add(obj.getShape());
				}
			});
		}
	}
	
	public void removeObject(GameObject obj) {
		if (obj != null && obj.getShape() != null) {
			Platform.runLater(new Runnable(){
				@Override
				public void run() {
					gamePane.getChildren().remove(obj.getShape());
				}
			});
		}
	}
	
	public void gameOver(int score) {
		Platform.runLater(new Runnable() {			
			@Override
			public void run() {
				Alert gameOverAlert = new Alert(AlertType.INFORMATION);
				gameOverAlert.setTitle("Game Over!");
				gameOverAlert.setHeaderText("");
				gameOverAlert.getDialogPane().getStylesheets().add(getClass().getResource("Menu.css").toExternalForm());
				gameOverAlert.setContentText("Game Over \nScore: " + score);
				gameOverAlert.show();
				main.setMenuScene();
			}
		});
	}
	

}
