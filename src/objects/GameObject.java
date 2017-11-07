package objects;

import javafx.scene.shape.Polygon;

public abstract class GameObject {
	
	private Double hPos, vPos;
	private boolean collision;
	private Polygon polygon;
	

	public abstract Polygon getPolygon();


}
