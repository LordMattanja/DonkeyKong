package objects;

import gui.LevelController;
import gui.MainApplication;
import javafx.scene.shape.Polygon;
import utils.Settings;

public class Player extends MovingGameObject {

	private Double hPos, vPos, vSpeed = 0.0;
	private int health;
	private boolean collision, isPressedKeyRight = false, isPressedKeyLeft = false;
	private Polygon polygon;
	
	public boolean isPressedKeyRight() {
		return isPressedKeyRight;
	}

	public void setPressedKeyRight(boolean isPressedKeyRight) {
		this.isPressedKeyRight = isPressedKeyRight;
	}

	public boolean isPressedKeyLeft() {
		return isPressedKeyLeft;
	}

	public void setPressedKeyLeft(boolean isPressedKeyLeft) {
		this.isPressedKeyLeft = isPressedKeyLeft;
	}

	public Polygon getPolygon() {
		return this.polygon;
	}
	
	public Player(Double hPosition, Double vPosition) {
		this.hPos = hPosition;
		this.vPos = vPosition;
		this.polygon = new Polygon();
		polygon.getPoints().setAll(new Double[]{hPos, vPos, hPos, vPos+30, hPos+20, vPos+30, hPos+20, vPos});
		System.out.println(hPos);
	}

	
	public void move() {
	    if(isPressedKeyLeft && !isPressedKeyRight && hPos >= 5.0){
	    	hPos -= 5.0;
	    } else if(!isPressedKeyLeft && isPressedKeyRight && hPos <= 700){
	    	hPos += 5.0;
	    }
	    if(vSpeed != 0.0) {
	    	vPos += vSpeed;
	    	vSpeed -= Settings.gravity;
	    	polygon.setTranslateY(vPos-Settings.playerStartingPosY);
	    }
		polygon.setTranslateX(hPos-Settings.playerStartingPosX);
		System.out.println("moving");
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


	public void setVSpeed(Double vSpeed) {
		this.vSpeed = vSpeed;
	}

	@Override
	public void setvPos(Double vPos) {
		this.vPos = vPos;		
	}

	public Double getvSpeed() {
		return vSpeed;
	}

}
