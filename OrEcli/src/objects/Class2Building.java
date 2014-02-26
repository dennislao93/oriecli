package objects;

import display.ImageHandler;
import map.MapBuilding;

public abstract class Class2Building extends Building {

	private MapBuilding mapBuilding1;
	private MapBuilding mapBuilding2;

	public Class2Building(ImageHandler imageHandler, int HPMax, MapBuilding mapBuilding1, MapBuilding mapBuilding2) {
		super(imageHandler, HPMax);
		this.mapBuilding1 = mapBuilding1;
		this.mapBuilding2 = mapBuilding2;
	}
	
	public MapBuilding getMapElem1() {
		return mapBuilding1;
	}
	
	public MapBuilding getMapElem2() {
		return mapBuilding2;
	}

}
