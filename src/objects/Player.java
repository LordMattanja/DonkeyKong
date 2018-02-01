package objects;

import game.GameState;
import general.ImageLoader;
import general.Settings;
import gui.MainApplication;
import javafx.animation.ScaleTransition;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Player extends GameObject {

	//Attribute für die Geschwindigkeit mit der sich der Spieler gerade bewegt
	private double vSpeed = 0.0, hSpeed = 0.0;
	//Lebenspunkte des Spielers
	private int health = 3;
	//Booleans für die Steuerung und Bewegung
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
	
	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	/*
	 * Initialisiert den Spieler mit Position, Shape und übergibt den GameState, damit der Spieler leichteren Zugriff auf Methoden hat 
	 */
	public Player(double hPosition, double vPosition, GameState gs) {
		super(hPosition, vPosition, 25, 40, new Rectangle(hPosition, vPosition-30, 25, 30), ImageLoader.getPlayerImage());
		gameState = gs;
		shape.setFill(new ImagePattern(img));
	}
	
	/*
	 * Lässt den Spieler in die gewünschte Richtung klettern (durch verändern der Position und verschieben des Shapes) und hält ihn an der Leiter
	 */
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
	
	/*
	 * Verschiebt den Spieler genau an die Leiter
	 */
	private void gripToLadder() {
		Ladder ladder = gameState.getUsedLadder(this);
		if(ladder != null) {
			hPos = ladder.gethPos();
		}
		movePlayerPolygon();
	}
	
	/*
	 * Die Methode bewegt den Spieler:
	 * Sowohl laufen/horizontale Bewegungen als auch fallen werden hier koordiniert
	 */
	public void move() {
		grounded = checkIfGrounded();
		//wenn nicht grounded -> fall()
		 if(!grounded || vSpeed != 0) {
			 fall();
		 } 
		 
		 grounded = checkIfGrounded();
		 //wenn der Spieler nach rechts drückt
		 if(!isClimbing && isPressedKeyLeft && !isPressedKeyRight){
		      //wird die horizontale Geschwindigkeit auf -5 am Boden und -2 in der Luft gesetzt
		    	hSpeed = (grounded || hSpeed == -5.0)? -5.0 : -2.0;
		    } else if(!isClimbing && !isPressedKeyLeft && isPressedKeyRight){ //wenn der Spieler nach rechts drückt
		      //wird die horizontale Geschwindigkeit auf 5 am Boden und 2 in der Luft gesetzt
		    	hSpeed = (grounded || hSpeed == 5.0)? 5.0 : 2.0;;
		    }
		 //hSpeed wird zur Position hinzugefügt
		 if(!(hPos <= 0.0 && hSpeed < 0 || hPos > Settings.tiltedPlatformLength && hSpeed > 0)) hPos += hSpeed;
		 //überprüft und verarbeitet potentielle Kollision
		 checkAndResolveCollision(hSpeed, true);
		 //verschiebt den Shape
		 movePlayerPolygon();		 
	}
	
	/*
	 * Verschiebt den Shape an die aktuelle Position
	 */
	private synchronized void movePlayerPolygon() {
		if(!gameState.playerPlatformCollision()) {
			 javafx.application.Platform.runLater(new Runnable() {

				@Override
				public void run() {
					 shape.setTranslateX(hPos-Settings.playerStartingPosX);
					 shape.setTranslateY(vPos-Settings.playerStartingPosY);
				}
				 
			 });
		 } 
	}
	
	/*
	 * Lässt den Spieler fallen, passt die Fallgeschwindigkeit an und prüft Kollisionen
	 */
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
	
	/*
	 * Verschiebt den Spieler bei einer Kollision mit einer Plattform gerade soweit nach oben, dass er auf der Plattform steht
	 */
	private void gripToPlatform() {
		while(gameState.playerPlatformCollision()) {
			vPos -= 0.1;
		}
	}
	
	/*
	 * Überprüft ob sich der Spieler gerade auf einer Plattform befindet
	 */
	private boolean checkIfGrounded() {
		vPos += .5;
		boolean grounded = gameState.playerPlatformCollision();
		vPos -= .5;
		return grounded;
	}
	
	/*
	 * Lässt den Spieler die Leiter loslassen
	 */
	public void stopClimbing() {
		isClimbing = false;
		if(gameState.playerPlatformCollision()) {
			isClimbing = true;
		}
	}
	
	/*
	 * Schrumpft den Spieler, dass es so aussieht als ob er im Ziel verschwindet, setzt das danach zurück und beendet das Level im GameThread
	 */
	public void disappear() {
		ScaleTransition disappear = new ScaleTransition(Duration.seconds(1.5), shape);
		disappear.setToX(0.01);
		disappear.setToY(0.01);
		disappear.setOnFinished(e -> {
			MainApplication.getMain().getGameThread().endLevel();
			shape.setScaleX(1);
			shape.setScaleY(1);
			hPos = Settings.playerStartingPosX;
			vPos = Settings.playerStartingPosY;
			movePlayerPolygon();
		});	
		disappear.play();
		MainApplication.getMain().getGameThread().pauseThread();
	}
	
	/*
	 * Bekommt als Parameter die Distanz und die Information ob es eine horizontale oder vertikale Kollision war und löst die Kollision dementsprechend
	 */
	private void checkAndResolveCollision(double moveDistance, boolean horizontal) {
		if(gameState.playerPlatformCollision() && !isClimbing) {
			Platform platform = gameState.getCurrentlyUsedPlatform(this);
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
				Platform platform = gameState.getCurrentlyUsedPlatform(this);
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
	
	/*
	 * Die Methode wechselt zwischen den beiden laufenden Bildern des Spielers, sodass im Ansatz eine Laufanimation entsteht
	 */
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
