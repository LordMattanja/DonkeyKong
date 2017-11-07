package objects;

import gui.LevelController;
import gui.MainApplication;
import javafx.scene.shape.Polygon;

public class Player extends MovingGameObject {

	private Double hPos, vPos;
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
	
	public Player(Double hPos, Double vPos) {
		this.hPos = hPos;
		this.vPos = vPos;
		this.polygon = new Polygon();
		polygon.getPoints().setAll(new Double[]{hPos, vPos, hPos, vPos+30, hPos+20, vPos+30, hPos+20, vPos,});
	}

	
	public void move() {
	    if(isPressedKeyLeft && !isPressedKeyRight && hPos >= 5.0){
	    	hPos -= 5.0;
	    } else if(!isPressedKeyLeft && isPressedKeyRight && hPos <= 750){
	    	hPos += 5.0;
	    }
		polygon.setTranslateX(hPos);
		System.out.println("moving");
	}

	@Override
	public Double gethPos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sethPos(Double hPos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Double getvPos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setvPos(Double vPos) {
		// TODO Auto-generated method stub
		
	}

}
