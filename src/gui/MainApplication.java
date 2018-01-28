package gui;

import java.io.IOException;
import game.GameState;
import game.GameThread;
import general.Settings;
import general.XMLFileWriter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainApplication extends Application{
	
	private Stage window;
	private Scene levelScene, menuScene, scoreScene;
	private FXMLLoader levelLoader, menuLoader, scoreLoader;
	private Pane rootLevel, rootMenu, rootScore;
	private LevelController contrLevel;
	private ScoreBoardController contrScore;
	private MenuController contrMenu;
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
	
	public ScoreBoardController getContrScore() {
		return contrScore;
	}

	public MenuController getContrMenu() {
		return contrMenu;
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

	public void setMenuScene() {
		window.setScene(menuScene);
	}
	
	public void setLevelScene() {
		window.setScene(levelScene);
	}
	
	public void setScoreScene() {
		window.setScene(scoreScene);
	}
	
	
	private void initialize() {
		levelLoader = new FXMLLoader(getClass().getResource("Level.fxml"));
		menuLoader = new FXMLLoader(getClass().getResource("Menu.fxml"));
		scoreLoader = new FXMLLoader(getClass().getResource("ScoreBoard.fxml"));
		try {
			rootLevel = (Pane)levelLoader.load();
			rootLevel.getStylesheets().add(getClass().getResource("Level.css").toExternalForm());
			rootMenu = (Pane)menuLoader.load();
			rootMenu.getStylesheets().add(getClass().getResource("Menu.css").toExternalForm());
			rootScore = (Pane)scoreLoader.load();
			rootScore.getStylesheets().add(getClass().getResource("Menu.css").toExternalForm());
		} catch (IOException e) {
			e.printStackTrace();
		}
		window.setHeight(Settings.playerStartingPosY+100);
		window.setWidth(635);
		levelScene = new Scene(rootLevel, Settings.sceneWidth, Settings.sceneHeight);
		menuScene = new Scene(rootMenu, Settings.sceneWidth, Settings.sceneHeight);
		scoreScene = new Scene(rootScore, Settings.sceneWidth, Settings.sceneHeight);
		contrLevel = levelLoader.getController();
		contrScore = scoreLoader.getController();
		contrMenu = menuLoader.getController();
		window.setScene(menuScene);
	}

	@Override
	public synchronized void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		window.setResizable(false);
		main = this;

		gameState = new GameState();
		
		initialize();
		
		window.show();
		window.setOnCloseRequest(e -> {
			XMLFileWriter.writeFile();
			XMLFileWriter.updateDocument();
		});		
	}
	
	public synchronized void startGame(boolean nextLevel) {
		gameState.initLevel();
		if(!nextLevel){
			window.setScene(levelScene);
			gameThread = new GameThread();
		}
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				contrLevel.initGame();
				gameState.setGameActive(true);
				if (!nextLevel) {
					gameThread.start();
					gameState.getHealthProperty().setValue(3);
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
