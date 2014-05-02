package main;

import java.awt.Container;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JLabel;

public class Library {

	private static JLabel loadingImages = new JLabel("Loading images...");
	public static boolean doneLoading;
	
	public static Image moonsurface;
	public static Image road;
	public static Image linkImgN;
	public static Image linkImgS;
	public static Image linkImgE;
	public static Image linkImgW;

	public static void loadResources(final Container content) throws IOException {
		new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					moonsurface = ImageIO.read(ClassLoader.getSystemResourceAsStream("images/moonsurface.png"));
					road = ImageIO.read(ClassLoader.getSystemResourceAsStream("images/road.png"));
					linkImgN = ImageIO.read(ClassLoader.getSystemResourceAsStream("images/roadLinkNorth.png"));
					linkImgS = ImageIO.read(ClassLoader.getSystemResourceAsStream("images/roadLinkSouth.png"));
					linkImgE = ImageIO.read(ClassLoader.getSystemResourceAsStream("images/roadLinkEast.png"));
					linkImgW = ImageIO.read(ClassLoader.getSystemResourceAsStream("images/roadLinkWest.png"));
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
