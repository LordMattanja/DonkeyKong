package objects;

import java.util.Random;

import general.ImageLoader;
import general.Settings;
import gui.MainApplication;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class Barrel extends GameObject{

	public Barrel(double hPos, double vPos, boolean ingame, double size, int speed) {
		super(hPos, vPos + size/2, size, size, new Circle(hPos, vPos, size/2), ImageLoader.getBarrelImage());
		getShape().setFill(new ImagePattern(getImage()));
		if(ingame) {
			createNextBarrelPath(this, speed, true, null);
		}
	} 
	
	/*
	 * Berechnet für das Fass den Weg den es als nächstes zurücklegt, also entweder zur nächsten Leiter/Ende der Plattform oder bis zur nächsten Plattform darunter und
	 * ruft sich nach Ende der Animation wieder selbst auf um den nächsten Weg zu berechnen
	 */
	private void createNextBarrelPath(Barrel barrel, int speed, boolean rolling, Ladder usedLadder) {
		TranslateTransition transition = new TranslateTransition();
		RotateTransition rotate = new RotateTransition();
		transition.setNode(barrel.getShape());
		transition.setFromX(barrel.getShape().getBoundsInParent().getMinX());
		transition.setFromY(barrel.getShape().getBoundsInParent().getMaxY()- barrel.getvPos());
		transition.setInterpolator(Interpolator.LINEAR);
		transition.setCycleCount(1);
		if (rolling) {
			rotate = new RotateTransition(Duration.seconds(1), barrel.getShape());
			rotate.setByAngle((MainApplication.getMain().getGamestate().getCurrentlyUsedPlatform(barrel) != null)? MainApplication.getMain().getGamestate().getCurrentlyUsedPlatform(barrel).getTilt()*-36 : -360);
			rotate.setCycleCount(RotateTransition.INDEFINITE);
			rotate.setInterpolator(Interpolator.LINEAR);
			objects.Platform usedPlatform = MainApplication.getMain().getGamestate().getCurrentlyUsedPlatform(barrel);
			if (usedPlatform != null) {
				transition.setToX((usedPlatform.getTilt() < 0) ? 600 : 5);
				transition.setToY((usedPlatform.getTilt() < 0) ? usedPlatform.getvPos() - barrel.getvPos() - usedPlatform.getTilt() : usedPlatform.getvPos() - barrel.getvPos() + usedPlatform.getTilt());
				Random rand = new Random();				
				int x = rand.nextInt(5);
				if (x < usedPlatform.getLadders().length && usedPlatform.getLadders()[x].getvPos() > barrel.getShape()
						.getBoundsInParent().getMaxY()) {
					usedLadder = usedPlatform.getLadders()[x];
					transition.setToX(usedLadder.gethPos());
					transition.setDuration(Duration
							.seconds((4.0)* Math.abs(((transition.getFromX()-transition.getToX())/Settings.tiltedPlatformLength))));
					transition.setToY(usedLadder.getvPos()-barrel.getvPos());
				} else {
					transition.setDuration(Duration.seconds((usedLadder == null)? 4 + speed / 10.0 : (4.0)* Math.abs(((transition.getFromX()-transition.getToX())/Settings.tiltedPlatformLength))));			
					usedLadder = null;
				}
				if(usedPlatform.getTilt() == 0) {
					usedLadder = null;
					transition.setToX(-25);
					transition.setInterpolator(Interpolator.EASE_OUT);
					transition.setDuration(Duration.seconds(4.0));
					rotate.setByAngle(-360);
				}
			} else {
				usedLadder = null;
				transition.setToX(-25);
				transition.setInterpolator(Interpolator.EASE_OUT);
				transition.setDuration(Duration.seconds(4.0));
			}
			rotate.play();
		} else {
			transition.setDuration(Duration.seconds(.7 + speed / 15.0));
			if (usedLadder != null) {
				transition.setByY(usedLadder.getHeight());
			} else {
				transition.setToY((barrel.getShape().getBoundsInParent().getMaxY() < Settings.playerStartingPosY - 100)
						? transition.getFromY() + 500 / Settings.numberOfPlatforms - 20
						: Settings.playerStartingPosY - barrel.getvPos());
			}
		}
		final RotateTransition rotateFinal = rotate;
		final Ladder finalLadder = usedLadder;
		transition.setOnFinished(e -> {
			if (barrel.getShape().getBoundsInParent().getMinX() < 0) {
				MainApplication.getMain().getGamestate().removeBarrel(barrel);
			} else {
				if (rolling) {
					rotateFinal.stop();
				}
				createNextBarrelPath(barrel, speed, !rolling, finalLadder);
			}
		});
		transition.play();
	}

}