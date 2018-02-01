package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import general.Game;
import general.XMLFileManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class ScoreBoardController implements Initializable{

	//GridPane die alle Labels beinhaltet, die später die Informationen der Bestenliste darstellen
	@FXML
	private GridPane scorePane;
	//Labels die die GridPane bevölkern
	private Label[] playerNameLabels = new Label[10];
	private Label[] scoreLabels = new Label[10];
	private Label[] levelLabels = new Label[10];
	
	/*
	 * Methode wechselt zur MenuScene (aufgerufen durch einen Button)
	 */
	@FXML
	private void backToMenu() {
		MainApplication.getMain().setMenuScene();
	}
	
	/*
	 * Methode leert das Dokument das die Bestenliste beinhaltet und updated die Anzeige
	 */
	@FXML
	private void clear() {
		XMLFileManager.createNewFile();
		XMLFileManager.writeFile();
		XMLFileManager.updateDocument();
		updateScoreInfo();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//Initialisiert die Titel der Spalten der Tabelle
		scorePane.add(new Label("Rank"), 0, 0);
		scorePane.add(new Label("Player"), 1, 0);
		scorePane.add(new Label("Level"), 2, 0);
		scorePane.add(new Label("Score"), 3, 0);
		//fügt die Labels zur GridPane hinzu
		for(int i = 0; i < 10; i++) {
			scorePane.add(new Label(""+ (i+1)), 0, i+1);
			scorePane.add(playerNameLabels[i] = new Label(), 1, i+1);
			scorePane.add(levelLabels[i] = new Label(), 2, i+1);
			scorePane.add(scoreLabels[i] = new Label(), 3, i+1);
		}
	}
	
	/*
	 * Methode wird bei Anzeigen der Scene aufgerufen und aktualisiert die Anzeige der Bestenliste
	 */
	public void updateScoreInfo() {
		XMLFileManager.sortGames();
		ArrayList<Game> games = XMLFileManager.readDocument();
		for(int i = 0; i < 10; i++) {
			if (i < games.size()) {
				playerNameLabels[i].setText(games.get(i).getName());
				levelLabels[i].setText("" + games.get(i).getLevel());
				scoreLabels[i].setText("" + games.get(i).getScore());
			} else {
				playerNameLabels[i].setText("");
				levelLabels[i].setText("");
				scoreLabels[i].setText("");
			}
		}
	}
	
	

}
