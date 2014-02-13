package actions;

import game.Game;

public class PauseClient extends Action {

	// args: {gameType}
	public PauseClient(String[] args) {
		super(args);
	}

	@Override
	public void processAction(Game game) {
		game.togglePause();
	}

	@Override
	public String getHeader() {
		return "pseC";
	}

}
