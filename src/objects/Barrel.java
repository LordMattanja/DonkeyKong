package objects;

import Exceptions.CollisionException;
import gameLogic.GameState;
import javafx.scene.shape.Polygon;

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
		polygon.getPoints().setAll(new Double[]{ hPos, vPos, hPos+15, vPos, hPos + 15, vPos+15, hPos, vPos+15 });
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

	private void fall() throws CollisionException{
		vPos += 1;
		polygon.setTranslateY(vPos);
		if(gameState.checkObjectCollision(this)){
			System.out.println("collision");
			vPos -= 1;
			polygon.setTranslateY(vPos);
			throw new CollisionException();
		}
	}
	
	private void roll() {
		int translate = gameState.getCollidingPlatform().getTilt()/10;
		hPos += translate;
		vPos += 0.02;
		polygon.setTranslateX(hPos);
		polygon.setTranslateY(vPos);
	}

	@Override
	public synchronized void run() {
		try {
			fall();
			//TODO check if valid
			Thread.sleep(33);
		}catch (CollisionException e) {
			roll();
			// TODO: handle exception
		} catch (InterruptedException e) {
			// TODO: handle exception
		}
	}

}
