package actions;

import game.Game;

public class Resume extends Action {

	// args: {gameType, init/confirm/deny}
	public Resume(String[] args) {
		super(args);
	}

	@Override
	public void processAction(Game game) {
		if (args[0].equals("SINGLE_PLAYER")) {
			game.togglePause();
			return;
		}
		game.addUpdate(new ResumeClient(args));
		if (args[0].equals("SERVER")) {
			if (args[1].equals("init")) {
				game.waitForResume();
			} else if (args[1].equals("confirm")) {
				game.togglePause();
			} else { // deny
				game.denyResume();
			}
		} else {
			if (args[1].equals("init")) {
				game.promptResume();
			} else if (args[1].equals("confirm")) {
				game.togglePause();
			} else { // deny
				game.denyResume();
			}
		}
	}

	@Override
	public String getHeader() {
		return "rsm";
	}

}
