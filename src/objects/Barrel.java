package objects;

import Exceptions.CollisionException;
import gameLogic.GameState;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import utils.Settings;

public class Barrel extends MovingGameObject implements Runnable{
	
	private Double hPos, vPos;
	int translate;
	private boolean collision, rolling = false;
	private Polygon polygon;
	private GameState gameState;

	public Barrel(Double hPos, Double vPos, boolean collision, GameState gs) {
		gameState = gs;
		this.hPos = hPos;
		this.vPos = vPos;
		this.polygon = new Polygon();
		this.polygon.setFill(Color.AQUA);
		this.polygon.getPoints().setAll(new Double[]{ hPos, vPos, hPos+15, vPos, hPos + 15, vPos+15, hPos, vPos+15 });
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
			int translate = gameState.getCollidingPlatform(this.polygon).getTilt()/10;
			gripToPlatForm();
			throw new CollisionException(translate);		
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
		while (true) {
			try { 
				Thread.sleep(33);
				if(!rolling) {
					fall();					
				} else {
					roll();
				}				
				// TODO check if valid
			} catch (CollisionException e) {
				translate = e.getPlatformTilt();
				rolling = true;
				// TODO: handle exception
			} catch (InterruptedException e) {
				// TODO: handle exception
			}
		}
	}

}
