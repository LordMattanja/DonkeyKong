package utils;

import javafx.scene.image.Image;

public class ImageLoader {

	private static Image barrelImg;
	private static Image ladderImg;
	private static Image platformImg;
	
	public static Image getBarrelImage() {
		if(barrelImg == null) {
			String url = ImageLoader.class.getResource("resources/barrel.png").toString();
			barrelImg = new Image(url);
		}
		return barrelImg;
	}
	
	public static Image getLadderImage() {
		if(ladderImg == null) {
			String url = ImageLoader.class.getResource("resources/ladder.png").toString();
			ladderImg = new Image(url);
		}
		return ladderImg;
	}
	
	public static Image getPlatformImage() {
		if(platformImg == null) {
			String url = ImageLoader.class.getResource("resources/platformwall.png").toString();
			platformImg = new Image(url);
		}
		return platformImg;
	}
	
}
