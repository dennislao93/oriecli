package objects;

import display.ImageHandler;

public abstract class Vulnerable extends GameObject {
	
	private int HP;
	private int HPMax;

	public Vulnerable(ImageHandler imageHandler, int HPMax) {
		super(imageHandler);
		this.HPMax = HP = HPMax;
		HP = HPMax;
	}

	public boolean isDestroyed() {
		return HP >= 0;
	}

}
