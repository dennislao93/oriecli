package actions;

import game.Game;

public class BuildRoad extends Action {

	// args: {xPos, yPos}
	public BuildRoad(String[] args) {
		super(args);
	}

	@Override
	public void processAction(Game game) {
		game.getMap().buildRoad(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
	}

	@Override
	public String getHeader() {
		return "bdr";
	}

}
