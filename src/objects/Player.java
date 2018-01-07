package objects;

import com.sun.xml.internal.messaging.saaj.soap.ver1_1.Header1_1Impl;

import gameLogic.GameState;
import gui.MainApplication;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import sun.applet.Main;
import utils.Settings;

public class Player extends MovingGameObject {

	private double hPos, vPos, vSpeed = 0.0, hSpeed = 0.0;
	private int health;
	private IntegerProperty healthProperty;
	private boolean collision, isPressedKeyRight = false, isPressedKeyLeft = false, isPressedKeyUp = false, isPressedKeyDown = false, grounded = false, isClimbing = false, canClimb = false;
	private Polygon polygon;
	private GameState gameState;

	public IntegerProperty getHealthProperty() {
		return healthProperty;
	}
	
	public double gethSpeed() {
		return hSpeed;
	}

	public void sethSpeed(double hSpeed) {
		this.hSpeed = hSpeed;
	}

	public boolean isPressedKeyRight() {
		return isPressedKeyRight;
	}

	public void setPressedKeyRight(boolean isPressedKeyRight) {
		this.isPressedKeyRight = isPressedKeyRight;
		if(!isPressedKeyRight && !isPressedKeyLeft) {
			hSpeed = 0.0;
		}
	}

	public boolean isPressedKeyLeft() {
		return isPressedKeyLeft;
	}

	public void setPressedKeyLeft(boolean isPressedKeyLeft) {
		this.isPressedKeyLeft = isPressedKeyLeft;
		if(!isPressedKeyLeft && !isPressedKeyRight) {
			hSpeed = 0.0;
		}
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


	public void setVSpeed(Double vSpeed) {
		this.vSpeed = vSpeed;
	}

	@Override
	public void setvPos(double vPos) {
		this.vPos = vPos;		
	}

	public double getvSpeed() {
		return vSpeed;
	}
	
	public Player(double hPosition, double vPosition, GameState gs) {
		gameState = gs;
		this.hPos = hPosition;
		this.vPos = vPosition;
		this.polygon = new Polygon();
		this.healthProperty = new SimpleIntegerProperty(3);
		polygon.setFill(Color.STEELBLUE);
		polygon.getPoints().setAll(new Double[]{hPos, vPos, hPos, vPos-30, hPos+20, vPos-30, hPos+20, vPos});
		System.out.println("New PLayer: , hPos : "+ hPos + " vPos: "+ vPos);
	}

//	private void resolveCollision(boolean vertical){
//		if(vertical) {
//			if(grounded) {
//				vPos -= vSpeed+.1;
//			} else {
//			if(vSpeed > 0 && vPos < 630){
//				vPos -= .1;
//		    	grounded = true;
//		    	vSpeed = 0.0;
//			} else if(vSpeed < 0 && vPos > 0){
//				vPos += .1;
//		    	vSpeed -= Settings.gravity;
//			}
//			}
//	    	polygon.setTranslateY(vPos-Settings.playerStartingPosY);
//		} else {
//			if(isPressedKeyLeft && !isPressedKeyRight && hPos >= 5.0){
//				hPos += .1;
//				if(gamestate.getCollidingPlatform(this.polygon).getTilt() < 0) {
//					vPos -= .05;
//					polygon.setTranslateY(vPos-Settings.playerStartingPosY);
//				}
//			} else if(!isPressedKeyLeft && isPressedKeyRight && hPos <= 700){
//		    	hPos -= .1;
//		    	if(gamestate.getCollidingPlatform(this.polygon).getTilt() > 0) {
//					vPos -= .05;
//					polygon.setTranslateY(vPos-Settings.playerStartingPosY);
//				}
//		    }
//			polygon.setTranslateX(hPos-Settings.playerStartingPosX);
//			if(gamestate.canClimb()){
//				return;
//			}
//		}
//		if(gamestate.checkObjectCollision(this)){
//			resolveCollision(vertical);
//		} 
//	}
	
	public void climb(){
		if(canClimb && isPressedKeyUp){
			vPos -= 3.0;
			vSpeed = 0.0;
			isClimbing = true;
		} else if(canClimb && isPressedKeyDown){
			vPos += 3.0;
			vSpeed = 0.0;
			isClimbing = true;
		}
		javafx.application.Platform.runLater(new Runnable() {

			@Override
			public void run() {
				 polygon.setTranslateY(vPos-Settings.playerStartingPosY);
			}
			 
		 });
	}
	
//	public void applyGravity(){
//		if(vSpeed == 0 && !canClimb){
//			polygon.setTranslateY(-Settings.gravity + vPos - Settings.playerStartingPosY);
//			if(!gamestate.checkObjectCollision(this)){
//				vSpeed += -Settings.gravity;
//			}
//			polygon.setTranslateY(Settings.gravity + vPos - Settings.playerStartingPosY);
//		}
//	}
//	
//	public  void move() {
//	    if(isPressedKeyLeft && !isPressedKeyRight && hPos >= 5.0){
//	    	hPos -= 5.0;
//	    } else if(!isPressedKeyLeft && isPressedKeyRight && hPos <= 700){
//	    	hPos += 5.0;
//	    }
//	    if(vSpeed != 0.0 && vPos+vSpeed < 630) {
//	    	if(grounded){
//	    		grounded = false;
//	    	}
//	    	vPos += vSpeed;
//	    	polygon.setTranslateY(vPos-Settings.playerStartingPosY);
//	    	if(gamestate.checkObjectCollision(this)){
//			  resolveCollision(true);
//			} else {
//	    	  vSpeed -= Settings.gravity;
//			}
//	    }
//		polygon.setTranslateX(hPos-Settings.playerStartingPosX);
//		if(gamestate.checkObjectCollision(this)){
//			resolveCollision(false);
//		}
////		System.out.println("x: " + hPos + ", y: " + vPos); 
//	}
	
	public void move() {
		grounded = checkIfGrounded();
		
		//if the player is not grounded -> fall
		 if(!grounded || vSpeed != 0) {
			 fall();
		 } 
		 
		 grounded = checkIfGrounded();
		//If the player presses the left key and is not on the left border
		 if(!isClimbing && isPressedKeyLeft && !isPressedKeyRight && hPos >= 5.0){
			  //the moveDistance is set to -5
		    	hSpeed = (grounded || hSpeed == -5.0)? -5.0 : -2.0;
		    } else if(!isClimbing && !isPressedKeyLeft && isPressedKeyRight && hPos <= Settings.tiltedPlatformLength){ //If the player presses the right key and is not on the left border
		      //the moveDistance is set to 5
		    	hSpeed = (grounded || hSpeed == 5.0)? 5.0 : 2.0;;
		    }	
		 //moveDistance is added to the hPos of the player
		 hPos += hSpeed;
		 //checks for a collision with a platform after moving right/left
		 checkAndResolveCollision(hSpeed, true);
		 
		 
		 if(!gameState.playerPlatformCollision()) {
			 javafx.application.Platform.runLater(new Runnable() {

				@Override
				public void run() {
					 polygon.setTranslateX(hPos-Settings.playerStartingPosX);
					 polygon.setTranslateY(vPos-Settings.playerStartingPosY);
				}
				 
			 });
		 } else {
			 System.err.println("ERROR: Cannot move player due to collision");
		 }
	}
	
	private void fall() {
		vSpeed += (vSpeed < 15)? 1.0 : 0;
		vPos += vSpeed;
		if(gameState.playerPlatformCollision()) {
			if(vSpeed > 0) {
				gripToPlatform();
				vSpeed = 0.0;
			} else {
				checkAndResolveCollision(vSpeed, false);
			}
		}
	}
	
	private void gripToPlatform() {
		while(gameState.playerPlatformCollision()) {
			vPos -= 0.1;
		}
	}
	
	private boolean checkIfGrounded() {
		vPos += .5;
		boolean grounded = gameState.playerPlatformCollision();
		vPos -= .5;
		return grounded;
	}
	
	private void checkAndResolveCollision(double moveDistance, boolean horizontal) {
		if(gameState.playerPlatformCollision() && !isClimbing) {
			Platform platform = gameState.getCollidingPlatform();
			if(horizontal) {
				hPos -= moveDistance;
				if(platform != null && platform.getTilt() != 0 && vPos-Settings.playerHeight < platform.getPolygon().getBoundsInParent().getMinY()) {
					vPos -= Math.abs(((double)platform.getTilt())/Settings.tiltedPlatformLength/2*moveDistance);
					hPos += moveDistance;
					checkAndResolveCollision(moveDistance, horizontal);
				}
			} else {
				vPos -= moveDistance;
			}
		} else if(isClimbing) {
			if(!horizontal) {
			Platform platform = gameState.getCollidingPlatform();
			Ladder ladder = gameState.getLadderPlayerIsUsing();
			for(Ladder platformLadder : platform.getLadders()) {
				if(ladder == platformLadder) {
					vPos -= moveDistance;
					break;
				}
			}
			}
		}
	}
	
	public void updatePlayerHealth() {
		javafx.application.Platform.runLater(new Runnable() {
			@Override
			public void run() {
				healthProperty.setValue(healthProperty.intValue()-1);
			}			
		});		
	}

//	@Override
//	public void run() {
//		while (healthProperty.intValue() > 0){
//			if(gamestate.playerBarrelCollision()){
//				healthProperty.setValue(healthProperty.intValue()-1);
//				System.out.println("Health : " + healthProperty.intValue());
//			}
//			try {
//				Thread.sleep(33);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		if(healthProperty.intValue() <= 0){
//			gamestate.endGame();
//		}
//	}

}
