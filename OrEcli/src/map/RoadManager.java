package map;

import java.util.ArrayList;
import java.util.HashMap;

public class RoadManager {

	private HashMap<MapElem, ArrayList<Link>> inlets;
	private HashMap<MapElem, ArrayList<Link>> outlets;
	private ArrayList<LinkProperty> helperProperties;

	public RoadManager() {
		inlets = new HashMap<MapElem, ArrayList<Link>>();
		outlets = new HashMap<MapElem, ArrayList<Link>>();
		helperProperties = new ArrayList<LinkProperty>();
	}

	public void addRoadOutlet(MapElem prev, MapRoad next) {
		handleNewOutlet(prev, next);
	}

	public void addBuildingOutlet(MapElem prev, MapBuilding next) {
		handleNewOutlet(prev, next);
		addProperties(next, next.getProperties());
	}
	
	public void addExit(MapElem prev, MapExit exit) {
		Link link = new Link(prev, exit, new ArrayList<LinkProperty>());
		outlets.get(prev).add(link);
		inlets.put(exit, new ArrayList<Link>());
		inlets.get(exit).add(link);
		addProperties(exit, exit.getProperties());
	}

	private void handleNewOutlet(MapElem prev, MapElem next) {
		Link link = new Link(prev, next, new ArrayList<LinkProperty>());
		outlets.get(prev).add(link);
		inlets.put(next, new ArrayList<Link>());
		inlets.get(next).add(link);
		outlets.put(next, new ArrayList<Link>());
	}

	public void addRoadInletToRoad(MapElem from, MapRoad to, MapRoad thru) {
		handleInletProperties(from, to, thru);
		gatherOutletProperties(to);
		addProperties(to, helperProperties);
	}

	public void addBuildingInletToRoad(MapElem from, MapRoad to, MapBuilding thru) {
		handleInletProperties(from, to, thru);
		gatherOutletProperties(to);
		addPropertiesSingleElem(to, helperProperties);
		helperProperties.addAll(thru.getProperties());
		addProperties(thru, helperProperties);
	}

	public void addRoadInletToBuilding(MapElem from, MapBuilding to, MapRoad thru) {
		handleInletProperties(from, to, thru);
		gatherOutletProperties(to);
		helperProperties.addAll(to.getProperties());
		addProperties(to, helperProperties);
	}

	public void addBuildingInletToBuilding(MapElem from, MapBuilding to, MapBuilding thru) {
		handleInletProperties(from, to, thru);
		gatherOutletProperties(to);
		helperProperties.addAll(to.getProperties());
		addPropertiesSingleElem(to, helperProperties);
		helperProperties.addAll(thru.getProperties());
		addProperties(thru, helperProperties);
	}
	
	private void handleInletProperties(MapElem from, MapElem to, MapElem thru) {
		Link link = new Link(from, thru, new ArrayList<LinkProperty>());
		outlets.get(from).add(link);
		inlets.put(thru, new ArrayList<Link>());
		inlets.get(thru).add(link);
		outlets.put(thru, new ArrayList<Link>());
		Link link2 = new Link(thru, to, new ArrayList<LinkProperty>());
		outlets.get(thru).add(link2);
		inlets.get(to).add(link2);
	}

	private void gatherOutletProperties(MapElem elem) {
		helperProperties.clear();
		for (Link outletLink: outlets.get(elem)) {
			for (LinkProperty outletLinkProperty: outletLink.getProperties()) {
				helperProperties.add(outletLinkProperty);
			}
		}
	}

	private boolean visited;
	private void addProperties(MapElem elem, ArrayList<LinkProperty> properties) {
		visited = true;
		addPropertiesSingleElem(elem, properties);
		if (!visited) {
			for (Link elemInletLink: inlets.get(elem)) {
				addProperties(elemInletLink.getFrom(), properties);
			}
		}
	}
	
	private void addPropertiesSingleElem(MapElem elem, ArrayList<LinkProperty> properties) {
		for (Link elemInletLink: inlets.get(elem)) {
			for (LinkProperty newProp: properties) {
				for (LinkProperty elemInletProp: elemInletLink.getProperties()) {
					if (newProp.getRequest() != elemInletProp.getRequest()) {
						visited = false;
						elemInletLink.addProperty(newProp);
					}
				}
			}
		}
	}
	
	public void removeRoad(MapRoad road) {
		
	}
	
	public void removeBuilding(MapBuilding building) {
		
	}

}
