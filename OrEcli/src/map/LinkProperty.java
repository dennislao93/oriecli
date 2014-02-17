package map;

import objects.Building;
import objects.UnitType;

public class LinkProperty {
	
	private Building building;
	private UnitType unitType;
	private boolean infinite;
	
	public Building getBuilding() {
		return building;
	}
	
	public UnitType getUnitType() {
		return unitType;
	}
	
	public boolean isInfinite() {
		return infinite;
	}

}
