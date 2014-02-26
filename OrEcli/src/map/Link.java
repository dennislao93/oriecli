package map;

import java.util.HashSet;

public class Link {
	
	private MapElem from;
	private MapElem to;
	private HashSet<LinkProperty> properties;
	private int dir;
	
	public Link(MapElem from, MapElem to, HashSet<LinkProperty> properties) {
		this.from = from;
		this.to = to;
		this.properties = properties;
	}
	
	public MapElem getFrom() {
		return from;
	}
	
	public MapElem getTo() {
		return to;
	}
	
	public void addProperty(LinkProperty p) {
		properties.add(p);
	}
	
	public void removeProperty(LinkProperty p) {
		properties.remove(p);
	}
	
	public HashSet<LinkProperty> getProperties() {
		return properties;
	}
	
	public int getDir() {
		return dir;
	}

}
