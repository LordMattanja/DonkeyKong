package objects;

import javafx.scene.shape.Polygon;

public class Player extends MovingGameObject {

	private Double vSpeed = .0, hSpeed = .0; 
	private Double hPos, vPos;
	private int health;
	private boolean collision;
	private Polygon polygon;
	
	public Polygon getPolygon() {
		return this.polygon;
	}
	
	public Double gethSpeed() {
		return hSpeed;
	}

	public void sethSpeed(Double hSpeed) {
		this.hSpeed = hSpeed;
	}

	public Player(Double hPos, Double vPos) {
		super(hPos, vPos, true);
		this.hPos = hPos;
		this.vPos = vPos;
		this.polygon = new Polygon();
		polygon.getPoints().setAll(new Double[]{hPos, vPos, hPos, vPos+30, hPos+20, vPos+30, hPos+20, vPos,});
	}

	
	public void move() {
		this.hPos += this.hSpeed;
		polygon.setTranslateX(hSpeed + hPos);
		System.out.println("moving");
	}
	
	
}
