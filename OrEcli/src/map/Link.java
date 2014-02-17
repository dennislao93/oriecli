package map;

import java.util.ArrayList;

public class Link {
	
	private Road from;
	private Road to;
	private ArrayList<LinkProperty> properties;
	
	public Link(Road from, Road to, ArrayList<LinkProperty> properties) {
		this.from = from;
		this.to = to;
		this.properties = properties;
	}
	
	public void addProperty(LinkProperty p) {
		properties.add(p);
	}
	
	public ArrayList<LinkProperty> getProperties() {
		return properties;
	}

}
