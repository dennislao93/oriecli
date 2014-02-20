package objects;

import map.MapBuilding;

public abstract class Class2Building extends Building {

	private MapBuilding mapBuilding1;
	private MapBuilding mapBuilding2;

	public Class2Building(int xPos, int yPos, int HPMax, MapBuilding mapBuilding1, MapBuilding mapBuilding2) {
		super(xPos, yPos, HPMax);
		this.mapBuilding1 = mapBuilding1;
		this.mapBuilding2 = mapBuilding2;
	}

}
