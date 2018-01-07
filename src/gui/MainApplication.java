package gui;

import java.io.IOException;

import gameLogic.GameThread;
import gameLogic.GameState;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import utils.Settings;

public class MainApplication extends Application{
	
	private Stage window;
	private Scene levelScene, menuScene;
	private FXMLLoader levelLoader, menuLoader;
	private Pane rootLevel, rootMenu;
	private LevelController contrLevel;
	private GameState gameState;
	private GameThread gameThread;
	private static MainApplication main;
	
	public Stage getWindow() {
		return window;
	}	
	
	public Scene getLevelScene() {
		return levelScene;
	}

	public LevelController getContrLevel() {
		return contrLevel;
	}
	public void setContrLevel(LevelController contrLevel) {
		this.contrLevel = contrLevel;
	}
	public GameState getGamestate() {
		return gameState;
	}
	public void setGamestate(GameState gamestate) {
		this.gameState = gamestate;
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
		levelLoader = new FXMLLoader(getClass().getResource("Level.fxml"));
		menuLoader = new FXMLLoader(getClass().getResource("Menu.fxml"));
		try {
			rootLevel = (Pane)levelLoader.load();
			rootMenu = (Pane)menuLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		window.setHeight(Settings.playerStartingPosY+125);
		window.setWidth(650);
		levelScene = new Scene(rootLevel, Settings.sceneWidth, Settings.sceneHeight);
		menuScene = new Scene(rootMenu, Settings.sceneWidth, Settings.sceneHeight);
		contrLevel = levelLoader.getController();
		window.setScene(menuScene);
	}

	@Override
	public synchronized void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		main = this;

		gameState = new GameState();
		
		initialize();
		
		window.show();
		
	}
	
	public void setMenuScene() {
		window.setScene(menuScene);
	}
	
	public void setLevelScene() {
		window.setScene(levelScene);
	}
	
	public synchronized void startAgain(boolean nextLevel) {
		gameState.initLevel();
		if(!nextLevel){
			window.setScene(levelScene);
			gameThread = new GameThread();
			gameThread.initGameThread();
		}
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				contrLevel.initGame();
				gameState.setControlsEnabled(true);
				gameState.setGameActive(true);
				if (!nextLevel) {
					gameThread.start();
				} else {
					gameThread.updatePlayer();
					gameThread.resumeThread();
				}
			}
		});		
	}

	public static void main(String[] args) {    
	    launch(args);   
	 }
	
}
