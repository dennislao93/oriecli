import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;


public class TestUDP {

	public static void main(String[] args) {
		TestUDP test = new TestUDP();
		if (args[0].equals("s")) {
			test.execute('s');
		} else if (args[0].equals("c")) {
			test.execute('c');
		}
	}

	public void execute(char c) {
		if (c == 's') {
			new ServerThread().start();
		} else {
			new ClientThread().start();
		}
	}

	public class ServerThread extends Thread {
		@Override
		public void run() {
			DatagramSocket socket;
			DatagramPacket packet;
			try {
				socket = new DatagramSocket(5822);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			byte[] b;
			String msg;
			boolean run = true;
			while (run) {
				try {
					b = new byte[32];
					packet = new DatagramPacket(b, b.length);
					socket.receive(packet);
					byte[] bytes = packet.getData();
					msg = new String(bytes);
					System.out.println("Length:" + msg.length() + ", num bytes: " + bytes.length);
					System.out.println(msg);
					System.out.println(new Integer(msg.charAt(29)));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					break;
				}
				if (msg.trim().equals("stop")) {
					run = false;
				}
			}
		}
	}

	public class ClientThread extends Thread {
		@Override
		public void run() {
			DatagramSocket socket;
			DatagramPacket packet;
			try {
				socket = new DatagramSocket(5823);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			byte[] b;
			Scanner in = new Scanner(System.in);
			boolean run = true;
			String msg;
			while (run) {
				msg = in.nextLine();
				b = msg.getBytes();
				try {
					packet = new DatagramPacket(b, b.length, InetAddress.getByName("localhost"), 5822);
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					break;
				}
				try {
					socket.send(packet);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					break;
				}
				if (msg.equals("stop")) {
					run = false;
				}
			}
		}
	}


}
