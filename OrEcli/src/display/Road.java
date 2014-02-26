package display;

import map.MapRoad;


public class Road extends Displayable {
	
	private MapRoad mapRoad;

	public Road(ImageHandler imageHandler, MapRoad mapRoad) {
		super(imageHandler);
		this.mapRoad = mapRoad;
	}

	@Override
	public void display() {
		// TODO Auto-generated method stub
	}
	
	public MapRoad getMapElem() {
		return mapRoad;
	}
}
