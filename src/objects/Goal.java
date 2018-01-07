package objects;

import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;

public class Goal extends StaticGameObject {
	
	private double hPos, vPos, height;
	private Polygon polygon;

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
	public Polygon getPolygon() {
		return polygon;
	}
	
	public Goal() {
		vPos = 55;
		hPos = 80;
		height = 40;
		polygon = new Polygon();
		this.polygon.setFill(Color.DARKGREEN);
		this.polygon.getPoints().setAll(new Double[]{ hPos, vPos, hPos+30, vPos, hPos+30, vPos + height, hPos, vPos+height});
	}

}
