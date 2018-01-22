package objects;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import utils.ImageLoader;

public class Ladder extends StaticGameObject {
	
	private Platform platformBelow, platformAbove;
	private double hPos, vPos, height;
	private boolean collision;
	private Shape rect;
	private Image img = ImageLoader.getLadderImage();
	

	public double getHeight() {
		return height;
	}

	public Ladder(double hPos, double vPos, double height) {
		this.hPos = hPos;
		this.vPos = vPos;
		this.height = height;
		rect = new Rectangle(hPos, vPos, 25, height);
		rect.setFill(new ImagePattern(img));
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

	
	
}
