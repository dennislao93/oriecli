package actions;

import game.Game;

public class Pause extends Action {

	// args: {gameType}
	public Pause(String[] args) {
		super(args);
	}

	@Override
	public void processAction(Game game) {
		game.togglePause();
		if (!args[0].equals("SINGLE_PLAYER")) {
			game.addUpdate(new PauseClient(args));
		}
	}

	@Override
	public String getHeader() {
		return "pse";
	}

}
