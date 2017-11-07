package objects;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Ladder extends StaticGameObject {
	
	private Platform platformBelow, platformAbove;
	private int hPos, vPos;
	private boolean collision;
	private Polygon polygon;
	

	
	public Polygon getPolygon() {
		return polygon;
	}

	public Ladder(Double hPos, Double vPos, Double height) {
		super(hPos, vPos, false);
		polygon = new Polygon();
		polygon.setFill(Color.BROWN);
		polygon.getPoints().setAll(new Double[]{hPos, vPos, hPos+20, vPos, hPos+20, vPos+height, hPos, vPos+height});
	}

	
	
}
