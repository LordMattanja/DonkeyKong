package objects;

import javafx.scene.shape.Polygon;

public class GameObject {
	
	private Double hPos, vPos;
	private boolean collision;
	private Polygon polygon;
	

	public Double gethPos() {
		return hPos;
	}

	public void sethPos(Double hPos) {
		this.hPos = hPos;
	}

	public Double getvPos() {
		return vPos;
	}

	public void setvPos(Double vPos) {
		this.vPos = vPos;
	}

	public boolean isCollision() {
		return collision;
	}

	public void setCollision(boolean collision) {
		this.collision = collision;
	}

	public Polygon getPolygon() {
		return polygon;
	}

	public void setPolygon(Polygon polygon) {
		this.polygon = polygon;
	}



	public GameObject(Double hPos, Double vPos, boolean collision) {
		this.hPos = hPos;
		this.vPos = vPos;
		this.collision = collision;
	}

}
