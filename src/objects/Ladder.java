package objects;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Ladder extends StaticGameObject {
	
	private Platform platformBelow, platformAbove;
	private double hPos, vPos, height;
	private boolean collision;
	private Polygon polygon;
	

	
	public Polygon getPolygon() {
		return polygon;
	}

	public double getHeight() {
		return height;
	}

	public Ladder(double hPos, double vPos, double height) {
		this.hPos = hPos;
		this.vPos = vPos;
		this.height = height;
		polygon = new Polygon();
		polygon.setFill(Color.BROWN);
		polygon.getPoints().setAll(new Double[]{hPos, vPos, hPos+25, vPos, hPos+25, vPos+height+2, hPos, vPos+height+2});
	}

	@Override
	public double gethPos() {
		return hPos;
	}


	@Override
	public double getvPos() {
		return vPos;
	}

	
	
}
