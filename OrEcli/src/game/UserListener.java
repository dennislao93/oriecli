package game;

import actions.Action;

public class UserListener {

	private Action action;
	private GameType gameType;

	public UserListener(GameType gameType) {
		this.gameType = gameType;
		action = null;
	}

	public synchronized void setAction(Action action) {
		this.action = action;
	}

	public synchronized Action getAction() {
		Action action1 = action;
		action = null;
		return action1;
	}
	
	public String getGameTypeStr() {
		switch (gameType) {
		case SERVER: return "SERVER";
		case CLIENT: return "CLIENT";
		case SINGLE_PLAYER: return "SINGLE_PLAYER";
		default: return null;
		}
	}

}
