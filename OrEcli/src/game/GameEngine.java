package game;

import display.Display;
import main.Constants;
import main.Debug;
import main.NetLayer;
import actions.Action;
import ai.CPU;

/**
 * @author Dennis
 * processes actions and updates of the game
 */
public class GameEngine {

	private NetLayer netLayer;
	private Game game;
	private Display display;
	private UserListener listener;
	private GameType gameType;
	private SettlementType cpuSettlement;
	private boolean connected;
	private final int NOTICE_DELAY = 5000; //ms

	public void setup(Game game, GameType gameType, UserListener listener, Display display) {
		this.game = game;
		this.gameType = gameType;
		this.listener = listener;
		this.display = display;
		connected = true;
	}

	public void setNetLayer(NetLayer udp) {
		this.netLayer = udp;
	}

	public void setCPUSettlement(SettlementType settlement) {
		cpuSettlement = settlement;
	}

	public Game run() {
		try {
			game.initializeRandom();
			Action[] actions = new Action[2];
			Action userAction;
			CPU cpu = null;
			if (gameType == GameType.SINGLE_PLAYER) {
				cpu = new CPU(cpuSettlement);
			}
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						while (connected) {
							if (!game.isPaused()) {
								display.step(game.getDisplayableObjects());
							}
							if (game.gameOver()) {
								break;
							}
							Thread.sleep(Constants.WAIT_TIME);
						}
					} catch (InterruptedException e) {
						//TODO
						e.printStackTrace();
					}
				}
			}).start();
			long cycleTimeKeeper = System.currentTimeMillis();
			long cycleTime;
			while (connected) {
				if (gameType == GameType.SERVER || gameType == GameType.SINGLE_PLAYER) {
					// SERVER / SINGLE_PLAYER
					actions[0] = listener.getAction();
					if (gameType == GameType.SERVER) {
						// SERVER
						netLayer.receiveAction();
						actions[1] = netLayer.getAction();
						netLayer.clearAction();
					} else {
						// SINGLE_PLAYER
						if (!game.isPaused()) {
							cpu.step();
							actions[1] = cpu.getAction();
						}
					}
					// SERVER / SINGLE_PLAYER
					if (actions[0] != null) game.processAction(actions[0]);
					if (actions[1] != null) game.processAction(actions[1]);
				} else {
					// CLIENT
					userAction = listener.getAction();
					netLayer.sendAction(userAction);
					netLayer.receiveUpdates();
					game.processUpdates(netLayer.getUpdates());
					netLayer.clearUpdates();
				}
				// SERVER / SINGLE_PLAYER / CLIENT
				if (!game.isPaused()) {
					game.step();
				}
				if (gameType == GameType.SERVER) {
					netLayer.sendUpdates(game.getUpdates());
					game.clearUpdates();
				}
				// SERVER / SINGLE_PLAYER / CLIENT
				if (game.gameOver()) {
					netLayer.cancelTimer();
					break;
				}
				Debug.println('n', "Cycle time: " + (cycleTime = System.currentTimeMillis() - cycleTimeKeeper));
				Thread.sleep(cycleTime > Constants.WAIT_TIME ? 0 : Constants.WAIT_TIME - cycleTime);
				cycleTimeKeeper = System.currentTimeMillis();
				Debug.cycleNum++;
				if (gameType != GameType.SINGLE_PLAYER) checkIOException();
			}
			if (gameType != GameType.SINGLE_PLAYER && !connected) {
				display.disconnected();
			} else {
				display.showGameOver(game.getIsForfeit(), game.getIsVictor());
			}
			Thread.sleep(NOTICE_DELAY);
			return game;
		} catch (InterruptedException e) {
			//TODO
			e.printStackTrace();
			return null;
		}
	}

	private void checkIOException() {
		if (netLayer.ioException != null) {
			connected = false;
		}
	}
}
