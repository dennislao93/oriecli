package map;

import java.util.ArrayList;

import game.Side;
import display.Displayable;

public class Road extends Displayable {
	
	private ArrayList<Junction> inlets;
	private ArrayList<Junction> outlets;

	public Road(int xPos, int yPos) {
		super(xPos, yPos);
		inlets = new ArrayList<Junction>();
		outlets = new ArrayList<Junction>();
	}
	
	public void addInlet(Junction j) {
		inlets.add(j);
	}
	
	public void addOutlet(Junction j) {
		outlets.add(j);
	}

	@Override
	public void display() {
		// TODO Auto-generated method stub

	}

}
