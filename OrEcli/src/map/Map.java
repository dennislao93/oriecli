package map;

import display.Road;
import main.Constants;
import game.Side;

public class Map {

	private int width;
	private int height;
	private Tile map[][];
	private RoadManager roadManager;
	private MapRoad start1;
	private MapRoad start2;

	public Map(int mapWidth, int mapHeight) {
		width = mapWidth;
		height = mapHeight;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				map[x][y] = new Tile();
			}
		}
		start1 = new MapRoad(Side.PLAYER1, Constants.NORTH, false);
		start2 = new MapRoad(Side.PLAYER2, Constants.SOUTH, false);
		map[width / 2][0].setRoad(new Road(null, start1)); //TODO
		map[width / 2][height - 1].setRoad(new Road(null, start2)); //TODO
		roadManager = new RoadManager(start1, start2);
	}
	
	/**
	 * @param x xTile
	 * @param y yTile
	 * @param isBig
	 * @return direction of road to be added, or -1 if road can't be built on tile
	 */
	public int getRoadSuggestion(int x, int y, boolean isBig) {
		return -1; //TODO
	}
}
