package map;

import objects.Building;
import objects.Unit;
import display.Road;

public class Tile {
	
	private Road road;
	private Building building;
	private Unit unit;
	
	public Road getRoad() {
		return road;
	}
	public void setRoad(Road road) {
		this.road = road;
	}
	public Building getBuilding() {
		return building;
	}
	public void setBuilding(Building building) {
		this.building = building;
	}
	public Unit getUnit() {
		return unit;
	}
	public void setUnit(Unit unit) {
		this.unit = unit;
	}

}
