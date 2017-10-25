package objects;

import javafx.scene.shape.Polygon;

public class Platform extends StaticGameObject{

	private int hPos, vPos;
	private boolean collision;
	private Polygon polygon;
	
	public Platform(Double hPos, Double vPos, boolean collision) {
		super(hPos, vPos, collision);
	}

}
