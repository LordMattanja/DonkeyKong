package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import objects.Player;

public class LevelController implements Initializable{
	
	private MainApplication main;
	private Player player;
	@FXML
	private Pane gamePane;
	
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		gamePane.getChildren().add(player.getPolygon());
		
		gamePane.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.LEFT) {
					player.sethSpeed(-.5);
				} else if(event.getCode() == KeyCode.RIGHT) {
					player.sethSpeed(.5);
				}
			}
			
		});
	}
	
	public LevelController () {
		main = MainApplication.getMain();
		player = main.getGamestate().getPlayer();
		
	}
	
	

}
