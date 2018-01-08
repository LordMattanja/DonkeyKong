package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import sun.font.CreatedFontTracker;
import utils.Game;
import utils.XMLFileWriter;

public class ScoreBoardController implements Initializable{

	
	@FXML
	private GridPane scorePane;
	
	private Label[] playerNameLabels = new Label[10];
	private Label[] scoreLabels = new Label[10];
	private Label[] levelLabels = new Label[10];
	
	@FXML 
	Label headerLabel;
	
	@FXML
	private void backToMenu() {
		MainApplication.getMain().setMenuScene();
	}
	
	@FXML
	private void clear() {
		System.out.println("clearing");
		XMLFileWriter.createNewFile();
		XMLFileWriter.writeFile();
		XMLFileWriter.updateDocument();
		updateScoreInfo();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		scorePane.add(new Label("Rank"), 0, 0);
		scorePane.add(new Label("Player"), 1, 0);
		scorePane.add(new Label("Level"), 2, 0);
		scorePane.add(new Label("Score"), 3, 0);
		for(int i = 0; i < 10; i++) {
			scorePane.add(new Label(""+ (i+1)), 0, i+1);
			scorePane.add(playerNameLabels[i] = new Label(), 1, i+1);
			scorePane.add(levelLabels[i] = new Label(), 2, i+1);
			scorePane.add(scoreLabels[i] = new Label(), 3, i+1);
		}
	}
	
	public void updateScoreInfo() {
		XMLFileWriter.sortGames();
		ArrayList<Game> games = XMLFileWriter.readFile();
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
