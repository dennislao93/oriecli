package map;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class RoadManager {

	private HashMap<MapElem, HashSet<Link>> inlets;
	private HashMap<MapElem, HashSet<Link>> outlets;
	private HashSet<LinkProperty> helperProperties;
	private HashSet<LinkProperty> validProperties;
	private Iterator<LinkProperty> badPropertiesIterator;

	public RoadManager(MapRoad startingRoad1, MapRoad startingRoad2) {
		inlets = new HashMap<MapElem, HashSet<Link>>();
		outlets = new HashMap<MapElem, HashSet<Link>>();
		helperProperties = new HashSet<LinkProperty>();
		validProperties = new HashSet<LinkProperty>();
		outlets.put(startingRoad1, new HashSet<Link>());
		outlets.put(startingRoad2, new HashSet<Link>());
	}

	public void addRoadOutlet(MapElem prev, MapRoad next) {
		handleNewOutlet(prev, next);
	}

	public void addBuildingOutlet(MapElem prev, MapBuilding next) {
		handleNewOutlet(prev, next);
		addProperties(next, next.getProperties());
	}

	public void addExit(MapElem prev, MapExit exit) {
		Link link = new Link(prev, exit, new HashSet<LinkProperty>());
		outlets.get(prev).add(link);
		inlets.put(exit, new HashSet<Link>());
		inlets.get(exit).add(link);
		addProperties(exit, exit.getProperties());
	}

	private void handleNewOutlet(MapElem prev, MapElem next) {
		Link link = new Link(prev, next, new HashSet<LinkProperty>());
		outlets.get(prev).add(link);
		inlets.put(next, new HashSet<Link>());
		inlets.get(next).add(link);
		outlets.put(next, new HashSet<Link>());
	}

	public void addThruRoad(MapElem from[], MapElem to[], MapRoad thru) {
		constructLinksAndGatherOutletProps(from, to, thru);
		addProperties(thru, helperProperties);
	}

	public void addThruBuilding(MapElem from[], MapElem to[], MapBuilding thru) {
		constructLinksAndGatherOutletProps(from, to, thru);
		helperProperties.addAll(thru.getProperties());
		addProperties(thru, helperProperties);
	}

	private void constructLinksAndGatherOutletProps(MapElem from[], MapElem to[], MapElem thru) {
		inlets.put(thru, new HashSet<Link>());
		outlets.put(thru, new HashSet<Link>());
		for (MapElem fromElem: from) {
			Link fromLink = new Link(fromElem, thru, new HashSet<LinkProperty>());
			outlets.get(fromElem).add(fromLink);
			inlets.get(thru).add(fromLink);
		}
		helperProperties.clear();
		for (MapElem toElem: to) {
			Link toLink = new Link(thru, toElem, new HashSet<LinkProperty>());
			outlets.get(thru).add(toLink);
			inlets.get(toElem).add(toLink);
			gatherOutletProperties(toElem);
			if (toElem instanceof MapBuilding) {
				helperProperties.addAll(((MapBuilding)toElem).getProperties());
			}
		}
	}

	private void gatherOutletProperties(MapElem elem) {
		for (Link outletLink: outlets.get(elem)) {
			for (LinkProperty outletLinkProperty: outletLink.getProperties()) {
				helperProperties.add(outletLinkProperty);
			}
		}
	}

	private boolean visited;
	private void addProperties(MapElem elem, Collection<LinkProperty> newProperties) {
		visited = true;
		addPropertiesSingleElem(elem, newProperties);
		if (!visited) {
			for (Link elemInletLink: inlets.get(elem)) {
				addProperties(elemInletLink.getFrom(), newProperties);
			}
		}
	}

	private void addPropertiesSingleElem(MapElem elem, Collection<LinkProperty> newProperties) {
		for (Link elemInletLink: inlets.get(elem)) {
			for (LinkProperty newProp: newProperties) {
				if (!elemInletLink.getProperties().contains(newProp)) {
					visited = false;
					elemInletLink.addProperty(newProp);
				}
			}
		}
	}

	public void removeRoad(MapRoad road) {
		helperProperties.clear();
		gatherOutletProperties(road);
		for (Link inletLink: inlets.get(road)) {
			removeProperties(inletLink.getFrom(), helperProperties, inletLink);
		}
		removeLinks(road);
	}

	public void removeBuilding(MapBuilding building) {
		helperProperties.clear();
		gatherOutletProperties(building);
		helperProperties.addAll(building.getProperties());
		for (Link inletLink: inlets.get(building)) {
			removeProperties(inletLink.getFrom(), helperProperties, inletLink);
		}
		removeLinks(building);
	}

	public void removeExit(MapExit exit) {
		removeProperties(inlets.get(exit).iterator().next().getFrom(), exit.getProperties(), inlets.get(exit).iterator().next());
		outlets.get(inlets.get(exit).iterator().next().getFrom()).remove(inlets.get(exit).iterator().next());
		inlets.remove(exit);
	}

	@SuppressWarnings("unchecked")
	private void removeProperties(MapElem elem, HashSet<LinkProperty> badProperties, Link fromOutlet) {
		validProperties.clear();
		for (Link outletLink: outlets.get(elem)) {
			if (outletLink == fromOutlet) continue;
			for (LinkProperty outletLinkProperty: outletLink.getProperties()) {
				validProperties.add(outletLinkProperty);
			}
		}
		for (badPropertiesIterator = badProperties.iterator(); badPropertiesIterator.hasNext(); ) {
			for (LinkProperty validProperty: validProperties) {
				if (badPropertiesIterator.next() == validProperty) {
					badPropertiesIterator.remove();
				}
			}
		}
		for (Link inletLink: inlets.get(elem)) {
			for (LinkProperty badProperty: badProperties) {
				inletLink.removeProperty(badProperty);
			}
			removeProperties(inletLink.getFrom(), (HashSet<LinkProperty>) badProperties.clone(), inletLink);
		}
	}

	private void removeLinks(MapElem elem) {
		for (Link inletLink: inlets.get(elem)) {
			outlets.get(inletLink.getFrom()).remove(inletLink);
		}
		for (Link outletLink: outlets.get(elem)) {
			inlets.get(outletLink.getTo()).remove(outletLink);
		}
		inlets.remove(elem);
		outlets.remove(elem);
	}

}
