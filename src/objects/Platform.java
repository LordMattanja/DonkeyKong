package objects;

import java.util.Random;

import gui.MainApplication;
import javafx.geometry.HPos;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import sun.applet.Main;
import utils.ImageLoader;
import utils.Settings;

public class Platform extends StaticGameObject{
	
	private double hPos, vPos, height;
	private int tilt;
	private boolean collision;
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

	public Ladder[] getLadders() {
		return ladders;
	}

	public Platform(double hPos, double vPos, int length, boolean collision, int tilt, int numOfLadders) {
		this.vPos = vPos;
		this.hPos = hPos;
		this.height = Settings.platformHeight;
		polygon = new Polygon();
		ImagePattern pattern = (tilt != 0)?new ImagePattern(img, 0, 0, .2, 2, true):new ImagePattern(img, 0, 0, .2, 3.5, true);
		polygon.getStyleClass().add("polygon");
		polygon.setFill(pattern);
		this.tilt = tilt;
		polygon.getPoints().setAll(new Double[]{hPos, vPos+tilt, hPos+length, vPos-tilt, hPos+length, vPos+23-tilt, hPos, vPos+23+tilt});
		Random rand = new Random();
		double hPosLadder = 25.0+hPos;
		ladders = new Ladder[numOfLadders];
		for(int i = 0; i < numOfLadders; i++) {
			hPosLadder = (tilt != 0)? rand.nextDouble()*(length-50)/(numOfLadders)+i*(length-50)/(numOfLadders) : Settings.playerStartingPosX-2;
			double verticalShift = calcLadderVPos(hPosLadder);
			ladders[i] = new Ladder(hPosLadder, verticalShift + vPos, 500.0/Settings.numberOfPlatforms+-2*verticalShift);
		}
	}
	
	private double calcLadderVPos(double hPosLadder) {
		double vPosLadder = hPosLadder-hPos-Settings.tiltedPlatformLength/2;
		vPosLadder = vPosLadder/(Settings.tiltedPlatformLength/2)*tilt;
		return (tilt != 0)? vPosLadder*-1 : 0;
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


}
