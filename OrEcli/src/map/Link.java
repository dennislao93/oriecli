package map;

import java.util.HashSet;

public class Link {
	
	MapElem from;
	MapElem to;
	HashSet<LinkProperty> properties;
	byte dir;
	
	public Link(MapElem from, MapElem to, byte dir) {
		this.from = from;
		this.to = to;
		this.properties = new HashSet<LinkProperty>();
		this.dir = dir;
	}

}
