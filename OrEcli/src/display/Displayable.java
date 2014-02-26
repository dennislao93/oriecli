package display;

public abstract class Displayable {
	
	private ImageHandler imageHandler;
	private int xPos;
	private int yPos;
	
	public Displayable(ImageHandler imageHandler) {
		this.imageHandler = imageHandler;
	}
	
	public int getXPos() {
		return xPos;
	}
	
	public int getYPos() {
		return yPos;
	}
	
	public abstract void display();
	
}
