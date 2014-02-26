package objects;

import display.ImageHandler;
import map.MapBuilding;

public abstract class Class1Building extends Building {
	
	private MapBuilding mapBuilding;
	
	public Class1Building(ImageHandler imageHandler, int HPMax, MapBuilding mapBuilding) {
		super(imageHandler, HPMax);
		this.mapBuilding = mapBuilding;
	}
	
	public MapBuilding getMapElem() {
		return mapBuilding;
	}

}
