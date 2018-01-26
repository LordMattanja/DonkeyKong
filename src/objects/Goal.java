package objects;

import javafx.scene.shape.Rectangle;

public class Goal extends GameObject {
		
	public Goal() {
		super(80, 55, 30, 40, new Rectangle(80, 55, 30, 40), null);
		shape.getStyleClass().add("goal");
	}

	
}
