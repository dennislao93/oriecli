package map;

import display.Displayable;
import objects.Building;
import objects.Unit;

public class Map {

	private int width;
	private int height;
	private Unit unitMap[][];
	private Building buildingMap[][];

	public Map(int mapWidth, int mapHeight) {
		width = mapWidth;
		height = mapHeight;
		map = new Tile[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				map[x][y] = new Tile();
			}
		}
	}

}
