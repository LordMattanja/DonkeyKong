package Objects;

public class Player extends GameObject {

	private int vSpeed, hSpeed, hPos, vPos;
	private int health;
	private boolean collision;
	
	public Player(int hPos, int vPos, boolean collision) {
		super(hPos, vPos, collision);
	}

	
	public void move(int hSpeed) {
		this.hPos += hSpeed;
	}
	
	
}
