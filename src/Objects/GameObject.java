package Objects;

import javafx.scene.shape.Polygon;

public class GameObject {
	
	private int hPos, vPos;
	private boolean collision;
	private Polygon polygon;
	

	public int gethPos() {
		return hPos;
	}

	public void sethPos(int hPos) {
		this.hPos = hPos;
	}

	public int getvPos() {
		return vPos;
	}

	public void setvPos(int vPos) {
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



	public GameObject(int hPos, int vPos, boolean collision) {
		this.hPos = hPos;
		this.vPos = vPos;
		this.collision = collision;
	}

}
