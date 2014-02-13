package main;

import game.Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import actions.Action;

public class NetLayer {

	boolean isUdp;
	boolean isServer;

	private InetAddress address;
	private int length = 32;
	private byte[] buf;
	private int remotePort;

	private DatagramSocket socket;
	private DatagramPacket sendPacket;
	private DatagramPacket receivePacket;
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private PrintWriter out;
	private BufferedReader in;

	private String[] dataReceived;
	private byte[] dataToSend;
	private StringBuilder updatesSB;
	private String[] updatesStr = new String[2];

	private Action action = null;
	private Action[] updates = new Action[2];

	private Timer timer = new Timer();
	private TimerTask timerTask;
	public IOException ioException = null;
	
	private final String ACK = "H+\"D%O;<f6%6o:wQE";
	private final String BLANKSTRING = "_";
	private final int MAX_UDP_RETRIES = 2;
	private final int UDP_RETRY_INTERVAL = 200; //ms

	public NetLayer(boolean isServer, String inetAddressStr, int remotePort, int listenPort) throws UnknownHostException, SocketException {
		address = InetAddress.getByName(inetAddressStr);
		this.remotePort = remotePort;
		socket = new DatagramSocket(listenPort);
		isUdp = true;
		this.isServer = isServer;
	}

	public NetLayer(int listenPort) throws UnknownHostException, IOException {
		serverSocket = new ServerSocket(listenPort);
	}

	public NetLayer(String inetAddressStr, int remotePort) throws UnknownHostException {
		address = InetAddress.getByName(inetAddressStr);
		this.remotePort = remotePort;
	}

	public void waitConnection(boolean isUdp, boolean isServer) throws IOException {
		if (isUdp) {
			if (isServer) {
				Debug.print('n', "receive) waiting for client connection... ");
				buf = new byte[length];
				receivePacket = new DatagramPacket(buf, length);
				socket.receive(receivePacket);
				String connTestStr = new String(receivePacket.getData());
				if (!connTestStr.trim().equals(ACK)) {
					throw new IOException("error");
				}
				Debug.println('n', "received connection request.");
				Game.randomSeed = (long)(Math.random() * 42);
				byte[] connTestData = ("" + Game.randomSeed).getBytes();
				sendPacket = new DatagramPacket(connTestData, connTestData.length, address, remotePort);
				Debug.println('n', "send) sending random seed: " + Game.randomSeed);
				socket.send(sendPacket);
			} else {
				byte[] connTestData = ACK.getBytes();
				sendPacket = new DatagramPacket(connTestData, connTestData.length, address, remotePort);
				Debug.println('n', "send) synching with server.");
				socket.send(sendPacket);
				Debug.print('n', "receive) waiting for random seed... ");
				buf = new byte[length];
				receivePacket = new DatagramPacket(buf, length);
				socket.receive(receivePacket);
				String connTestStr = new String(receivePacket.getData());
				Game.randomSeed = Long.parseLong(connTestStr.trim());
				Debug.println('n', "received random seed: " + Game.randomSeed);
			}
		} else {
			if (isServer) {
				Debug.println('n', "accept) waiting for client connection... ");
				clientSocket = serverSocket.accept();
			} else {
				Debug.println('n', "connect) synching with server.");
				clientSocket = new Socket(address, remotePort);
			}
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			if (isServer) {
				Game.randomSeed = (long)(Math.random() * 42);
				Debug.println('n', "send) sending random seed: " + Game.randomSeed);
				out.println("" + Game.randomSeed);
			} else {
				Debug.print('n', "receive) waiting for random seed... ");
				Game.randomSeed = Long.parseLong(in.readLine());
				Debug.println('n', "received random seed: " + Game.randomSeed);
			}
		}
	}

	public void receiveAction() {
		try {
			Debug.print('n', "receive " + Debug.cycleNum + ") action: ");
			if (isUdp) {
				buf = new byte[length];
				receivePacket = new DatagramPacket(buf, length);
				timer.scheduleAtFixedRate((timerTask = getTimerTask()), UDP_RETRY_INTERVAL, UDP_RETRY_INTERVAL);
				socket.receive(receivePacket);
				timerTask.cancel();
			}
			if (action == null) {
				String receivedStr = isUdp ? new String(receivePacket.getData()).trim() : in.readLine();
				Debug.println('n', receivedStr);
				dataReceived = receivedStr.split(":");
				if (dataReceived[0].length() == 3) {
					action = Action.instantiate(dataReceived[0], dataReceived[1].split(","));
				}
			}
		} catch (IOException e) {
			this.ioException = e;
		}
	}

	public Action getAction() {
		return action;
	}

	public void clearAction() {
		action = null;
	}

	public void sendAction(Action action) {
		try {
			Debug.println('n', "send " + Debug.cycleNum + ") action: " + (action == null ? "null" : action.toString()));
			if (isUdp) {
				dataToSend = action == null ? new byte[]{} : action.toString().getBytes();
				sendPacket = new DatagramPacket(dataToSend, dataToSend.length, address, remotePort);
				socket.send(sendPacket);
			} else {
				out.println(action == null ? "" : action.toString());
			}
		} catch (IOException e) {
			this.ioException = e;
		}
	}

	public void sendUpdates(Action[] updates) {
		try {
			updatesSB = new StringBuilder();
			updatesSB.append(updates[0] != null ? updates[0].toString() : BLANKSTRING).append(';');
			updatesSB.append(updates[1] != null ? updates[1].toString() : BLANKSTRING).append(';');
			Debug.println('n', "send " + Debug.cycleNum + ") updates: [" + updatesSB.toString() + "]");
			if (isUdp) {
				dataToSend = updatesSB.toString().getBytes();
				sendPacket = new DatagramPacket(dataToSend, dataToSend.length, address, remotePort);
				socket.send(sendPacket);
			} else {
				out.println(updatesSB.toString());
			}
		} catch (IOException e) {
			this.ioException = e;
		}
	}

	public void receiveUpdates() {
		try {
			Debug.print('n', "receive " + Debug.cycleNum + ") updates: [");
			if (isUdp) {
				buf = new byte[length * 2];
				receivePacket = new DatagramPacket(buf, length);
				timer.scheduleAtFixedRate((timerTask = getTimerTask()), UDP_RETRY_INTERVAL, UDP_RETRY_INTERVAL);
				socket.receive(receivePacket);
				timerTask.cancel();
			}
			updatesStr = (isUdp ? new String(receivePacket.getData()).trim() : in.readLine()).split(";");
			Debug.println('n', updatesStr[0] + ";" + updatesStr[1] + ";]");
			if (!updatesStr[0].equals(BLANKSTRING)) {
				dataReceived = updatesStr[0].split(":");
				updates[0] = Action.instantiate(dataReceived[0], dataReceived[1].split(","));
			}
			if (!updatesStr[1].equals(BLANKSTRING)) {
				dataReceived = updatesStr[1].split(":");
				updates[1] = Action.instantiate(dataReceived[0], dataReceived[1].split(","));
			}
		} catch (IOException e) {
			this.ioException = e;
		}
	}
	
	public Action[] getUpdates() {
		return updates;
	}

	public void clearUpdates() {
		updates[0] = updates[1] = null;
	}

	private TimerTask getTimerTask() {
		return new TimerTask() {
			private int invoked = 0;
			@Override
			public void run() {
				if (++invoked > MAX_UDP_RETRIES) {
					Debug.println('n', "lost the connection");
					ioException = new IOException("lost the connection");
					if (socket != null) socket.close();
					this.cancel();
					return;
				}
				try {
					socket.send(sendPacket);
				} catch (IOException e1) {
					ioException = e1;
				}
			}
		};
	}

	public void cancelTimer() {
		timer.cancel();
	}
}
