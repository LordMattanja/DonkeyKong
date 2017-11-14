package objects;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import utils.Settings;

public class TiltedPlatform extends Platform {
	
	private int hPos, vPos;
	private boolean collision;
	private Polygon polygon;
	private boolean tiltedLeft;	
	
	public Polygon getPolygon() {
		return polygon;
	}

	public TiltedPlatform(Double hPos, Double vPos, boolean collision, int tilt) {
		super(hPos, vPos, collision);
		polygon = new Polygon();
		polygon.setFill(Color.BURLYWOOD);
		polygon.getPoints().setAll(new Double[]{hPos, vPos+tilt, hPos+Settings.tiltedPlatformLength, vPos-tilt, hPos+Settings.tiltedPlatformLength, vPos+15-tilt, hPos, vPos+15+tilt});
	}

}
