package map;

import java.util.ArrayList;
import java.util.HashMap;

import objects.Building;

public class RoadManager {

	private HashMap<Road, ArrayList<Link>> inlets;
	private HashMap<Road, ArrayList<Link>> outlets;
	private ArrayList<LinkProperty> tempProperties;

	public RoadManager() {
		inlets = new HashMap<Road, ArrayList<Link>>();
		outlets = new HashMap<Road, ArrayList<Link>>();
		tempProperties = new ArrayList<LinkProperty>();
	}

	public void addOutlet(Road r, Road next) {
		Link link = new Link(r, next, new ArrayList<LinkProperty>());
		outlets.get(r).add(link);
		inlets.put(next, new ArrayList<Link>());
		inlets.get(next).add(link);
		outlets.put(next, new ArrayList<Link>());
	}

	public void addThruRoad(Road from, Road to, Road thru) {
		tempProperties.clear();
		for (Link l: outlets.get(to)) {
			for (LinkProperty p: l.getProperties()) {
				tempProperties.add(p);
			}
		}
		@SuppressWarnings("unchecked")
		Link link1 = new Link(thru, to, (ArrayList<LinkProperty>) tempProperties.clone());
		inlets.get(to).add(link1);
		outlets.put(thru, new ArrayList<Link>());
		outlets.get(thru).add(link1);
		inlets.put(thru,  new ArrayList<Link>());
		@SuppressWarnings("unchecked")
		Link link2 = new Link(from, thru, (ArrayList<LinkProperty>) tempProperties.clone());
		inlets.get(thru).add(link2);
		outlets.get(from).add(link2);
		addProperties(from, tempProperties);
	}

	private void addProperties(Road from, ArrayList<LinkProperty> properties) {
		
	}
	
	private void removeRoad(Road r) {
		
	}

}
