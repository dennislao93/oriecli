package game;

import main.Constants;
import actions.Action;

public class UserListener {

	private Action action;
	private GameType gameType;
	private long actionTimer;

	public UserListener(GameType gameType) {
		this.gameType = gameType;
		action = null;
		actionTimer = System.currentTimeMillis();
	}

	public synchronized void setAction(Action action) {
		if (System.currentTimeMillis() - actionTimer > Constants.ACTION_COOLDOWN) {
			this.action = action;
			actionTimer = System.currentTimeMillis();
		}
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
