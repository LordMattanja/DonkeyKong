package objects;

import gameLogic.GameState;
import gui.LevelController;
import gui.MainApplication;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import utils.Settings;

public class Player extends MovingGameObject {

	private Double hPos, vPos, vSpeed = 2.0;
	private int health;
	private IntegerProperty healthProperty;
	public IntegerProperty getHealthProperty() {
		return healthProperty;
	}

	private boolean collision, isPressedKeyRight = false, isPressedKeyLeft = false, isPressedKeyUp = false, isPressedKeyDown = false, grounded = false, isClimbing = false, canClimb = false;
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

	public boolean isPressedKeyUp() {
		return isPressedKeyUp;
	}

	public void setPressedKeyUp(boolean isPressedKeyUp) {
		this.isPressedKeyUp = isPressedKeyUp;
	}

	public boolean isPressedKeyDown() {
		return isPressedKeyDown;
	}

	public void setPressedKeyDown(boolean isPressedKeyDown) {
		this.isPressedKeyDown = isPressedKeyDown;
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

	public boolean isClimbing() {
		return isClimbing;
	}

	public void setClimbing(boolean isClimbing) {
		this.isClimbing = isClimbing;
	}

	public boolean canClimb() {
		return canClimb;
	}
	
	public void setCanClimb(boolean canClimb){
		this.canClimb = canClimb; 
	}

	public Player(Double hPosition, Double vPosition, GameState gs) {
		gamestate = gs;
		this.hPos = hPosition;
		this.vPos = vPosition;
		this.polygon = new Polygon();
		this.healthProperty = new SimpleIntegerProperty(3);
		polygon.setFill(Color.CRIMSON);
		polygon.getPoints().setAll(new Double[]{hPos, vPos, hPos, vPos+30, hPos+20, vPos+30, hPos+20, vPos});
	}

	private void resolveCollision(boolean vertical){
		if(vertical) {
			if(grounded) {
				vPos -= vSpeed+.1;
			} else {
			if(vSpeed > 0 && vPos < 630){
				vPos -= .1;
		    	grounded = true;
		    	vSpeed = 0.0;
			} else if(vSpeed < 0 && vPos > 0){
				vPos += .1;
		    	vSpeed -= Settings.gravity;
			}
			}
	    	polygon.setTranslateY(vPos-Settings.playerStartingPosY);
		} else {
			if(isPressedKeyLeft && !isPressedKeyRight && hPos >= 5.0){
				hPos += .1;
				if(gamestate.getCollidingPlatform(this.polygon).getTilt() < 0) {
					vPos -= .05;
					polygon.setTranslateY(vPos-Settings.playerStartingPosY);
				}
			} else if(!isPressedKeyLeft && isPressedKeyRight && hPos <= 700){
		    	hPos -= .1;
		    	if(gamestate.getCollidingPlatform(this.polygon).getTilt() > 0) {
					vPos -= .05;
					polygon.setTranslateY(vPos-Settings.playerStartingPosY);
				}
		    }
			polygon.setTranslateX(hPos-Settings.playerStartingPosX);
			if(gamestate.canClimb()){
				return;
			}
		}
		if(gamestate.checkObjectCollision(this)){
			resolveCollision(vertical);
		} 
	}
	
	public void climb(){
		System.out.println("climbing");
		if(canClimb && isPressedKeyUp){
			vPos -= 3.0;
			vSpeed = 0.0;
			isClimbing = true;
		} else if( canClimb && isPressedKeyDown){
			vPos += 3.0;
			vSpeed = 0.0;
			isClimbing = true;
		}
		polygon.setTranslateY(vPos-Settings.playerStartingPosY);
	}
	
	public void applyGravity(){
		if(vSpeed == 0 && !canClimb){
			polygon.setTranslateY(-Settings.gravity + vPos - Settings.playerStartingPosY);
			if(!gamestate.checkObjectCollision(this)){
				vSpeed += -Settings.gravity;
			}
			polygon.setTranslateY(Settings.gravity + vPos - Settings.playerStartingPosY);
		}
	}
	
	public  void move() {
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
	    	if(gamestate.checkObjectCollision(this)){
				resolveCollision(true);
			} else {
	    	  vSpeed -= Settings.gravity;
			}
	    }
		polygon.setTranslateX(hPos-Settings.playerStartingPosX);
		if(gamestate.checkObjectCollision(this)){
			resolveCollision(false);
		}
//		System.out.println("x: " + hPos + ", y: " + vPos); 
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

	@Override
	public void run() {
		while (healthProperty.intValue() > 0){
			if(gamestate.playerBarrelCollision()){
				healthProperty.setValue(healthProperty.intValue()-1);
				System.out.println("Health : " + healthProperty.intValue());
			}
			try {
				Thread.sleep(33);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(healthProperty.intValue() <= 0){
			gamestate.endGame();
		}
	}

}
