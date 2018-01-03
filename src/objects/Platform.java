package objects;

import java.util.Random;

import gui.MainApplication;
import javafx.geometry.HPos;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import sun.applet.Main;
import utils.Settings;

public class Platform extends StaticGameObject{
	
	private Double hPos, vPos;
	private int tilt;
	private boolean collision;
	private Polygon polygon;
	private Ladder[] ladders;

	public int getTilt() {
		return tilt;
	}
	
	public Polygon getPolygon() {
		return polygon;
	}

	public Ladder[] getLadders() {
		return ladders;
	}

	public Platform(Double hPos, Double vPos, int length, boolean collision, int tilt, int numOfLadders) {
		super(hPos, vPos, collision);
		this.vPos = vPos;
		this.hPos = hPos;
		polygon = new Polygon();
		polygon.setFill(Color.BURLYWOOD);
		this.tilt = tilt;
		polygon.getPoints().setAll(new Double[]{hPos, vPos+tilt, hPos+length, vPos-tilt, hPos+length, vPos+15-tilt, hPos, vPos+15+tilt});
		Random rand = new Random();
		Double hPosLadder = 25.0+hPos;
		ladders = new Ladder[numOfLadders];
		for(int i = 0; i < numOfLadders; i++) {
			hPosLadder = (tilt != 0)? rand.nextDouble()*(Settings.tiltedPlatformLength-50)/(numOfLadders-i)+hPosLadder : Settings.playerStartingPosX;
			ladders[i] = new Ladder(hPosLadder, calcLadderVPos(hPosLadder), 600.0/Settings.numberOfPlatforms);
		}
	}
	
	private double calcLadderVPos(double hPosLadder) {
		double vPosLadder = hPosLadder-hPos-Settings.tiltedPlatformLength/2;
		vPosLadder = vPosLadder/(Settings.tiltedPlatformLength/2)*tilt;
		if(hPosLadder < hPos + Settings.tiltedPlatformLength/2) {
			return vPosLadder*-1 + vPos + tilt;
		}
		return vPosLadder + vPos - tilt;
	}

}
