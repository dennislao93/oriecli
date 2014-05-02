package game;

import java.util.Random;

import map.Map;
import display.Display;
import display.Displayable;
import actions.Action;

public class Game {
	
	private Display display;
	
	private boolean gameOver;
	private boolean paused = false;
	private Action[] updates = new Action[2];
	
	private Map map;
	
	private boolean isVictor;
	private boolean isForfeit;
	
	public static long randomSeed;
	public static Random random;

	public Game(int mapWidth, int mapHeight) {
		map = new Map(mapWidth, mapHeight);
	}
	
	public void initializeRandom() {
		random = new Random(randomSeed);
	}
	
	public void setDisplay(Display display) {
		this.display = display;
		map.display = display;
	}

	public boolean gameOver() {
		return gameOver;
	}

	public void processAction(Action action) {
		action.processAction(this);
	}

	public void processUpdates(Action[] updates) {
		if (updates[0] != null) updates[0].processAction(this);
		if (updates[1] != null) updates[1].processAction(this);
	}

	public void addUpdate(Action action) {
		updates[updates[0] == null ? 0 : 1] = action;
	}

	public Action[] getUpdates() {
		return updates;
	}
	
	public void clearUpdates() {
		updates[0] = updates[1] = null;
	}
	
	public Displayable[] getDisplayableObjects() {
		//TODO
		return null;
	}

	public boolean isPaused() {
		return paused;
	}
	
	public void togglePause() {
		paused = !paused;
		if (paused) {
			display.displayPause();
		} else {
			display.displayResume();
		}
	}
	
	public void waitForResume() {
		display.showWaitingResume();
	}

	public void promptResume() {
		display.showResumePrompt();
	}

	public void denyResume() {
		display.onResumeDenied();
	}
	
	public void setIsVictor(boolean isVictor) {
		this.isVictor = isVictor;
		gameOver = true;
	}

	public boolean getIsVictor() {
		return isVictor;
	}
	
	public void setIsForfeit(boolean isForfeit) {
		this.isForfeit = isForfeit;
	}

	public boolean getIsForfeit() {
		return isForfeit;
	}
	
	public Map getMap() {
		return map;
	}

	public void step() {
		
	}

}
