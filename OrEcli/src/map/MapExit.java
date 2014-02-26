package map;

import game.Side;

import java.util.HashSet;

public class MapExit extends MapElem {
	
	private HashSet<LinkProperty> properties;
	
	public MapExit(Side side) {
		super(side);
	}
	
	public HashSet<LinkProperty> getProperties() {
		return properties;
	}

}
