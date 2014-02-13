import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JLabel;


public class TestMouseThread {

	public static void main(String[] args) {
		new TestMouseThread();
	}

	public TestMouseThread() {
		final JFrame frame = new JFrame();
		final JLabel lbl = new JLabel();
		final StringBuilder s = new StringBuilder();
		frame.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				s.append(" =) ");
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

		});
		frame.add(lbl);
		frame.setPreferredSize(new Dimension(550, 400));
		frame.pack();
		frame.setVisible(true);
		new Thread(){
			int i = 0;
			@Override
			public void run() {
				while (i < 3000000) {
					lbl.setText(i + s.toString());
					i++;
					try {
						Thread.sleep(65);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

}
