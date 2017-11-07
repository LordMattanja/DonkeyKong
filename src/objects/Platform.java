package objects;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import utils.Settings;

public class Platform extends StaticGameObject{

	private int hPos, vPos, tilt = 10;
	private boolean collision;
	private Polygon polygon;
	private boolean tiltedLeft;
	
	
	public Polygon getPolygon() {
		return polygon;
	}



	public Platform(Double hPos, Double vPos, boolean collision, boolean tiltedLeft) {
		super(hPos, vPos, collision);
		this.tiltedLeft = tiltedLeft;
		if(this.tiltedLeft){
			tilt*= -1;
		}
		polygon = new Polygon();
		polygon.setFill(Color.BURLYWOOD);
		polygon.getPoints().setAll(new Double[]{hPos, vPos+tilt, hPos+Settings.platformLength, vPos-tilt, hPos+Settings.platformLength, vPos+15-tilt, hPos, vPos+15+tilt});
	}

}
