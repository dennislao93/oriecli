package display;

public abstract class Displayable {
	
	private int xPos;
	private int yPos;
	
	public Displayable(int xPos, int yPos) {
		this.xPos = xPos;
		this.yPos = yPos;
	}
	
	public int getXPos() {
		return xPos;
	}
	
	public int getYPos() {
		return yPos;
	}
	
	public abstract void display();
	
}
