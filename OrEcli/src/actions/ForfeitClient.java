package actions;

import game.Game;

public class ForfeitClient extends Action {

	// args: {gameType}
	public ForfeitClient(String[] args) {
		super(args);
	}

	@Override
	public void processAction(Game game) {
		game.setIsForfeit(true);
		if (args[0].equals("SERVER")) {
			game.setIsVictor(true);
		} else {
			game.setIsVictor(false);
		}
	}

	@Override
	public String getHeader() {
		return "fftC";
	}

}
