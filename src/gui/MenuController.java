package gui;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import objects.Barrel;

public class MenuController implements Initializable{
	
	@FXML
	Button newGameButton, quitGameButton;
	
	Barrel backgroundBarrel;
	
	@FXML
	AnchorPane backgroundPane;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		backgroundBarrel = new Barrel(100, -50, false, 50, 0);
		Random rand= new Random();
		TranslateTransition transition = new TranslateTransition();
		
		transition.setCycleCount(1);
		transition.setDuration(Duration.seconds(4));
		transition.setInterpolator(Interpolator.EASE_IN);
		transition.setNode(backgroundBarrel.getPolygon());
		transition.setFromX(100);
		transition.setToX(100);
		transition.setFromY(-75);
		transition.setToY(850);
	    transition.setOnFinished(e -> {
	    	int x = rand.nextInt(500) + 50;
	    	transition.setFromX(x);
	    	transition.setToX(x);
	    	transition.play();
	    });
		transition.play();
		backgroundPane.getChildren().add(backgroundBarrel.getPolygon());
		
	}
	
	@FXML
	private void startGame() {
		MainApplication.getMain().startAgain(false);
	}
	
	@FXML
	private void quitGame() {
		Platform.exit();
	}

}
