package Exceptions;


public class CollisionException extends Exception {
	
	int platformTilt;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public int getPlatformTilt(){
		return platformTilt;
	}
	
	public CollisionException(int tilt) {
		platformTilt = tilt;
	}
	

}
