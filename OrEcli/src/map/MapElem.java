package map;

import game.Side;

public abstract class MapElem {
	
	private Side side;
	
	public MapElem(Side side) {
		this.side = side;
	}
	
	public Side getSide() {
		return side;
	}
	
}
