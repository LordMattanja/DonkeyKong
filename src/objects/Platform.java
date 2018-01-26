package objects;

import java.util.Random;
import gui.MainApplication;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import utils.ImageLoader;
import utils.Settings;

public class Platform extends GameObject{
	
	private int tilt;
	private Polygon polygon;
	private Ladder[] ladders;

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
	
	public Platform(double hPos, double vPos, int length, int tilt, int numOfLadders) {
		super(hPos, vPos, length, 23, new Polygon(), ImageLoader.getPlatformImage());
		this.tilt = tilt;		
		ImagePattern pattern = (tilt != 0)?new ImagePattern(getImage(), 0, 0, .2, 2, true):new ImagePattern(getImage(), 0, 0, .2, 3.5, true);
		polygon = new Polygon();
		polygon.getStyleClass().add("polygon");
		polygon.setFill(pattern);
		polygon.getPoints().setAll(new Double[]{hPos, vPos+tilt, hPos+length, vPos-tilt, hPos+length, vPos+23-tilt, hPos, vPos+23+tilt});
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
			hPosLadder = (int) ((tilt != 0)? rand.nextInt((int)getWidth()-50)/(numOfLadders)+ladderID*(getWidth()-50)/(numOfLadders)+25 : (int)Settings.playerStartingPosX);
			if (MainApplication.getMain().getGamestate().getPlatforms().size() > 0) {
				for(Ladder ladderAbove : MainApplication.getMain().getGamestate().getPlatforms().get(MainApplication.getMain().getGamestate().getPlatforms().size()-1).getLadders()) {
					if(hPosLadder/25*25 == ladderAbove.gethPos()) {
					correctPositionfound = false;
					break;
					}
				}
			}
			if((ladderID != 0) && (hPosLadder - ladders[ladderID-1].gethPos() > getWidth()))	correctPositionfound = false;	
		}
		return hPosLadder;
	}
	
	private double calcLadderVPos(double hPosLadder) {
		double vPosLadder = hPosLadder-gethPos()-Settings.tiltedPlatformLength/2;
		vPosLadder = vPosLadder/(Settings.tiltedPlatformLength/2)*tilt;
		return (tilt != 0)? vPosLadder*-1 : 0;
	}



}
