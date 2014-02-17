package map;

public class Map {

	private int width;
	private int height;
	private Tile map[][];

	public Map(int mapWidth, int mapHeight) {
		width = mapWidth;
		height = mapHeight;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				map[x][y] = new Tile();
			}
		}
	}

}
