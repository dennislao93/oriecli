package map;

import objects.UnitType;

public class LinkProperty {
	
	private Request request;
	private UnitType unitType;
	private boolean infinite;
	
	public Request getRequest() {
		return request;
	}
	
	public UnitType getUnitType() {
		return unitType;
	}
	
	public boolean isInfinite() {
		return infinite;
	}

}
