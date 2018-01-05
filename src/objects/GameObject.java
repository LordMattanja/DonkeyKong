package objects;

import javafx.scene.image.Image;
import javafx.scene.shape.Polygon;

public abstract class GameObject {
	
	private Double hPos, vPos;
	private boolean collision;
	private Polygon polygon;
	private Image img;
	

	public abstract Polygon getPolygon();


}
