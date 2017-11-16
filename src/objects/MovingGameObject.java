package objects;

public abstract class MovingGameObject extends GameObject implements Runnable{
	

	public abstract Double gethPos();

	public abstract void sethPos(Double hPos);

	public abstract  Double getvPos();
	
	public abstract void setvPos(Double vPos);

}
