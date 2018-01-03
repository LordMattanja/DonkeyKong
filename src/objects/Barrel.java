package objects;

import Exceptions.CollisionException;
import gameLogic.GameState;
import gui.MainApplication;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.util.Duration;
import utils.Settings;

public class Barrel extends MovingGameObject implements Runnable{
	
	private Double hPos, vPos;
	int translate;
	private boolean collision, rolling = false;
	private Polygon polygon;
	private GameState gameState;
	private PathTransition transition;

	public Barrel(Double hPos, Double vPos, boolean collision, GameState gs) {
		gameState = gs;
		this.hPos = hPos;
		this.vPos = vPos;
		this.polygon = new Polygon();
		this.polygon.setFill(Color.AQUA);
		this.polygon.getPoints().setAll(new Double[]{ hPos, vPos, hPos+15, vPos, hPos + 15, vPos+15, hPos, vPos+15 });
		this.transition = new PathTransition();
		Polyline path = new Polyline();
		Double verticalValue = 35.0;
		Double horizontalValue = 10.0;
		for(int i = 0; i < Settings.numberOfPlatforms*2; i++){
			Double[] array = new Double[] {
					horizontalValue, verticalValue, 
			};			
			if(i%4 == 0) {
				horizontalValue += Settings.tiltedPlatformLength;
				verticalValue += 20;
			} else if(i%2 != 0) {
				verticalValue += 600/Settings.numberOfPlatforms-20;
			} else if(i%2 == 0) {
				horizontalValue -= Settings.tiltedPlatformLength;
				verticalValue += 20;
			}
			path.getPoints().addAll(array);
		}
		verticalValue = Settings.playerStartingPosY+15;
		path.getPoints().addAll(new Double[] {
				horizontalValue, verticalValue, 
				horizontalValue+Settings.tiltedPlatformLength, verticalValue
		});
		transition.setCycleCount(1);
		transition.setDuration(Duration.seconds(35));
		transition.setInterpolator(Interpolator.EASE_OUT);
		transition.setNode(polygon);
		transition.setPath(path);
		Barrel barrel = this;
		transition.setOnFinished(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				MainApplication.getMain().getContrLevel().removeObject(barrel);
			}
		});
		transition.play();
	}


	@Override
	public Polygon getPolygon() {
		return polygon;
	}


	@Override
	public Double gethPos() {
		return hPos;
	}


	@Override
	public void sethPos(Double hPos) {
		this.hPos = hPos;
	}


	@Override
	public Double getvPos() {
		return vPos;
	}


	@Override
	public void setvPos(Double vPos) {
		this.vPos = vPos;
	}
	
	private void gripToPlatForm() {
		while(gameState.checkPolygonCollision(this.polygon)) {
			vPos -= .1;
			polygon.setTranslateY(this.vPos-Settings.barrelStartingPosY);
		}
	}

	private synchronized void fall() throws CollisionException {
		this.vPos += 2;
		this.polygon.setTranslateY(this.vPos-Settings.barrelStartingPosY);
		if(gameState.checkPolygonCollision(this.polygon)){
			Platform collidingPlatform = gameState.getCollidingPlatform(this.polygon);
			if(collidingPlatform != null){
			  int translate = collidingPlatform.getTilt()/10;
			  gripToPlatForm();
			  throw new CollisionException(translate);	
			}
		}
	}

	private synchronized void roll() {
		if(hPos > 50 + Settings.tiltedPlatformLength && translate < 0 || hPos < 75 && translate > 0) {
			rolling = false;
			return;
		}
		this.hPos -= translate;
		if(translate != 0){
			this.vPos += (20.0/Settings.tiltedPlatformLength);
			this.polygon.setTranslateY(vPos-Settings.barrelStartingPosY);
		} else {
			hPos += 1;
		}
		this.polygon.setTranslateX(hPos-Settings.barrelStartingPosX);
	}

	@Override
	public synchronized void run() {
		long threadId = Thread.currentThread().getId();
    System.out.println("Thread # " + threadId + " is doing this task");
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
	}

}