package objects;

import javafx.scene.shape.Polygon;

public class Barrel extends MovingGameObject {
	
	private int hPos, vPos;
	private boolean collision;
	private Polygon polygon;

	public Barrel(Double hPos, Double vPos, boolean collision) {
		super(hPos, vPos, collision);
	}

}
