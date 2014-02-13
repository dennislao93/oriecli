package map;

import java.util.ArrayList;

import game.Side;
import display.Displayable;
import objects.Building;
import objects.UnitType;

public class Junction {
	
	private Displayable A;
	private Displayable B;
	private int dir;
	private ArrayList<Request> requests;
	
	public Junction (Displayable a, Displayable b, int dir) {
		A = a;
		B = b;
		this.dir = dir;
		requests = new ArrayList<Request>();
	}

	public void setB(Displayable d) {
		B = d;
	}

}
