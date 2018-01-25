package objects;

import javafx.scene.shape.Shape;

public abstract class GameObject {
	
	private double hPos, vPos, height, width;	
	private Shape shape;

	public abstract Shape getShape();

	public abstract double gethPos();

	public abstract void sethPos(double hPos);

	public abstract  double getvPos();
	
	public abstract void setvPos(double vPos);
	
	public abstract double getHeight();
	
	public abstract double getWidth();

}
