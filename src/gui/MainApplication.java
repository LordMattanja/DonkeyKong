package gui;

import java.io.IOException;
import game.GameState;
import game.GameThread;
import general.Settings;
import general.XMLFileManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainApplication extends Application{
	
	private Stage window;
	private Scene levelScene, menuScene, scoreScene;
	//FXMLLoader zum Laden der drei Scenes
	private FXMLLoader levelLoader, menuLoader, scoreLoader;
	//rootPanes für die Scenes
	private Pane rootLevel, rootMenu, rootScore;
	//Die einzelnen Controller für die Scenes
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
	
	/*
	 * Initialisiert die Scenes inklusive der dafür nötigen FXMLLoader, Panes und Controller
	 */
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
		window.setHeight(Settings.sceneHeight);
		window.setWidth(Settings.sceneWidth);
		levelScene = new Scene(rootLevel, Settings.sceneWidth, Settings.sceneHeight);
		menuScene = new Scene(rootMenu, Settings.sceneWidth, Settings.sceneHeight);
		scoreScene = new Scene(rootScore, Settings.sceneWidth, Settings.sceneHeight);
		contrLevel = levelLoader.getController();
		contrScore = scoreLoader.getController();
		contrMenu = menuLoader.getController();
		window.setScene(menuScene);
	}
	
	/*
	 * Sorgt dafür dass ein Level initialisiert wird und alle anderen Voraussetzungen zum Spielstart erfüllt sind
	 */
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
				} else {
					gameThread.resumeThread();
				}
			}
		});		
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
			if(gameThread != null) gameThread.pauseThread();
			XMLFileManager.writeFile();
			XMLFileManager.updateDocument();
		});		
	}

	public static void main(String[] args) {    
	    launch(args);   
	}
	
}
