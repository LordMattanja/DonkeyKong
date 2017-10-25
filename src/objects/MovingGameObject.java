package objects;

public class MovingGameObject extends GameObject {
	
	private Double hSpeed, vSpeed;

	public Double gethSpeed() {
		return hSpeed;
	}

	public void sethSpeed(Double hSpeed) {
		this.hSpeed = hSpeed;
	}

	public Double getvSpeed() {
		return vSpeed;
	}

	public void setvSpeed(Double vSpeed) {
		this.vSpeed = vSpeed;
	}

	public MovingGameObject(Double hPos, Double vPos, boolean collision) {
		super(hPos, vPos, collision);
	}

}
