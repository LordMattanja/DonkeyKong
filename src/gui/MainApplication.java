package gui;

import java.io.IOException;

import gameLogic.GameThread;
import gameLogic.GameState;
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
	private GameState gamestate;
	private GameThread gameThread;
	private static MainApplication main;
	
	public Scene getLevelScene() {
		return levelScene;
	}
	public void setLevelScene(Scene levelScene) {
		this.levelScene = levelScene;
	}
	public LevelController getContrLevel() {
		return contrLevel;
	}
	public void setContrLevel(LevelController contrLevel) {
		this.contrLevel = contrLevel;
	}
	public GameState getGamestate() {
		return gamestate;
	}
	public void setGamestate(GameState gamestate) {
		this.gamestate = gamestate;
	}
	public GameThread getGameThread() {
		return gameThread;
	}
	public void setGameThread(GameThread gameThread) {
		this.gameThread = gameThread;
	}

	public static MainApplication getMain() {
		return main;
	}
	
	
	private void initialize() {
		loader = new FXMLLoader(getClass().getResource("Level.fxml"));
		try {
			root = (Pane)loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		window.setHeight(800);
		window.setWidth(800);
		levelScene = new Scene(root, 800, 800);
		contrLevel = loader.getController();
		window.setScene(levelScene);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		main = this;
		
		gameThread = new GameThread();
		gamestate = new GameState();
		
		initialize();
		
		window.show();
		
	}

	public static void main(String[] args) {    
	    launch(args);   
	 }
	
}
