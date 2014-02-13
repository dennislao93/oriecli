package main;

import java.awt.Container;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.locks.Lock;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

public class Library {

	private static JLabel loadingImages = new JLabel("Loading images...");
	public static boolean doneLoading;
	
	public static Image moonsurface;
	public static MainFrame mainFrame;

	public static void loadResources(final Container content) throws IOException {
		new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					//moonsurface = ImageIO.read(new File("Images/moonsurface.png"));
					moonsurface = ImageIO.read(ClassLoader.getSystemResourceAsStream("Images/moonsurface.png"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				doneLoading = true;
			}
		}).start();
		content.add(loadingImages);
		content.validate();
		while (!doneLoading) {
			Thread.yield();
		}
		content.remove(loadingImages);
		content.validate();
	}

}
