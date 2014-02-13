package objects;

import game.Side;

public abstract class Vulnerable extends GameObject {
	
	protected Side side;
	protected int HP;
	protected int MaxHP;
	protected boolean destroyed;
	
	public Vulnerable(int xPos, int yPos) {
		super(xPos, yPos);
	}
	
	public boolean isDestroyed() {
		return destroyed;
	}

}
