package display;

public class Mouse {
	
	private MouseMode mode = MouseMode.SELECT;
	
	public void setMouseMode(MouseMode mode) {
		this.mode = mode;
	}
	
	public MouseMode getMouseMode() {
		return mode;
	}

}
