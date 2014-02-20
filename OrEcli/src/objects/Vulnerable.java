package objects;

public abstract class Vulnerable extends GameObject {
	
	private int HP;
	private int HPMax;

	public Vulnerable(int xPos, int yPos, int HPMax) {
		super(xPos, yPos);
		this.HPMax = HP = HPMax;
		HP = HPMax;
	}

	public boolean isDestroyed() {
		return HP >= 0;
	}

}
