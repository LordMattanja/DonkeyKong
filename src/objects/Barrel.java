package objects;

import gui.MainApplication;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import utils.ImageLoader;

public class Barrel extends GameObject{

	public Barrel(double hPos, double vPos, boolean ingame, double size, int speed) {
		super(hPos, vPos + size/2, size, size, new Circle(hPos, vPos, size/2), ImageLoader.getBarrelImage());
		getShape().setFill(new ImagePattern(getImage()));
		if(ingame) {
			MainApplication.getMain().getContrLevel().createNewBarrelPath(this, speed, true);
		}
	}

}