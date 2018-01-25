package objects;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import utils.ImageLoader;

public class Ladder extends GameObject {
	
	private double hPos, vPos, width, height;
	private Shape rect;
	private Image img = ImageLoader.getLadderImage();
	
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

	public Ladder(double hPos, double vPos, double height) {
		this.hPos = hPos;
		this.vPos = vPos;
		this.height = height;
		rect = new Rectangle(hPos, vPos, 25, (vPos > 500)? height + 10 : height);
		rect.setFill(new ImagePattern(img));
	}

	
	
}
