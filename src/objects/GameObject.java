package objects;

import javafx.scene.image.Image;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public abstract class GameObject {
	
	private Double hPos, vPos;
	private boolean collision;
	private Shape shape;
	private Image img;
	

	public abstract Shape getShape();


}
