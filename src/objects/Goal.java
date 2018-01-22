package objects;

import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Goal extends StaticGameObject {
	
	private double hPos, vPos, height;
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
	
	public Goal() {
		vPos = 55;
		hPos = 80;
		height = 40;
		rect = new Rectangle(hPos, vPos, 30, height);
		rect.getStyleClass().add("goal");
	}

}
