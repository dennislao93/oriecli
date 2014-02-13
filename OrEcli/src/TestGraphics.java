import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.Timer;


public class TestGraphics extends JFrame {
	
	public static void main(String[] args) throws Exception {
		new TestGraphics();
	}
	
	public TestGraphics() {
		JFrame frame  = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MyCanvas canvas = new MyCanvas();
		frame.add(canvas);
		canvas.setPreferredSize(new Dimension(400, 600));
		Listener listener = new Listener(canvas);
		frame.pack();
		frame.setVisible(true);
		Timer timer = new Timer(11, listener);
		timer.start();
	}
	
	private class MyCanvas extends Canvas {
		private int i;
		private int i2;
		private int i3;
		@Override
		public void paint(Graphics g)
		{
			g.setColor(Color.RED);
			g.drawRect(10 + i, 10, 100, 50);
			g.setColor(Color.GREEN);
			g.drawOval(10, 60 + i2, 50, 200);
			g.setColor(Color.BLUE);
			g.drawLine(50 + i3, 50 + i3, 100 + i3, 200 + i3);
		}
	}
	
	private class Listener implements ActionListener {
		private MyCanvas canvas;
		private Listener(MyCanvas canvas) {
			this.canvas = canvas;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			canvas.i--;
			canvas.i2--;
			canvas.i3--;
			canvas.repaint(0, 0, 120, 120);
		}
	}
	
 }
