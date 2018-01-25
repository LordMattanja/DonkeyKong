package objects;

import java.util.Random;

import game.GameState;
import gui.MainApplication;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import utils.ImageLoader;
import utils.Settings;

public class Platform extends GameObject{
	
	private double hPos, vPos, height, width;
	private int tilt;
	private Polygon polygon;
	private Ladder[] ladders;
	private Image img = ImageLoader.getPlatformImage();

	public int getTilt() {
		return tilt;
	}
	
	@Override
	public Shape getShape() {
		return polygon;
	}
	
	@Override
	public double gethPos() {
		return hPos;
	}


	@Override
	public double getvPos() {
		return vPos;
	}


	@Override
	public double getHeight() {
		return height;
	}

	public Ladder[] getLadders() {
		return ladders;
	}
	
	@Override
	public void sethPos(double hPos) {
		this.hPos = hPos;
	}

	@Override
	public void setvPos(double vPos) {
		this.vPos = vPos;
	}

	@Override
	public double getWidth() {
		return width;
	}

	public Platform(double hPos, double vPos, int length, boolean collision, int tilt, int numOfLadders) {
		this.vPos = vPos;
		this.hPos = hPos;
		this.height = Settings.platformHeight;
		this.width = length;
		this.tilt = tilt;
		polygon = new Polygon();
		ImagePattern pattern = (tilt != 0)?new ImagePattern(img, 0, 0, .2, 2, true):new ImagePattern(img, 0, 0, .2, 3.5, true);
		polygon.getStyleClass().add("polygon");
		polygon.setFill(pattern);
		polygon.getPoints().setAll(new Double[]{hPos, vPos+tilt, hPos+width, vPos-tilt, hPos+width, vPos+23-tilt, hPos, vPos+23+tilt});
		int hPosLadder = 25+(int)hPos;
		ladders = new Ladder[numOfLadders];
		for(int i = 0; i < numOfLadders; i++) {
			hPosLadder = (MainApplication.getMain().getGamestate().getPlatforms().size() == 4)? 550 : calcLadderHPos(tilt, numOfLadders, i);
			double verticalShift = calcLadderVPos(hPosLadder);
			ladders[i] = new Ladder(hPosLadder/25*25, verticalShift + vPos, 500.0/Settings.numberOfPlatforms+-2*verticalShift);
		}
	}
	
	private int calcLadderHPos (int tilt, int numOfLadders, int ladderID) {
		Random rand = new Random();
		int hPosLadder = 0;
		boolean correctPositionfound = false;
		while (!correctPositionfound){
			correctPositionfound = true;
			hPosLadder = (int) ((tilt != 0)? rand.nextInt((int)width-50)/(numOfLadders)+ladderID*(width-50)/(numOfLadders)+25 : (int)Settings.playerStartingPosX);
			if (MainApplication.getMain().getGamestate().getPlatforms().size() > 0) {
				for(Ladder ladderAbove : MainApplication.getMain().getGamestate().getPlatforms().get(MainApplication.getMain().getGamestate().getPlatforms().size()-1).getLadders()) {
					if(hPosLadder/25*25 == ladderAbove.gethPos()) {
					System.out.println("samepos");
					correctPositionfound = false;
					break;
					}
				}
			}
			if((ladderID != 0) && (hPosLadder - ladders[ladderID-1].gethPos() > width))	correctPositionfound = false;	
		}
		return hPosLadder;
	}
	
	private double calcLadderVPos(double hPosLadder) {
		double vPosLadder = hPosLadder-hPos-Settings.tiltedPlatformLength/2;
		vPosLadder = vPosLadder/(Settings.tiltedPlatformLength/2)*tilt;
		return (tilt != 0)? vPosLadder*-1 : 0;
	}



}
