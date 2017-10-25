package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import objects.Player;

public class LevelController implements Initializable{
	
	private MainApplication main;
	private Player player;
	@FXML
	private Pane gamePane;
	private Stage window;
	private Scene scene;
	
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		window = main.getWindow();
		
		gamePane.getChildren().add(player.getPolygon());
		
		
		
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
				if(event.getCode() == KeyCode.LEFT) {
					player.sethSpeed(.0);
				} else if(event.getCode() == KeyCode.RIGHT) {
					player.sethSpeed(.0);
				}
			}
			
		});
	}
	

}
