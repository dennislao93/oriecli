package map;

import java.util.ArrayList;

public class Link {
	
	private MapElem from;
	private MapElem to;
	private ArrayList<LinkProperty> properties;
	private int dir;
	
	public Link(MapElem from, MapElem to, ArrayList<LinkProperty> properties) {
		this.from = from;
		this.to = to;
		this.properties = properties;
	}
	
	public MapElem getFrom() {
		return from;
	}
	
	public void addProperty(LinkProperty p) {
		properties.add(p);
	}
	
	public ArrayList<LinkProperty> getProperties() {
		return properties;
	}
	
	public void clearProperties() {
		properties.clear();
	}
	
	public int getDir() {
		return dir;
	}

}
