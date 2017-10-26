package objects;

import javafx.scene.shape.Polygon;

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
		polygon.getPoints().setAll(new Double[]{hPos, vPos, hPos+300, vPos, hPos+300, vPos+20, hPos, vPos+20});
	}

}
