package objects;

import display.Displayable;

public abstract class GameObject extends Displayable {

	public GameObject(int xPos, int yPos) {
		super(xPos, yPos);
	}
	
	public abstract void step();

}
