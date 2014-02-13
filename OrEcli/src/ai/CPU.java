package ai;

import game.SettlementType;
import actions.Action;

public class CPU {
	
	private Action action;
	private SettlementType settlement;
	
	public CPU(SettlementType settlement) {
		action = null;
		this.settlement = settlement;
	}
	
	private void setAction(Action action) {
		this.action = action;
	}

	public Action getAction() {
		Action action1 = action;
		action = null;
		return action1;
	}

	public void step() {
		// TODO Auto-generated method stub
		
	}

}
