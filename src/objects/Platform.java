package objects;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import utils.Settings;

public class Platform extends StaticGameObject{
	
	private int hPos, vPos, tilt;
	private boolean collision;
	private Polygon polygon;

	public int getTilt() {
		return tilt;
	}
	
	public Polygon getPolygon() {
		return polygon;
	}

	public Platform(Double hPos, Double vPos, int length, boolean collision, int tilt) {
		super(hPos, vPos, collision);
		polygon = new Polygon();
		polygon.setFill(Color.BURLYWOOD);
		this.tilt = tilt;
		polygon.getPoints().setAll(new Double[]{hPos, vPos+tilt, hPos+length, vPos-tilt, hPos+length, vPos+15-tilt, hPos, vPos+15+tilt});
		;
	}

}
