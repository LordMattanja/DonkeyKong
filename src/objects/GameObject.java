package objects;

import javafx.scene.image.Image;
import javafx.scene.shape.Shape;

public abstract class GameObject {
	
	//Attribute mit denen die Position und Größe des Objekts bestimmt werden kann
	protected double hPos, vPos, height, width;
	//Shape, der die Form des Objekt repräsentiert
	protected Shape shape;
	//Bild mit dem das Objekt angezeigt wird
	protected Image img;

	public double getHeight() {
		return height;
	}

	public double gethPos() {
		return hPos;
	}

	public double getvPos() {
		return vPos;
	}

	public Shape getShape() {
		return shape;
	}
	
	public Image getImage() {
		return this.img;
	}
	
	public void sethPos(double hPos) {
		this.hPos = hPos;
	}

	public void setvPos(double vPos) {
		this.vPos = vPos;
	}

	public double getWidth() {
		return width;
	}
	
	protected GameObject(double hPos, double vPos, double width, double height, Shape s, Image img) {
		this.hPos = hPos;
		this.vPos = vPos;
		this.width = width;
		this.height = height;
		this.shape = s;
		this.img = img;
	}

}
