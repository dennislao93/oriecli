package display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import main.Constants;

import game.SettlementType;
import game.UserListener;

public class Dashboard extends JPanel {
	
	private Mouse mouse;
	private UserListener userListener;
	private SettlementType settlement;
	
	public Dashboard(SettlementType settlement) {
		setPreferredSize(new Dimension(Constants.DISPLAY_WIDTH, Constants.DASHBOARD_HEIGHT));
		this.settlement = settlement;
	}

	public void setMouse(Mouse mouse) {
		this.mouse = mouse;
	}

	public void setUserListener(UserListener userListener) {
		this.userListener = userListener;
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawRect(10, 0, getWidth() - 20, getHeight() - Constants.SLOW_SCROLL_THRESHOLD);
	}

}
