package map;

import java.util.HashMap;
import java.util.HashSet;

import display.Display;
import main.Constants;
import objects.Roads;

public class Map {

	public static Tile map[][];

	private int width;
	private int height;
	private RoadManager roadManager;
	HashMap<MapElem, HashSet<Link>> inlets;
	HashMap<MapElem, HashSet<Link>> outlets;
	HashSet<LinkProperty> validProperties;

	public Display display;

	public Map(int mapWidth, int mapHeight) {
		width = mapWidth;
		height = mapHeight;
		map = new Tile[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				map[x][y] = new Tile();
			}
		}
		inlets = new HashMap<MapElem, HashSet<Link>>();
		outlets = new HashMap<MapElem, HashSet<Link>>();
		validProperties = new HashSet<LinkProperty>();
		roadManager = new RoadManager(this);
		MapRoad startingPoint = new MapRoad(false);
		startingPoint.tileX = width / 2;
		startingPoint.tileY = height - 1;
		map[startingPoint.tileX][startingPoint.tileY].mapElem = startingPoint;
		outlets.put(startingPoint, new HashSet<Link>());
		Roads.locs.add(new Integer[]{startingPoint.tileX, startingPoint.tileY});
		Roads.mapRoads.add(startingPoint);
		map[startingPoint.tileX][startingPoint.tileY - 1].isRoadBuildable = true;
		map[startingPoint.tileX][startingPoint.tileY - 1].orientation = Constants.S;
		map[startingPoint.tileX + 1][startingPoint.tileY].isRoadBuildable = true;
		map[startingPoint.tileX + 1][startingPoint.tileY].orientation = Constants.W;
		map[startingPoint.tileX - 1][startingPoint.tileY].isRoadBuildable = true;
		map[startingPoint.tileX - 1][startingPoint.tileY].orientation = Constants.E;
	}

	public int[] checkBuildRoad(int x, int y) {
		if (x >= 0 && x < width && y >= 0 && y < height && map[x][y].isRoadBuildable) {
			return new int[]{x, y};
		} else {
			return null;
		}
	}
	
	public int[] checkRemoveRoad(int x, int y) {
		if (x >= 0 && x < width && y >= 0 && y < height && !(x == width / 2 && y == height - 1) && map[x][y].mapElem != null && map[x][y].mapElem instanceof MapRoad) {
			return new int[]{x, y};
		} else {
			return null;
		}
	}

	public void buildRoad(int x, int y) {
		MapRoad road = new MapRoad(false);
		road.tileX = x;
		road.tileY = y;
		switch (map[x][y].orientation) {
		case Constants.N:
			roadManager.addRoadOutlet(new MapElem[]{map[x][y - 1].mapElem}, road, new byte[]{Constants.S});
			break;
		case Constants.S:
			roadManager.addRoadOutlet(new MapElem[]{map[x][y + 1].mapElem}, road, new byte[]{Constants.N});
			break;
		case Constants.E:
			roadManager.addRoadOutlet(new MapElem[]{map[x + 1][y].mapElem}, road, new byte[]{Constants.W});
			break;
		case Constants.W:
			roadManager.addRoadOutlet(new MapElem[]{map[x - 1][y].mapElem}, road, new byte[]{Constants.E});
			break;
		case Constants.NS:
			display.dashboard.promptDirs(Constants.NS);
			roadManager.addRoadOutlet(new MapElem[]{map[x][y - 1].mapElem, map[x][y + 1].mapElem}, road, new byte[]{Constants.S, Constants.N});
			break;
		case Constants.NE:
			roadManager.addRoadOutlet(new MapElem[]{map[x][y - 1].mapElem, map[x + 1][y].mapElem}, road, new byte[]{Constants.S, Constants.W});
			break;
		case Constants.NW:
			roadManager.addRoadOutlet(new MapElem[]{map[x][y - 1].mapElem, map[x - 1][y].mapElem}, road, new byte[]{Constants.S, Constants.E});
			break;
		case Constants.SE:
			roadManager.addRoadOutlet(new MapElem[]{map[x][y + 1].mapElem, map[x + 1][y].mapElem}, road, new byte[]{Constants.N, Constants.W});
			break;
		case Constants.SW:
			roadManager.addRoadOutlet(new MapElem[]{map[x][y + 1].mapElem, map[x - 1][y].mapElem}, road, new byte[]{Constants.N, Constants.E});
			break;
		case Constants.EW:
			roadManager.addRoadOutlet(new MapElem[]{map[x + 1][y].mapElem, map[x - 1][y].mapElem}, road, new byte[]{Constants.W, Constants.E});
			break;
		case Constants.NSE:
			roadManager.addRoadOutlet(new MapElem[]{map[x][y - 1].mapElem, map[x][y + 1].mapElem, map[x + 1][y].mapElem}, road, new byte[]{Constants.S, Constants.N, Constants.W});
			break;
		case Constants.NSW:
			roadManager.addRoadOutlet(new MapElem[]{map[x][y - 1].mapElem, map[x][y + 1].mapElem, map[x - 1][y].mapElem}, road, new byte[]{Constants.S, Constants.N, Constants.E});
			break;
		case Constants.NEW:
			roadManager.addRoadOutlet(new MapElem[]{map[x][y - 1].mapElem, map[x + 1][y].mapElem, map[x - 1][y].mapElem}, road, new byte[]{Constants.S, Constants.W, Constants.E});
			break;
		case Constants.SEW:
			roadManager.addRoadOutlet(new MapElem[]{map[x][y + 1].mapElem, map[x + 1][y].mapElem, map[x - 1][y].mapElem}, road, new byte[]{Constants.N, Constants.W, Constants.E});
			break;
		case Constants.NSEW:
			roadManager.addRoadOutlet(new MapElem[]{map[x][y - 1].mapElem, map[x][y + 1].mapElem, map[x + 1][y].mapElem, map[x - 1][y].mapElem}, road, new byte[]{Constants.S, Constants.N, Constants.W, Constants.E});
			break;
		}
		Roads.locs.add(new Integer[]{x, y});
		Roads.mapRoads.add(road);
		map[x][y].mapElem = road;
		map[x][y].isRoadBuildable = false;
		checkRoadBuildableSurroundings(x, y);
	}

	private void checkRoadBuildableSurroundings(int x, int y) {
		if (y > 0) {
			checkRoadBuildable(x, y - 1);
		}
		if (y < height - 1) {
			checkRoadBuildable(x, y + 1);
		}
		if (x < width - 1) {
			checkRoadBuildable(x + 1, y);
		}
		if (x > 0) {
			checkRoadBuildable(x - 1, y);
		}
		checkRoadBuildable(x, y);
	}

	private void checkRoadBuildable(int x, int y) {
		if (map[x][y].mapElem == null) {
			boolean north = y > 0 && map[x][y - 1].mapElem != null && map[x][y - 1].mapElem instanceof MapRoad;
			boolean south = y < height - 1 && map[x][y + 1].mapElem != null && map[x][y + 1].mapElem instanceof MapRoad;
			boolean east = x < width - 1 && map[x + 1][y].mapElem != null && map[x + 1][y].mapElem instanceof MapRoad;
			boolean west = x > 0 && map[x - 1][y].mapElem != null && map[x - 1][y].mapElem instanceof MapRoad;
			if (north && east && map[x + 1][y - 1].mapElem != null && map[x + 1][y - 1].mapElem instanceof MapRoad ||
					east && south && map[x + 1][y + 1].mapElem != null && map[x + 1][y + 1].mapElem instanceof MapRoad ||
					south && west && map[x - 1][y + 1].mapElem != null && map[x - 1][y + 1].mapElem instanceof MapRoad ||
					west && north && map[x - 1][y - 1].mapElem != null && map[x - 1][y - 1].mapElem instanceof MapRoad ||
					!north && !south && !east && !west) {
				map[x][y].isRoadBuildable = false;
			} else {
				map[x][y].isRoadBuildable = true;
				byte orientation = 0;
				if (north && !south && !east && !west) {
					orientation = Constants.N;
				} else if (!north && south && !east && !west) {
					orientation = Constants.S;
				} else if (!north && !south && east && !west) {
					orientation = Constants.E;
				} else if (!north && !south && !east && west) {
					orientation = Constants.W;
				} else if (north && south && !east && !west) {
					orientation = Constants.NS;
				} else if (north && !south && east && !west) {
					orientation = Constants.NE;
				} else if (north && !south && !east && west) {
					orientation = Constants.NW;
				} else if (!north && south && east && !west) {
					orientation = Constants.SE;
				} else if (!north && south && !east && west) {
					orientation = Constants.SW;
				} else if (!north && !south && east && west) {
					orientation = Constants.EW;
				} else if (north && south && east && !west) {
					orientation = Constants.NSE;
				} else if (north && south && !east && west) {
					orientation = Constants.NSW;
				} else if (north && !south && east && west) {
					orientation = Constants.NEW;
				} else if (!north && south && east && west) {
					orientation = Constants.SEW;
				} else if (north && south && east && west) {
					orientation = Constants.NSEW;
				}
				map[x][y].orientation = orientation;
			}
		} else {
			map[x][y].isRoadBuildable = false;
		}
	}

	public void removeRoad(int x, int y) {
		roadManager.removeElem(map[x][y].mapElem);
		Roads.removeLoc(new Integer[]{x, y});
		Roads.mapRoads.remove(map[x][y].mapElem);
		map[x][y].mapElem = null;
		checkRoadBuildableSurroundings(x, y);
	}

}
