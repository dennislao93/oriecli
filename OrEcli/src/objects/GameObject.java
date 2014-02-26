package objects;

import display.Displayable;
import display.ImageHandler;

public abstract class GameObject extends Displayable {

	public GameObject(ImageHandler imageHandler) {
		super(imageHandler);
	}
	
	public abstract void step();

}
