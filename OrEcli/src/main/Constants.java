package main;

import game.Side;

public class Constants {
	
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
	
	public static final int NORTH = 0;
	public static final int SOUTH = 1;
	public static final int EAST = 2;
	public static final int WEST = 3;
}
