package objects;

import map.MapBuilding;

public abstract class Class1Building extends Building {
	
	private MapBuilding mapBuilding;
	
	public Class1Building(int xPos, int yPos, int HPMax, MapBuilding mapBuilding) {
		super(xPos, yPos, HPMax);
		this.mapBuilding = mapBuilding;
	}

}
