package objects;

import Exceptions.CollisionException;
import gameLogic.GameState;
import gui.MainApplication;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.util.Duration;
import utils.Settings;

public class Barrel extends MovingGameObject{
	
	private double hPos, vPos;
	int translate;
	private boolean collision, rolling = false;
	private Polygon polygon;
	private GameState gameState;
	private PathTransition transition;
	private Image img = new Image(this.getClass().getResource("Smiley2.png").toExternalForm());

	public Barrel(double hPos, double vPos, boolean ingame, double size, int speed) {
		this.hPos = hPos;
		this.vPos = vPos;
		this.polygon = new Polygon();
		this.polygon.setFill(new ImagePattern(img));
		this.polygon.getPoints().setAll(new Double[]{ hPos, vPos, hPos+size, vPos, hPos + size, vPos-size, hPos, vPos-size });
		if(ingame) {
			MainApplication.getMain().getContrLevel().createBarrelPath(this, speed);
		}
	}


	@Override
	public Polygon getPolygon() {
		return polygon;
	}


	public Image getImg() {
		return img;
	}


	@Override
	public double gethPos() {
		return hPos;
	}


	@Override
	public void sethPos(double hPos) {
		this.hPos = hPos;
	}


	@Override
	public double getvPos() {
		return vPos;
	}


	@Override
	public void setvPos(double vPos) {
		this.vPos = vPos;
	}
	
	public void setTransition(PathTransition transition) {
		this.transition = transition;
	}


//	private void gripToPlatForm() {
//		while(gameState.checkPolygonCollision(this.polygon)) {
//			vPos -= .1;
//			polygon.setTranslateY(this.vPos-Settings.barrelStartingPosY);
//		}
//	}

//	private synchronized void fall() throws CollisionException {
//		this.vPos += 2;
//		this.polygon.setTranslateY(this.vPos-Settings.barrelStartingPosY);
//		if(gameState.checkPolygonCollision(this.polygon)){
//			Platform collidingPlatform = gameState.getCollidingPlatform(this.polygon);
//			if(collidingPlatform != null){
//			  int translate = collidingPlatform.getTilt()/10;
//			  gripToPlatForm();
//			  throw new CollisionException(translate);	
//			}
//		}
//	}

//	private synchronized void roll() {
//		if(hPos > 50 + Settings.tiltedPlatformLength && translate < 0 || hPos < 75 && translate > 0) {
//			rolling = false;
//			return;
//		}
//		this.hPos -= translate;
//		if(translate != 0){
//			this.vPos += (20.0/Settings.tiltedPlatformLength);
//			this.polygon.setTranslateY(vPos-Settings.barrelStartingPosY);
//		} else {
//			hPos += 1;
//		}
//		this.polygon.setTranslateX(hPos-Settings.barrelStartingPosX);
//	}

//	@Override
//	public synchronized void run() {
//		long threadId = Thread.currentThread().getId();
//    System.out.println("Thread # " + threadId + " is doing this task");
//		while (true) { //TODO 
//			try { 
//				Thread.sleep(33);
//				if(!rolling) {
//					fall();					
//				} else {
//					roll();
//				}	
//				// TODO check if valid
//			} catch (CollisionException e) {
//				translate = e.getPlatformTilt();
//				rolling = true;
//				// TODO: handle exception
//			} catch (InterruptedException e) {
//				// TODO: handle exception
//			}
//		}
//	}

}