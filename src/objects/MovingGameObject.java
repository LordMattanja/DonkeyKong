package objects;

public class MovingGameObject extends GameObject {
	
	private int hSpeed, vSpeed;

	public int gethSpeed() {
		return hSpeed;
	}

	public void sethSpeed(int hSpeed) {
		this.hSpeed = hSpeed;
	}

	public int getvSpeed() {
		return vSpeed;
	}

	public void setvSpeed(int vSpeed) {
		this.vSpeed = vSpeed;
	}

	public MovingGameObject(int hPos, int vPos, boolean collision) {
		super(hPos, vPos, collision);
	}

}
