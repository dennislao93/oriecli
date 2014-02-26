package map;

import objects.Unit;
import objects.UnitType;

public class LinkProperty {
	
	private Unit requestedUnit;
	private UnitType unitType;
	private boolean infinite;
	
	public void setRequestedUnit(Unit unit) {
		this.requestedUnit = unit;
	}
	
	public Unit getRequestedUnit() {
		return requestedUnit;
	}
	
	public UnitType getUnitType() {
		return unitType;
	}
	
	public boolean isInfinite() {
		return infinite;
	}

}
