package objects;

import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Goal extends GameObject {
	
	private double hPos, vPos, height, width;
	private Rectangle rect;

	@Override
	public double getHeight() {
		return height;
	}

	@Override
	public double gethPos() {
		return hPos;
	}

	@Override
	public double getvPos() {
		return vPos;
	}

	@Override
	public Shape getShape() {
		return rect;
	}
	
	@Override
	public void sethPos(double hPos) {
		this.hPos = hPos;
	}

	@Override
	public void setvPos(double vPos) {
		this.vPos = vPos;
	}

	@Override
	public double getWidth() {
		return width;
	}

	
	public Goal() {
		vPos = 55;
		hPos = 80;
		height = 40;
		width = 30;
		rect = new Rectangle(hPos, vPos, width, height);
		rect.getStyleClass().add("goal");
	}

	
}
