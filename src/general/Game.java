package general;

public class Game {

	private String name;
	private int score, level;
	
	public String getName() {
		return name;
	}

	public int getScore() {
		return score;
	}

	public int getLevel() {
		return level;
	}

	public Game(String name, int score, int level) {
		this.name = name;
		this.score = score;
		this.level = level;
	}
	
}
