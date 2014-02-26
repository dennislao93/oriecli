package map;

import game.Side;

public class MapRoad extends MapElem {

	private int dir;
	private boolean isBig;
	
	public MapRoad(Side side, int dir, boolean isBig) {
		super(side);
		this.dir = dir;
		this.isBig = isBig;
	}

}
