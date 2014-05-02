package map;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import objects.Roads;

public class RoadManager {
	Map map;

	public RoadManager(Map map) {
		this.map = map;
	}

	public void addRoadOutlet(MapElem[] prev, MapRoad next, byte[] dirs) {
		handleNewOutlet(prev, next, dirs);
	}

	public void addBuildingOutlet(MapElem[] prev, MapBuilding next, byte[] dir) {
		handleNewOutlet(prev, next, dir);
		addProperties(next, next.getProperties());
	}

	public void addExit(MapElem prev, MapExit exit, byte dir) {
		Link link = new Link(prev, exit, dir);
		map.outlets.get(prev).add(link);
		map.inlets.put(exit, new HashSet<Link>());
		map.inlets.get(exit).add(link);
		Roads.linkFrom.add(new Integer[]{prev.tileX, prev.tileY});
		Roads.linkTo.add(new Integer[]{exit.tileX, exit.tileY});
		addProperties(exit, exit.properties);
	}

	private void handleNewOutlet(MapElem[] prev, MapElem next, byte[] dirs) {
		map.inlets.put(next, new HashSet<Link>());
		for (int i = 0; i < prev.length; i++) {
			Link link = new Link(prev[i], next, dirs[i]);
			map.outlets.get(prev[i]).add(link);
			map.inlets.get(next).add(link);
			Roads.linkFrom.add(new Integer[]{prev[i].tileX, prev[i].tileY});
			Roads.linkTo.add(new Integer[]{next.tileX, next.tileY});
		}
		map.outlets.put(next, new HashSet<Link>());
	}

	public void addThruElem(MapElem[] from, MapElem[] to, MapElem thru, byte[] fromDirs, byte[] toDirs) {
		HashSet<LinkProperty> helperProperties = new HashSet<LinkProperty>();
		constructLinksAndGatherOutletProps(from, to, thru, fromDirs, toDirs, helperProperties);
		if (thru instanceof MapBuilding) {
			helperProperties.addAll(((MapBuilding)thru).getProperties());
		}
		addProperties(thru, helperProperties);
	}

	private void constructLinksAndGatherOutletProps(MapElem from[], MapElem to[], MapElem thru, byte[] fromDirs, byte[] toDirs, HashSet<LinkProperty> helperProperties) {
		map.inlets.put(thru, new HashSet<Link>());
		map.outlets.put(thru, new HashSet<Link>());
		int i;
		for (i = 0; i < from.length; i++) {
			Link fromLink = new Link(from[i], thru, fromDirs[i]);
			map.outlets.get(from[i]).add(fromLink);
			map.inlets.get(thru).add(fromLink);
			Roads.linkFrom.add(new Integer[]{from[i].tileX, from[i].tileY});
			Roads.linkTo.add(new Integer[]{thru.tileX, thru.tileY});
		}
		for (i = 0; i < to.length; i++) {
			Link toLink = new Link(thru, to[i], toDirs[i]);
			map.outlets.get(thru).add(toLink);
			map.inlets.get(to[i]).add(toLink);
			Roads.linkFrom.add(new Integer[]{thru.tileX, thru.tileY});
			Roads.linkTo.add(new Integer[]{to[i].tileX, to[i].tileY});
			gatherOutletProperties(to[i], helperProperties);
			if (to[i] instanceof MapBuilding) {
				helperProperties.addAll(((MapBuilding)to[i]).getProperties());
			}
		}
	}

	private void gatherOutletProperties(MapElem elem, HashSet<LinkProperty> helperProperties) {
		for (Link outletLink: map.outlets.get(elem)) {
			for (LinkProperty outletLinkProperty: outletLink.properties) {
				helperProperties.add(outletLinkProperty);
			}
		}
	}

	private void addProperties(MapElem elem, Collection<LinkProperty> newProperties) {
		if (!addPropertiesSingleElem(elem, newProperties)) {
			for (Link elemInletLink: map.inlets.get(elem)) {
				addProperties(elemInletLink.from, newProperties);
			}
		}
	}

	private boolean addPropertiesSingleElem(MapElem elem, Collection<LinkProperty> newProperties) {
		boolean visited = true;
		for (Link elemInletLink: map.inlets.get(elem)) {
			for (LinkProperty newProp: newProperties) {
				if (!elemInletLink.properties.contains(newProp)) {
					visited = false;
					elemInletLink.properties.add(newProp);
				}
			}
		}
		return visited;
	}

	public void removeElem(MapElem elem) {
		HashSet<LinkProperty> helperProperties = new HashSet<LinkProperty>();
		gatherOutletProperties(elem, helperProperties);
		if (elem instanceof MapBuilding) {
			helperProperties.addAll(((MapBuilding)elem).getProperties());
		}
		for (Link inletLink: map.inlets.get(elem)) {
			removeProperties(inletLink.from, helperProperties, inletLink);
		}
		map.validProperties.clear();
		removeLinks(elem);
	}

	public void removeExit(MapExit exit) {
		Link exitInlet = map.inlets.get(exit).iterator().next();
		removeProperties(exitInlet.from, exit.properties, exitInlet);
		Roads.removeLink(exitInlet.from.tileX, exitInlet.from.tileY, exit.tileX, exit.tileY);
		map.outlets.get(exitInlet.from).remove(exitInlet);
		map.inlets.remove(exit);
	}

	@SuppressWarnings("unchecked")
	private void removeProperties(MapElem elem, HashSet<LinkProperty> badProperties, Link fromOutlet) {
		for (Link outletLink: map.outlets.get(elem)) {
			if (outletLink == fromOutlet) continue;
			for (LinkProperty outletLinkProperty: outletLink.properties) {
				map.validProperties.add(outletLinkProperty);
			}
		}
		badProperties.removeAll(map.validProperties);
		if (badProperties.size() == 0) return;
		for (Link inletLink: map.inlets.get(elem)) {
			inletLink.properties.removeAll(badProperties);
			removeProperties(inletLink.from, (HashSet<LinkProperty>) badProperties.clone(), inletLink);
		}
	}

	private void removeLinks(MapElem elem) {
		for (Link inletLink: map.inlets.get(elem)) {
			Roads.removeLink(inletLink.from.tileX, inletLink.from.tileY, elem.tileX, elem.tileY);
			map.outlets.get(inletLink.from).remove(inletLink);
		}
		for (Link outletLink: map.outlets.get(elem)) {
			Roads.removeLink(elem.tileX, elem.tileY, outletLink.to.tileX, outletLink.to.tileY);
			map.inlets.get(outletLink.to).remove(outletLink);
		}
		map.inlets.remove(elem);
		map.outlets.remove(elem);
	}

}
