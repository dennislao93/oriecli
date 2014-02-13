package actions;

import main.Constants;
import game.Game;
import game.Side;

public class BuildRoad extends Action {

	// args: {xPos, yPos, dir, big/small, P1/P2}
	public BuildRoad(String[] args) {
		super(args);
	}

	@Override
	public void processAction(Game game) {
		game.getMap().addRoad(Integer.parseInt(args[0]), Integer.parseInt(args[1]), getDir(args[2]), args[3].equals("big"), args[4].equals("P1") ? Side.PLAYER1 : Side.PLAYER2);
	}

	private int getDir(String string) {
		if (string.equals("NORTH")) {
			return Constants.NORTH;
		} else if (string.equals("SOUTH")) {
			return Constants.SOUTH;
		}
		return string.equals("EAST") ? Constants.EAST : Constants.WEST;
	}

	@Override
	public String getHeader() {
		return "bdr";
	}

}
