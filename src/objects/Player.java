package objects;

import gameLogic.GameState;
import gui.LevelController;
import gui.MainApplication;
import javafx.scene.shape.Polygon;
import utils.Settings;

public class Player extends MovingGameObject {

	private Double hPos, vPos, vSpeed = 2.0;
	private int health;
	private boolean collision, isPressedKeyRight = false, isPressedKeyLeft = false, grounded = false;
	private Polygon polygon;
	private GameState gamestate;
	
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
	
	public boolean isGrounded() {
		return grounded;
	}

	public void setGrounded(boolean grounded) {
		this.grounded = grounded;
	}

	public Player(Double hPosition, Double vPosition, GameState gs) {
		gamestate = gs;
		this.hPos = hPosition;
		this.vPos = vPosition;
		this.polygon = new Polygon();
		polygon.getPoints().setAll(new Double[]{hPos, vPos, hPos, vPos+30, hPos+20, vPos+30, hPos+20, vPos});
		System.out.println(hPos);
	}

	private void resolveCollision(boolean vertical){
		if(vertical) {
			if(grounded) {
				vPos += Settings.gravity;
			} else {
			if(vSpeed > 0 && vPos < 630){
				vPos -= .1;
		    	grounded = true;
			} else if(vSpeed < 0 && vPos > 0){
				vPos += .1;
		    	vSpeed -= Settings.gravity;
			}
			}
	    	polygon.setTranslateY(vPos-Settings.playerStartingPosY);
		} else {
			if(isPressedKeyLeft && !isPressedKeyRight && hPos >= 5.0){
				hPos += .1;
			} else if(!isPressedKeyLeft && isPressedKeyRight && hPos <= 700){
		    	hPos -= .1;
		    }
			polygon.setTranslateX(hPos-Settings.playerStartingPosX);
		}
		if(gamestate.checkPlayerCollision()){
			resolveCollision(vertical);
		} 
	}
	
	public void move() {
	    if(isPressedKeyLeft && !isPressedKeyRight && hPos >= 5.0){
	    	hPos -= 5.0;
	    } else if(!isPressedKeyLeft && isPressedKeyRight && hPos <= 700){
	    	hPos += 5.0;
	    }
	    if(vSpeed != 0.0 && vPos+vSpeed < 630) {
	    	if(grounded){
	    		grounded = false;
	    	}
	    	vPos += vSpeed;
	    	polygon.setTranslateY(vPos-Settings.playerStartingPosY);
	    	if(gamestate.checkPlayerCollision()){
				resolveCollision(true);
			} else {
	    	  vSpeed -= Settings.gravity;
			}
	    }
		polygon.setTranslateX(hPos-Settings.playerStartingPosX);
		if(gamestate.checkPlayerCollision()){
			resolveCollision(false);
		}
		System.out.println("x: " + hPos + ", y: " + vPos); 
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
