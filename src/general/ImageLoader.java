package general;

import javafx.scene.image.Image;

public class ImageLoader {

	private static Image barrelImg;
	private static Image ladderImg;
	private static Image platformImg;
	private static Image playerImg;
	private static Image[] playerWalking;
	
	/*
	 * Lädt das Bild für das Fass
	 */
	public static Image getBarrelImage() {
		if(barrelImg == null) {
			String url = ImageLoader.class.getResource("resources/barrel.png").toString();
			barrelImg = new Image(url);
		}
		return barrelImg;
	}
	
	/*
	 * Lädt das Bild der Leiter
	 */
	public static Image getLadderImage() {
		if(ladderImg == null) {
			String url = ImageLoader.class.getResource("resources/ladder.png").toString();
			ladderImg = new Image(url);
		}
		return ladderImg;
	}
	
	/*
	 * Lädt das Bild der Plattform
	 */
	public static Image getPlatformImage() {
		if(platformImg == null) {
			String url = ImageLoader.class.getResource("resources/platformwall.png").toString();
			platformImg = new Image(url);
		}
		return platformImg;
	}
	
	/*
	 * Lädt das Bild des Spielers
	 */
	public static Image getPlayerImage() {
		if(playerImg == null) {
			String url = ImageLoader.class.getResource("resources/kplayer.png").toString();
			playerImg = new Image(url);
		}
		return playerImg;
	}
	
	/*
	 * Lädt die Bilder des Spielers bei Bewegung
	 */
	public static Image[] getPlayerWalkingImage() {
		if(playerWalking == null) {
			playerWalking = new Image[2];
			playerWalking[0] = new Image(ImageLoader.class.getResource("resources/kplayerWalking.png").toString());
			playerWalking[1] = new Image(ImageLoader.class.getResource("resources/kplayerWalking2.png").toString());
		}
		return playerWalking;
	}
	
}
