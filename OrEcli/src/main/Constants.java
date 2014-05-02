package main;
import game.Side;

public class Constants {
	
	public static MainFrame mainFrame;
	
	public static Side side = Side.PLAYER1;
	
	public static int DISPLAY_WIDTH = 640;
	public static int DISPLAY_HEIGHT = 480;
	public static final int WAIT_TIME = 64; // 64
	public static final int TILE_WIDTH = 64;
	public static int DASHBOARD_HEIGHT = 196;
	public static final int SLOW_SCROLL_THRESHOLD = 64;
	public static final int FAST_SCROLL_THRESHOLD = 32;
	public static final int MAX_OFFSET= 32;
	public static final int MINISCULE_MAP_WIDTH = 11;
	public static final int MINISCULE_MAP_HEIGHT = 18;
	public static final int MIDSIZED_MAP_WIDTH = 17;
	public static final int MIDSIZED_MAP_HEIGHT = 28;
	public static final int MAMMOTH_MAP_WIDTH = 27;
	public static final int MAMMOTH_MAP_HEIGHT = 44;

	public static final int SLOW_SCROLL_SPEED = 8;
	public static final int FAST_SCROLL_SPEED = 24;
	
	public static final long ACTION_COOLDOWN = 200;
	
	public static final byte N = 0;
	public static final byte S = 1;
	public static final byte E = 2;
	public static final byte W = 3;
	public static final byte NS = 4;
	public static final byte NE = 5;
	public static final byte NW = 6;
	public static final byte SE = 7;
	public static final byte SW = 8;
	public static final byte EW = 9;
	public static final byte NSE = 10;
	public static final byte NSW = 11;
	public static final byte NEW = 12;
	public static final byte SEW = 13;
	public static final byte NSEW = 14;
	
}
