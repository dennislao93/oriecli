package display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import main.Constants;
import map.Tile;
import game.Game;
import game.SettlementType;
import game.UserListener;

public class Dashboard extends JPanel {

	private UserListener userListener;
	private SettlementType settlement;
	static Game game;
	static Screen screen;
	public static Mode mode;
	static boolean showGrid;
	private MouseListener mouseListener = new MouseListener() {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (!game.isPaused()) {
				screen.handleMouseClickEvent(e);
			}
		}
		@Override
		public void mousePressed(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) {}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
	};
	private MouseMotionListener mouseMotionListener = new MouseMotionListener() {
		@Override
		public void mouseDragged(MouseEvent arg0) {	}
		@Override
		public void mouseMoved(MouseEvent e) {
			screen.handleMouseMoveEvent(e);
		}
	};

	public Dashboard(SettlementType settlement, Game game) {
		this.game = game;
		setPreferredSize(new Dimension(Constants.DISPLAY_WIDTH, Constants.DASHBOARD_HEIGHT));
		this.settlement = settlement;
		screen = new MainMenu(this);
		mode = Mode.MAIN_MENU;
		showGrid = false;
		addMouseListener(mouseListener);
		addMouseMotionListener(mouseMotionListener);
	}

	public void setUserListener(UserListener userListener) {
		this.userListener = userListener;
	}

	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawRect(10, 0, getWidth() - 20, getHeight() - Constants.SLOW_SCROLL_THRESHOLD);
		screen.makeDashBoard(g);
	}

	public static abstract class Screen {
		Dashboard dashboard;
		DashboardButton[] buttons;
		public Screen(Dashboard dashboard) {
			this.dashboard = dashboard;
			Constants.mainFrame.repaint();
		}
		public abstract void makeDashBoard(Graphics g);
		public abstract void handleMouseClickEvent(MouseEvent e);
		public void handleMouseMoveEvent(MouseEvent e) {
			for (DashboardButton button: buttons) {
				if (button.isMouseOver(e)) {
					button.rollOver = true;
				} else {
					button.rollOver = false;
				}
				Constants.mainFrame.repaint();
			}
		};
	}

	public static enum Mode {
		MAIN_MENU, BUILD_ROAD, REMOVE_ROAD, BUILD_ROAD_PROMPT
	}

	public static int[] checkBuildRoad(int x, int y) {
		return game.getMap().checkBuildRoad(x, y);
	}

	public static int[] checkRemoveRoad(int x, int y) {
		return game.getMap().checkRemoveRoad(x, y);
	}

	public void promptDirs(byte dirs) {
		screen = new BuildRoadPrompt(this, dirs);
	}

}
