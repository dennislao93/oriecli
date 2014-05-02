package display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import objects.Roads;
import actions.BuildRoad;
import actions.Forfeit;
import actions.Pause;
import actions.RemoveRoad;
import actions.Resume;
import main.Constants;
import main.Library;
import main.MainFrame;
import game.UserListener;

public class Display extends JPanel {

	private JLayeredPane layeredPane;

	private MainFrame mainFrame;
	public Dashboard dashboard;
	private UserListener userListener;
	private int xOffset;
	private int yOffset;
	private int xTiles;
	private int yTiles;
	boolean initialized;
	private int rightDistance;
	private int downDistance;

	private int tw = Constants.TILE_WIDTH;
	private Point mousePos;
	private int mousePosX;
	private int mousePosY;
	private final int UP = 0;
	private final int DOWN = 1;
	private final int LEFT = 2;
	private final int RIGHT = 3;
	private boolean[] scrollSwitches = new boolean[]{false, false, false, false}; // up down left right
	private boolean[] scrollSpeeds = new boolean[] {false, false, false, false};
	private boolean[] keyScrollSwitches = new boolean[]{false, false, false, false};
	private int[] buildRoadTile;
	private int[] removeRoadTile;

	private GroundPnl groundPnl = new GroundPnl();
	private BuildingBasesPnl buildingBasesPnl = new BuildingBasesPnl();
	private UnitsShotsPnl unitsShotsPnl = new UnitsShotsPnl();
	private HitsPnl hitsPnl = new HitsPnl();
	private BuildingTopsPnl buildingTopsPnl = new BuildingTopsPnl();
	private GridPnl gridPnl = new GridPnl();
	private Integer noticeLayer = new Integer(6);
	private boolean paused = false;
	private boolean isPromptingResume = false;
	private boolean isWaitingResume = false;
	private boolean gameover;

	private KeyListener keyListener = new KeyListener() {
		@Override
		public void keyTyped(KeyEvent e) {
			switch (e.getKeyChar()) {
			case 'p': // pause
				if (!paused) {
					userListener.setAction(new Pause(new String[]{userListener.getGameTypeStr()}));
				} else if (!isWaitingResume && !isPromptingResume) {
					userListener.setAction(new Resume(new String[]{userListener.getGameTypeStr(), "init"}));
				} break;
			case 'f': //forfeit
				if (!paused) {
					userListener.setAction(new Forfeit(new String[]{userListener.getGameTypeStr()}));
				}
			}
		}
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case 65: keyScrollSwitches[LEFT] = true; break; // a
			case 68: keyScrollSwitches[RIGHT] = true; break; // d
			case 87: keyScrollSwitches[UP] = true; break; // w
			case 83: keyScrollSwitches[DOWN] = true; break; // s
			}
			for (int i = 0; i < scrollSpeeds.length; i++) {
				scrollSpeeds[i] = true;
			}
		}
		@Override
		public void keyReleased(KeyEvent e) {
			switch (e.getKeyCode()) {
			case 65: keyScrollSwitches[LEFT] = false; break;
			case 68: keyScrollSwitches[RIGHT] = false; break;
			case 87: keyScrollSwitches[UP] = false; break;
			case 83: keyScrollSwitches[DOWN] = false; break;
			}
		}
	};
	
	private MouseMotionListener mouseMotionListener = new MouseMotionListener() {
		@Override
		public void mouseDragged(MouseEvent arg0) {}
		@Override
		public void mouseMoved(MouseEvent e) {
			if (Dashboard.mode == Dashboard.Mode.BUILD_ROAD) {
				int x = (e.getX() - xOffset) / tw;
				int y = (e.getY() - yOffset) / tw;
				buildRoadTile = Dashboard.checkBuildRoad(x, y);
			} else {
				buildRoadTile = null;
			}
			if (Dashboard.mode == Dashboard.Mode.REMOVE_ROAD) {
				int x = (e.getX() - xOffset) / tw;
				int y = (e.getY() - yOffset) / tw;
				removeRoadTile = Dashboard.checkRemoveRoad(x, y);
			} else {
				removeRoadTile = null;
			}
		}
	};
	
	private MouseListener mouseListener = new MouseListener() {
		@Override
		public void mouseClicked(MouseEvent arg0) {
			if (paused) return;
			if (buildRoadTile != null) {
				userListener.setAction(new BuildRoad(new String[]{"" + buildRoadTile[0], "" + buildRoadTile[1]}));
				buildRoadTile = null;
			} else if (removeRoadTile != null) {
				userListener.setAction(new RemoveRoad(new String[]{"" + removeRoadTile[0], "" + removeRoadTile[1]}));
				removeRoadTile = null;
			}
		}
		@Override
		public void mouseEntered(MouseEvent arg0) {}
		@Override
		public void mouseExited(MouseEvent arg0) {}
		@Override
		public void mousePressed(MouseEvent arg0) {}
		@Override
		public void mouseReleased(MouseEvent arg0) {}
	};

	private JPanel pauseNotice = new JPanel() {
		@Override
		public void paint(Graphics g) {
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(Color.BLACK);
			g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
			g.drawString("Game paused (P to resume)", 12, 30);
		}
	};
	private JPanel resumePrompt = new JPanel();
	private JLabel resumePromptLabel = new JLabel("Your partner would like to resume.");
	private JButton confirmResume = new JButton("Confirm");
	private JButton denyResume = new JButton("Deny");
	private JPanel awaitingResumeNotice = new JPanel() {
		@Override
		public void paint(Graphics g) {
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(Color.BLACK);
			g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
			g.drawString("Please wait...", 12, 30);
		}
	};
	private JPanel disconnectedNotice = new JPanel() {
		@Override
		public void paint(Graphics g) {
			g.setColor(new Color(220, 180, 180));
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(Color.BLACK);
			g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
			g.drawString("Lost connection.", 12, 30);
		}
	};
	private JPanel gameOverNotice = new JPanel();

	public Display(final UserListener userListener, MainFrame mainFrame, final Dashboard dashboard, final int xTiles, final int yTiles) {
		this.userListener = userListener;
		this.dashboard = dashboard;
		this.mainFrame = mainFrame;
		this.xTiles = xTiles;
		this.yTiles = yTiles;
		initialized = false;
		setLayout(new BorderLayout());
		layeredPane = new JLayeredPane();
		add(layeredPane);
		layeredPane.add(groundPnl, new Integer(0));
		layeredPane.add(buildingBasesPnl, new Integer(1));
		layeredPane.add(unitsShotsPnl, new Integer(2));
		layeredPane.add(hitsPnl, new Integer(3));
		layeredPane.add(buildingTopsPnl, new Integer(4));
		layeredPane.add(gridPnl, new Integer(5));
		paused = false;
		addComponentListener(new ComponentListener(){
			@Override
			public void componentResized(ComponentEvent e) {
				if (!initialized) {
					xOffset = getWidth() / 2 - tw * xTiles / 2;
					rightDistance = getWidth() - tw * xTiles - xOffset;
					yOffset = getHeight() - tw * yTiles;
					downDistance = getHeight() - tw * yTiles - yOffset;
					initialized = true;
				} else {
					if (getWidth() > tw * xTiles) {
						xOffset = getWidth() / 2 - tw * xTiles / 2;
					} else {
						xOffset += getWidth() - tw * xTiles - xOffset - rightDistance;
						if (xOffset > Constants.MAX_OFFSET) {
							xOffset = Constants.MAX_OFFSET;
						}
					}
					if (getHeight() > tw * yTiles) {
						yOffset = getHeight() - tw * yTiles;
					} else {
						yOffset += getHeight() - tw * yTiles - yOffset - downDistance;
						if (yOffset > Constants.MAX_OFFSET * 3) {
							yOffset = Constants.MAX_OFFSET * 3;
						}
					}
				}
				formatComponents();
				repaint();
			}
			@Override
			public void componentMoved(ComponentEvent e) {}
			@Override
			public void componentShown(ComponentEvent e) {}
			@Override
			public void componentHidden(ComponentEvent e) {}
		});
		mainFrame.addKeyListener(keyListener);
		groundPnl.addMouseMotionListener(mouseMotionListener);
		groundPnl.addMouseListener(mouseListener);
		new Thread() {
			@Override
			public void run() {
				while (!gameover) {
					if (!paused) {
						processScroll();
						scroll();
						repaint();
						try {
							Thread.sleep(Constants.WAIT_TIME / 2);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					Thread.yield();
				}
			}
		}.start();
		resumePrompt.setSize(new Dimension(220, 60));
		resumePromptLabel.setSize(new Dimension(220, 30));
		resumePrompt.add(resumePromptLabel);
		confirmResume.setSize(new Dimension(80, 20));
		resumePrompt.add(confirmResume);
		denyResume.setSize(new Dimension(80, 20));
		resumePrompt.add(denyResume);
		pauseNotice.setSize(new Dimension(180, 60));
		awaitingResumeNotice.setSize(new Dimension(180, 60));
		disconnectedNotice.setSize(new Dimension(180, 60));
		confirmResume.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				userListener.setAction(new Resume(new String[]{userListener.getGameTypeStr(), "confirm"}));
			}
		});
		denyResume.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				userListener.setAction(new Resume(new String[]{userListener.getGameTypeStr(), "deny"}));
			}
		});
	}

	private void formatComponents() {
		centerPanel(pauseNotice);
		centerPanel(resumePrompt);
		centerPanel(awaitingResumeNotice);
		centerPanel(disconnectedNotice);
	}

	private void centerPanel(JPanel panel) {
		panel.setLocation(getWidth() / 2 - panel.getWidth() / 2, getHeight() / 2 - panel.getHeight() / 2);
	}

	public void gameover() {
		gameover = true;
	}

	public void processScroll() {
		mousePos = mainFrame.getMousePosition();
		if (mousePos != null) {
			mousePosX = (int)mousePos.getX();
			mousePosY = (int)mousePos.getY();
			if (mousePosX < Constants.SLOW_SCROLL_THRESHOLD) {
				scrollSwitches[LEFT] = true;
				scrollSwitches[RIGHT] = false;
				scrollSpeeds[LEFT] = mousePosX < Constants.FAST_SCROLL_THRESHOLD ? true : false;
			} else if (mousePosX >= getWidth() - Constants.SLOW_SCROLL_THRESHOLD) {
				scrollSwitches[RIGHT] = true;
				scrollSwitches[LEFT] = false;
				scrollSpeeds[RIGHT] = mousePosX >= getWidth() - Constants.FAST_SCROLL_THRESHOLD ? true : false;
			} else {
				scrollSwitches[LEFT] = false;
				scrollSwitches[RIGHT] = false;
			}
			if (mousePosY < Constants.SLOW_SCROLL_THRESHOLD) {
				scrollSwitches[UP] = true;
				scrollSwitches[DOWN] = false;
				scrollSpeeds[UP] = mousePosY < Constants.FAST_SCROLL_THRESHOLD ? true : false;
			} else if (mousePosY >= mainFrame.getHeight() - Constants.SLOW_SCROLL_THRESHOLD) {
				scrollSwitches[DOWN] = true;
				scrollSwitches[UP] = false;
				scrollSpeeds[DOWN] = mousePosY >= mainFrame.getHeight() - Constants.FAST_SCROLL_THRESHOLD ? true : false;
			} else {
				scrollSwitches[UP] = false;
				scrollSwitches[DOWN] = false;
			}
		}
	}

	public void step(Displayable[] displayObjects) {

	}

	private void scroll() {
		for (int i = 0; i < 4; i++) {
			if (keyScrollSwitches[UP] == false && keyScrollSwitches[DOWN] == false && keyScrollSwitches[LEFT] == false && keyScrollSwitches[RIGHT] == false ? scrollSwitches[i] : keyScrollSwitches[i]) {
				switch (i) {
				case DOWN:
					if (yOffset >= getHeight() - tw * yTiles - Constants.MAX_OFFSET * 3) {
						yOffset -= scrollSpeeds[i] ? Constants.FAST_SCROLL_SPEED : Constants.SLOW_SCROLL_SPEED;
						downDistance = getHeight() - tw * yTiles - yOffset;
					}
					break;
				case LEFT:
					if (xOffset < Constants.MAX_OFFSET) {
						xOffset += scrollSpeeds[i] ? Constants.FAST_SCROLL_SPEED : Constants.SLOW_SCROLL_SPEED;
						rightDistance = getWidth() - tw * xTiles - xOffset;
					}
					break;
				case RIGHT:
					if (xOffset >= getWidth() - tw * xTiles - Constants.MAX_OFFSET) {
						xOffset -= scrollSpeeds[i] ? Constants.FAST_SCROLL_SPEED : Constants.SLOW_SCROLL_SPEED;
						rightDistance = getWidth() - tw * xTiles - xOffset;
					}
					break;
				case UP:
					if (yOffset < Constants.MAX_OFFSET * 3) {
						yOffset += scrollSpeeds[i] ? Constants.FAST_SCROLL_SPEED : Constants.SLOW_SCROLL_SPEED;
						downDistance = getHeight() - tw * yTiles - yOffset;
					}
					break;
				}
			}
		}
	}

	private class DisplayPanel extends JPanel {

		private DisplayPanel() {
			this.setOpaque(false);
			this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		}

	}

	private int fromX;
	private int fromY;
	private int toX;
	private int toY;
	private class GroundPnl extends DisplayPanel {
		@Override
		public void paintComponent(Graphics g) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.drawImage(Library.moonsurface, xOffset, yOffset, tw * xTiles, tw * yTiles, null);
			for (Integer[] roadLocs: Roads.locs) {
				g.drawImage(Library.road, roadLocs[0] * tw + xOffset, roadLocs[1] * tw + yOffset, null);
			}
			for (int i = 0; i < Roads.linkFrom.size(); i++) {
				fromX = Roads.linkFrom.get(i)[0];
				fromY = Roads.linkFrom.get(i)[1];
				toX = Roads.linkTo.get(i)[0];
				toY = Roads.linkTo.get(i)[1];
				if (fromX < toX) {
					g.drawImage(Library.linkImgE, fromX * tw + xOffset + 48, fromY * tw + yOffset + 12, null);
				} else if (fromX > toX) {
					g.drawImage(Library.linkImgW, fromX * tw + xOffset - 16, fromY * tw + yOffset + 12, null);
				} else if (fromY < toY) {
					g.drawImage(Library.linkImgS, fromX * tw + xOffset + 12, fromY * tw + yOffset + 48, null);
				} else if (fromY > toY) {
					g.drawImage(Library.linkImgN, fromX * tw + xOffset + 12, fromY * tw + yOffset - 16, null);
				}
			}
			if (buildRoadTile != null) {
				g.setColor(Color.GREEN);
				g.fillRect(buildRoadTile[0] * tw + xOffset, buildRoadTile[1] * tw + yOffset, tw, tw);
			} else if (removeRoadTile != null) {
				g.setColor(Color.RED);
				g.fillRect(removeRoadTile[0] * tw + xOffset, removeRoadTile[1] * tw + yOffset, tw, tw);
			}
		}
	}

	private class BuildingBasesPnl extends DisplayPanel {
		public void paintComponent(Graphics g) {
			//TODO
		}
	}

	private class UnitsShotsPnl extends DisplayPanel {
		public void paintComponent(Graphics g) {
			//TODO
		}
	}

	private class HitsPnl extends DisplayPanel {
		public void paintComponent(Graphics g) {
			//TODO
		}
	}

	private class BuildingTopsPnl extends DisplayPanel {
		public void paintComponent(Graphics g) {
			//TODO
		}
	}

	private class GridPnl extends DisplayPanel {
		public void paintComponent(Graphics g) {
			if (Dashboard.showGrid) {
				g.setColor(Color.BLACK);
				for (int i = 0; i < xTiles; i++) {
					for (int j = 0; j < yTiles; j++) {
						g.drawRect(i * tw + xOffset, j * tw + yOffset, tw, tw);
					}
				}
			}
		}
	}

	public void displayPause() {
		paused = true;
		layeredPane.add(pauseNotice, noticeLayer);
	}

	public void showResumePrompt() {
		isPromptingResume = true;
		layeredPane.remove(pauseNotice);
		layeredPane.add(resumePrompt, noticeLayer);
		layeredPane.repaint();
	}

	public void showWaitingResume() {
		isWaitingResume = true;
		layeredPane.remove(pauseNotice);
		layeredPane.add(awaitingResumeNotice, noticeLayer);
		layeredPane.repaint();
	}

	public void displayResume() {
		paused = false;
		if (isWaitingResume) {
			layeredPane.remove(awaitingResumeNotice);
			isWaitingResume = false;
		} else if (isPromptingResume) {
			layeredPane.remove(resumePrompt);
			isPromptingResume = false;
		} else {
			layeredPane.remove(pauseNotice);
		}
		mainFrame.requestFocusInWindow();
	}

	public void onResumeDenied() {
		if (isWaitingResume) {
			isWaitingResume = false;
			layeredPane.remove(awaitingResumeNotice);
		} else {
			isPromptingResume = false;
			layeredPane.remove(resumePrompt);
		}
		layeredPane.add(pauseNotice, noticeLayer);
		layeredPane.repaint();
		mainFrame.requestFocusInWindow();
	}

	public void disconnected() {
		paused = true;
		layeredPane.add(disconnectedNotice, noticeLayer);
	}

	public void showGameOver(final boolean isForfeit, final boolean isVictor) {
		paused = true;
		gameOverNotice = new JPanel() {
			@Override
			public void paint(Graphics g) {
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, getWidth(), getHeight());
				g.setColor(Color.BLACK);
				g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
				g.drawString("GAME OVER", 48, 30);
				g.drawString((isVictor ? (isForfeit ? "The other side forfeits!" : "You are victorious!") : "The other side is victorious!"), 12, 48);
			}
		};
		layeredPane.add(gameOverNotice, noticeLayer);
		gameOverNotice.setSize(new Dimension(256, 72));
		centerPanel(gameOverNotice);
	}
}
