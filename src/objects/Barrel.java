package objects;

import Exceptions.CollisionException;
import gameLogic.GameState;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import utils.Settings;

public class Barrel extends MovingGameObject implements Runnable{
	
	private Double hPos, vPos;
	private boolean collision;
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

	private synchronized void fall() throws CollisionException{
		this.vPos += 2;
		this.polygon.setTranslateY(vPos-Settings.barrelStartingPosY);
		if(gameState.checkObjectCollision(this)){
			throw new CollisionException();
		}
	}
	
	private synchronized void roll() {
		int translate = gameState.getCollidingPlatform(this).getTilt()/10;
		this.hPos -= translate;
		if(translate != 0){
			this.vPos -= (2-translate/Settings.platformLength);
		} else {
			hPos += 1;
		}
		this.polygon.setTranslateX(hPos-Settings.barrelStartingPosX);
		this.polygon.setTranslateY(vPos-Settings.barrelStartingPosY);
	}

	@Override
	public synchronized void run() {
		while (true) {
			try {
				Thread.sleep(33);
				fall();
				// TODO check if valid
			} catch (CollisionException e) {
				roll();
				// TODO: handle exception
			} catch (InterruptedException e) {
				// TODO: handle exception
			}
		}
	}

}
