package general;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;;

public class XMLFileManager {
	
	private static Document document;
	private static Element root;

	
	/**
	 * 
	 * @author jsobbe
	 */
	public static void createNewFile() {
		root = new Element("Games");
		document = new Document(root);
	}
	
	public static void addNewGame(Game game) {
		if(document == null) {
			updateDocument();
		}
		Element gameEle = new Element("game");
		gameEle.setAttribute("name", game.getName());
		gameEle.setAttribute("level", ""+game.getLevel());
		gameEle.setAttribute("score", ""+game.getScore());
		root.addContent(gameEle);
		writeFile();
	}
	
	public static ArrayList<Game> readDocument() {
		updateDocument();
		ArrayList<Game> gamesList = new ArrayList<Game>();
		if(document == null) {
			return gamesList;
		}
		List<Element> games = root.getChildren();
		Iterator<Element> iterator = games.iterator();
		while (iterator.hasNext()) {
			try {
				Element gameEle = iterator.next();
				Attribute attr = gameEle.getAttribute("score");
				int score = attr.getIntValue();
				attr = gameEle.getAttribute("level");
				int level = attr.getIntValue();
				attr = gameEle.getAttribute("name");
				String name = attr.getValue();
				Game game = new Game(name, score, level);gamesList.add(game);
			} catch (DataConversionException e) {
				e.printStackTrace();
				System.out.println();
			}			
		}
		return gamesList;
	}
	
	public static void writeFile() {
		if (document != null) {
			try {
				XMLOutputter out = new XMLOutputter();
				File f = new File("games.xml");
				FileWriter writer = new FileWriter(f);
				out.output(document, writer);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void updateDocument() {
		try {
			document = new SAXBuilder().build("games.xml");
			root = document.getRootElement();
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
			createNewFile();
		}
	}
	
	public static void sortGames() {
		ArrayList<Game> gamesList = readDocument();
		Game game;
		for(int i = 0; i < gamesList.size(); i++) {
			for(int j = gamesList.size()-1; j > i ; j--) {
				if(gamesList.get(i).getScore() < gamesList.get(j).getScore()) {
					game = gamesList.get(i);
					gamesList.set(i, gamesList.get(j));
					gamesList.set(j, game);
				}
			}
		}
		createNewFile();
		for(int i = 0; i < 10 && i < gamesList.size(); i++) {
			addNewGame(gamesList.get(i));
		}
		writeFile();
	}
	
	public static void main(String args[]) {
		createNewFile();
		addNewGame(new Game("The King", 10000, 3));
		addNewGame(new Game("Larry", 43, 1));
		addNewGame(new Game("Larry2", 4900, 2));
//		readFile();
		writeFile();
	}
	
}
