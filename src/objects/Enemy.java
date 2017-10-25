package objects;

import javafx.scene.shape.Polygon;

public class Enemy extends StaticGameObject{
	
	private int hPos, vPos;
	private boolean collision;
	private Polygon polygon;

	public Enemy(Double hPos, Double vPos, boolean collision) {
		super(hPos, vPos, collision);
	}

}
