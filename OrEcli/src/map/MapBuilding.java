package map;

import game.Side;

import java.util.HashSet;

public class MapBuilding extends MapElem {
	
	private BuildingLink inletLinks[];
	private BuildingLink outletLinks[];

	public MapBuilding(int inletLinks[][][], int outletLinks[][][], boolean isBig) {
		this.inletLinks = new BuildingLink[inletLinks.length];
		this.outletLinks = new BuildingLink[outletLinks.length];
		int i = 0;
		for (int inletLink[][]: inletLinks) {
			this.inletLinks[i] = new BuildingLink(isBig, inletLink[0], inletLink[1]);
		}
		for (int outletLink[][]: outletLinks) {
			this.outletLinks[i] = new BuildingLink(isBig, outletLink[0], outletLink[1]);
		}
	}

	public HashSet<LinkProperty> getProperties() {
		// TODO Auto-generated method stub
		return null;
	}
}
