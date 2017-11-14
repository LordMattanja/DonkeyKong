package objects;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import utils.Settings;

public class Platform extends StaticGameObject{

	private int hPos, vPos;
	private boolean collision;
	private Polygon polygon;
	
	
	public Polygon getPolygon() {
		return polygon;
	}



	public Platform(Double hPos, Double vPos, boolean collision) {
		super(hPos, vPos, collision);
		polygon = new Polygon();
		polygon.setFill(Color.BURLYWOOD);
		polygon.getPoints().setAll(new Double[]{hPos, vPos, hPos+Settings.platformLength, vPos, hPos+Settings.platformLength, vPos+15, hPos, vPos+15});
	}

}
