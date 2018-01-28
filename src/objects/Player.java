package objects;

import game.GameState;
import general.ImageLoader;
import general.Settings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Player extends GameObject {

	private double vSpeed = 0.0, hSpeed = 0.0;
	private boolean isPressedKeyRight = false, isPressedKeyLeft = false, isPressedKeyUp = false, isPressedKeyDown = false, grounded = true, isClimbing = false, canClimb = false;
	private GameState gameState;
	
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
		if(isPressedKeyRight == isPressedKeyLeft) {
			hSpeed = 0.0;
		}
	}

	public boolean isPressedKeyLeft() {
		return isPressedKeyLeft;
	}

	public void setPressedKeyLeft(boolean isPressedKeyLeft) {
		this.isPressedKeyLeft = isPressedKeyLeft;
		if(isPressedKeyLeft == isPressedKeyRight) {
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

	public void setVSpeed(Double vSpeed) {
		this.vSpeed = vSpeed;
	}


	public double getvSpeed() {
		return vSpeed;
	}
	
	public Player(double hPosition, double vPosition, GameState gs) {
		super(hPosition, vPosition, 25, 40, new Rectangle(hPosition, vPosition-30, 25, 30), ImageLoader.getPlayerImage());
		gameState = gs;
		shape.setFill(new ImagePattern(img));
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
		canClimb = gameState.canClimb();
		if(canClimb && isPressedKeyUp && !isPressedKeyDown){
			vPos -= 3.0;
			vSpeed = 0.0;
			isClimbing = true;
			gripToLadder();
		} else if(canClimb && isPressedKeyDown && !isPressedKeyUp){
			vPos += 3.0;
			vSpeed = 0.0;
			isClimbing = true;
			gripToLadder();
		}
		javafx.application.Platform.runLater(new Runnable() {

			@Override
			public void run() {
				 shape.setTranslateY(vPos-Settings.playerStartingPosY);
			}
			 
		 });
	}
	
	private void gripToLadder() {
		Ladder ladder = gameState.getUsedLadder(this);
		if(ladder != null) {
			hPos = ladder.gethPos();
		}
		movePlayerPolygon();
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
		 if(!isClimbing && isPressedKeyLeft && !isPressedKeyRight){
			  //the moveDistance is set to -5
			 System.out.println(hPos);
		    	hSpeed = (grounded || hSpeed == -5.0)? -5.0 : -2.0;
		    } else if(!isClimbing && !isPressedKeyLeft && isPressedKeyRight){ //If the player presses the right key and is not on the right border
		      //the moveDistance is set to 5
		    	hSpeed = (grounded || hSpeed == 5.0)? 5.0 : 2.0;;
		    }
		 //moveDistance is added to the hPos of the player
		 if(!(hPos <= 0.0 && hSpeed < 0 || hPos >= Settings.platformLength-width-5 && hSpeed > 0)) hPos += hSpeed;
		 //checks for a collision with a platform after moving right/left
		 checkAndResolveCollision(hSpeed, true);
		 
		 movePlayerPolygon();		 
	}
	
	private synchronized void movePlayerPolygon() {
		if(!gameState.playerPlatformCollision()) {
			 javafx.application.Platform.runLater(new Runnable() {

				@Override
				public void run() {
					 shape.setTranslateX(hPos-Settings.playerStartingPosX);
					 shape.setTranslateY(vPos-Settings.playerStartingPosY);
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
				if(platform != null && platform.getTilt() != 0 && vPos-Settings.playerHeight < platform.getShape().getBoundsInParent().getMinY()) {
					vPos -= Math.abs(((double)platform.getTilt())/Settings.tiltedPlatformLength/2*moveDistance);
					hPos += moveDistance;
					checkAndResolveCollision(moveDistance, horizontal);
				}
			} else {
				vPos -= moveDistance;
			}
		} else if(isClimbing) {
			if (!horizontal) {
				Platform platform = gameState.getCollidingPlatform();
				Ladder ladder = gameState.getUsedLadder(this);
				for (Ladder platformLadder : platform.getLadders()) {
					if (ladder == platformLadder) {
						vPos -= moveDistance;
						break;
					}
				}
			} else {
				if(!gameState.canClimb()) {
					hPos -= moveDistance;
					isClimbing = true;
				}
			}
		}
	}	

	public void switchPlayerImg(boolean walking) {
		if(!walking) {
			 img = ImageLoader.getPlayerImage();
		} else if(img != ImageLoader.getPlayerWalkingImage()[0]) {
			 img = ImageLoader.getPlayerWalkingImage()[0];
		 } else {
			 img = ImageLoader.getPlayerWalkingImage()[1];
		 } 
		 shape.setFill(new ImagePattern(img));
	}
	

}
