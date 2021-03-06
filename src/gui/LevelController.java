package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import game.GameState;
import general.Settings;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
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
import objects.GameObject;
import objects.Player;

public class LevelController implements Initializable{
	
	private MainApplication main;
	private GameState gameState;
	private Player player;
	@FXML
	private AnchorPane gamePane;
	private Scene scene;
	@FXML 
	private Label playerHealthLabel;
	@FXML
	private Label scoreLabel;
	@FXML 
	private Label levelLabel;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		gameState = main.getGamestate();
	}
	
	public LevelController () {
		main = MainApplication.getMain();	
	}
	
	
	public void initGame() {	

		player = main.getGamestate().getPlayer();	
		scene = main.getLevelScene();
		
		levelLabel.setText("Level: " + main.getGamestate().getLevel());
		scoreLabel.setText("Score: " + main.getGamestate().getScore());
		
		

		ArrayList<GameObject> staticObjects = gameState.getGameObjects();
		
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
				if(event.getCode() == KeyCode.CONTROL && gameState.isGameActive() && gameState.isControlsEnabled()) {
					if(player.isGrounded() || player.isClimbing()){
					  player.setVSpeed(Settings.playerJumpSpeed);
					  player.stopClimbing();
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
		
		gamePane.requestFocus();
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
	
	public void updateHealth(){
		playerHealthLabel.setText(player.getHealth() + "");
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
	
	@FXML 
	protected void backToMenu() {
		main.getGameThread().pauseThread();
		gameState.endGame(true);
		main.setMenuScene();
	}
	

}
