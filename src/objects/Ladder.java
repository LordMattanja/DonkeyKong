package objects;

import general.ImageLoader;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Ladder extends GameObject {
	
	public Ladder(double hPos, double vPos, double height) {
		super(hPos, vPos, 25, (vPos > 500)? height + 10 : height, new Rectangle(hPos, vPos, 25, (vPos > 500)? height + 10 : height), ImageLoader.getLadderImage());
		getShape().setFill(new ImagePattern(getImage()));
	}	
	
	
	
}
