package main;
import game.Game;
import game.GameEngine;
import game.GameType;
import game.SettlementType;
import game.Side;
import game.UserListener;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import display.Dashboard;
import display.Display;
import display.Mouse;

/**
 * @author Dennis
 * the main gui interface
 */
public class MainFrame extends JFrame {
	private Container content;

	public static void main(String[] args) throws IOException, InterruptedException {
		if (args.length > 0 && args[0].equals("debug")) {
			Debug.setDebug(true);
			Debug.setFlag(args[1]);
		}
		new MainFrame();
	}

	private MainFrame() throws IOException, InterruptedException {
		content = getContentPane();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(Constants.DISPLAY_WIDTH, Constants.DISPLAY_HEIGHT));
		content.setLayout(new BorderLayout());
		pack();
		setVisible(true);
		Library.mainFrame = this;
		Library.loadResources(content);
		play(GameType.SINGLE_PLAYER, SettlementType.YUEH_KUNG, SettlementType.TSUKI_NO_MIYAKO, null, MapSize.MIDSIZED);
		//makeNetworkChoice();
	}

	private void makeNetworkChoice() throws UnknownHostException, SocketException {
		final JPanel testPanel = new JPanel(new GridLayout(0,2));
		content.add(testPanel);
		final JButton udpButton = new JButton("UDP");
		testPanel.add(udpButton);
		testPanel.add(new JLabel("<html>recommended if both you and your partner's network addresses are accessible to each other</html>"));
		final JButton tcpButton = new JButton("TCP");
		testPanel.add(tcpButton);
		testPanel.add(new JLabel("<html>recommended if one player is in a private network and is inaccessible, but the other player is accessible</html>"));
		udpButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				makeClientServerChoice("UDP");
			}
		});
		tcpButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				makeClientServerChoice("TCP");
			}
		});
		validate();
	}

	protected void makeClientServerChoice(final String protocol) {
		content.removeAll();
		final JPanel testPanel = new JPanel(new GridLayout(0,2));
		content.add(testPanel);
		JButton serverButton = new JButton("Server");
		testPanel.add(serverButton);
		testPanel.add(new JLabel("<html>wait for your partner's connection!" + (protocol.equals("UDP") ? "" : " (make sure your network address is accessible to your partner)") +  "<html>"));
		JButton clientButton = new JButton("Client");
		testPanel.add(clientButton);
		testPanel.add(new JLabel("<html>connect with your partner!<html>"));
		serverButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					setTitle("Server");
					makePrePlay(GameType.SERVER, protocol);
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SocketException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		clientButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					setTitle("Client");
					makePrePlay(GameType.CLIENT, protocol);
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SocketException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		validate();
	}

	private void makePrePlay(final GameType gameType, final String protocol) throws UnknownHostException, SocketException {
		content.removeAll();
		final JPanel prePlayPnl = new JPanel();
		content.add(prePlayPnl);
		Box box = new Box(BoxLayout.Y_AXIS);
		prePlayPnl.add(box);
		JLabel enterInstr = new JLabel();
		box.add(enterInstr);
		final JTextField in = new JTextField(20);
		final JTextField in2 = new JTextField(4);
		box.add(in);
		if (protocol.equals("UDP")) {
			box.add(new JLabel("Enter a port number to listen to."));
			box.add(in2);
			in2.setText(gameType == GameType.SERVER ? "10799" : "10800");
		}
		JButton goButton = new JButton("Go");
		box.add(goButton);
		final JLabel status = new JLabel("Please wait...");
		box.add(status);
		status.setVisible(false);
		if (protocol.equals("UDP")) {
			enterInstr.setText("Enter your partner's address in the form [address:port].");
			in.setText(gameType == GameType.SERVER ? ":10800" : ":10799");
		} else {
			if (gameType == GameType.SERVER) {
				enterInstr.setText("Enter a port number to listen to.");
				in.setText("10800");
			} else {
				enterInstr.setText("Enter your partner's address in the form [address:port].");
				in.setText("10800");
			}
		}
		goButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						status.setVisible(true);
						NetLayer netLayer = null;
						if (protocol.equals("UDP")) {
							try {
								netLayer = new NetLayer(gameType == GameType.SERVER, in.getText().split(":")[0], Integer.parseInt(in.getText().split(":")[1]), Integer.parseInt(in2.getText()));
							} catch (NumberFormatException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (UnknownHostException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (SocketException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} else {
							if (gameType == GameType.SERVER) {
								try {
									netLayer = new NetLayer(Integer.parseInt(in.getText()));
								} catch (NumberFormatException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								} catch (UnknownHostException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							} else {
								try {
									String[] input;
									netLayer = new NetLayer((input = in.getText().split(":"))[0], Integer.parseInt(input[1]));
								} catch (NumberFormatException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								} catch (UnknownHostException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
						}
						try {
							netLayer.waitConnection(protocol.equals("UDP"), gameType == GameType.SERVER);
							play(gameType, null, null, netLayer, MapSize.MIDSIZED);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}).start();
			}
		});
		validate();
	}

	private void play(GameType gameType, SettlementType settlement, SettlementType cpuSettlement, NetLayer net, MapSize mapSize) {
		final GameEngine engine = new GameEngine();
		if (gameType != GameType.SINGLE_PLAYER) {
			playHelper(engine, gameType, settlement, mapSize, net);
			engine.setNetLayer(net);
			if (gameType == GameType.CLIENT) Constants.side = Side.PLAYER2;
		} else {
			playHelper(engine, GameType.SINGLE_PLAYER, settlement, mapSize, null);
			engine.setCPUSettlement(cpuSettlement);
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				Game game = engine.run();
				content.removeAll();
				content.repaint();
			}
		}).start();
		requestFocusInWindow();
	}

	private void playHelper(GameEngine engine, GameType gameType, SettlementType settlement, MapSize mapSize, NetLayer net) {
		Game game = null;
		int mapWidth = 0;
		int mapHeight = 0;
		switch (mapSize) {
		case MAMMOTH:
			mapWidth = Constants.MAMMOTH_MAP_WIDTH;
			mapHeight = Constants.MAMMOTH_MAP_HEIGHT;
			break;
		case MIDSIZED:
			mapWidth = Constants.MIDSIZED_MAP_WIDTH;
			mapHeight = Constants.MIDSIZED_MAP_HEIGHT;
			break;
		case MINISCULE:
			mapWidth = Constants.MINISCULE_MAP_WIDTH;
			mapHeight = Constants.MINISCULE_MAP_HEIGHT;
		}
		game = new Game(mapWidth, mapHeight);
		if (gameType != GameType.SINGLE_PLAYER) {
			engine.setNetLayer(net);
		}
		Dashboard dashboard = new Dashboard(settlement);
		dashboard.setPreferredSize(new Dimension(Constants.DISPLAY_WIDTH - 20, Constants.DASHBOARD_HEIGHT));
		Mouse mouse = new Mouse();
		dashboard.setMouse(mouse);
		UserListener userListener = new UserListener(gameType);
		dashboard.setUserListener(userListener);
		Display display = new Display(userListener, mouse, this, mapWidth, mapHeight);
		game.setDisplay(display);
		makePlayScreen(dashboard, display);
		engine.setup(game, gameType, userListener, display);
	}

	private void makePlayScreen(final Dashboard dashboard, Display display) {
		content.removeAll();
		content.add(display);
		JPanel dashboardContainer = new JPanel();
		dashboardContainer.add(dashboard);
		dashboardContainer.setLayout(new GridBagLayout());
		content.add(dashboardContainer, BorderLayout.SOUTH);
		dashboardContainer.validate();
		content.validate();
	}

}
