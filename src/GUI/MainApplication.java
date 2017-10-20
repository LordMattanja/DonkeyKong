package GUI;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainApplication extends Application{
	
	private Stage window;
	private Scene levelScene;
	private FXMLLoader loader;
	private Pane root;
	private LevelController contrLevel;
	
	private void initialize() {
		loader = new FXMLLoader(getClass().getResource("Level.fxml"));
		try {
			root = (Pane)loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		window.setHeight(800);
		window.setWidth(500);
		levelScene = new Scene(root);
		contrLevel = loader.getController();
		window.setScene(levelScene);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		initialize();
		
		window.show();
		
	}

	public static void main(String[] args) {    
	    launch(args);   
	 }
	
}
