package com.ntk.darkmoor.engine;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.ntk.commons.StringUtils;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.config.Log;
import com.ntk.darkmoor.exception.InitializationException;
import com.ntk.darkmoor.resource.Resources;
import com.ntk.darkmoor.stub.GameTime;
import com.ntk.darkmoor.stub.Maze;
import com.ntk.darkmoor.stub.Mouse;
import com.ntk.darkmoor.stub.Square;
import com.ntk.darkmoor.stub.TileSet;

public class Dungeon {

	public static final String TAG = "dungeon";

	private HashMap<String, Maze> mazes;
	private DungeonLocation startLocation;
	private TileSet itemTileSet;
	private String itemTileSetName;
	private boolean disposed;
	private String note;
	private String name;

	public Dungeon() {
		mazes = new HashMap<String, Maze>();
		startLocation = new DungeonLocation();
	}

	public boolean init() {
		Log.debug("[Dungeon] : init() start");

		// Loads maze display coordinates
		try {
			DisplayCoordinates.load();
		} catch (IOException e) {
			throw new InitializationException(e);
		}

		loadItemTileSet();

		for (Maze maze : mazes.values())
			maze.init();

		Mouse.loadTileSet(itemTileSet);

		return true;
	}

	private boolean loadItemTileSet() {
		if (StringUtils.isEmpty(itemTileSetName))
			return false;

		if (itemTileSet != null)
			itemTileSet.dispose();
		itemTileSet = null;

		itemTileSet = Resources.createAsset(TileSet.class, itemTileSetName);

		return itemTileSet != null;
	}

	public void dispose() {
		for (Maze maze : mazes.values())
			maze.dispose();
		mazes.clear();

		if (itemTileSet != null)
			itemTileSet.dispose();
		itemTileSet = null;

		startLocation = null;
		note = "";

		disposed = true;
	}

	public boolean load(XmlReader.Element node) {
		if (node == null || !StringUtils.equals(node.getName(), TAG)) {
			Log.error("[Dungeon] expecting \"" + TAG + "\" in node header, found \"" + node.getName()
					+ "\" when loading Dungeon.");
			return false;
		}

		name = node.getName();

		for (int i = 0; i < node.getChildCount(); i++) {
			XmlReader.Element child = node.getChild(i);

			if ("items".equalsIgnoreCase(child.getName())) {
				itemTileSetName = child.getAttribute("tileset");

			} else if (Maze.TAG.equalsIgnoreCase(child.getName())) {
				String name = node.getAttribute("name");
				Maze maze = new Maze(this);
				maze.setName(name);
				mazes.put(name, maze);
				maze.load(node);

			} else if ("start".equalsIgnoreCase(child.getName())) {
				startLocation = new DungeonLocation(child);

			} else if ("note".equalsIgnoreCase(child.getName())) {
				note = child.getText();

			}
		}
		return true;
	}

	public boolean save(XmlWriter writer, int id) throws IOException {
		if (writer == null)
			return false;

		writer.element(TAG).attribute("name", name);

		writer.element("items").attribute("tileset", itemTileSetName).pop();

		for (Maze maze : mazes.values()) {
			maze.save(writer);
		}

		startLocation.save("start", writer);

		// TODO: ntk: check if this write(note) works as expected
		writer.element("note").write(note); 
		writer.pop().pop();

		return true;
	}

	public void clear() {
		for (Maze maze : mazes.values())
			maze.dispose();
		mazes.clear();

		startLocation = new DungeonLocation(startLocation);
	}

	public void update(GameTime time) {
		for (Maze maze : mazes.values()) {
			maze.update(time);
		}
	}

	/**
	 * Defines a square at a given location
	 * 
	 * @param target
	 *            Location in the dungeon
	 * @param square
	 *            Square handle
	 * @return True on success
	 */
	public boolean setSquare(DungeonLocation target, Square square) {
		if (target == null || square == null)
			return false;

		Maze maze = getMaze(target.getMaze());
		if (maze == null)
			return false;

		return maze.setSquare(target.getCoordinates(), square);
	}

	/**
	 * Returns the maze by its name
	 * 
	 * @param name
	 *            maze name
	 * @return maze or null
	 */
	public Maze getMaze(String name) {
		if (StringUtils.isEmpty(name) || !mazes.containsKey(name))
			return null;

		return mazes.get(name);
	}

	/**
	 * Adds a maze to the dungeon
	 * 
	 * @param maze
	 *            the maze to add
	 */
	public void addMaze(Maze maze) {
		if (maze == null)
			return;

		mazes.put(maze.getName(), maze);
	}

	/**
	 * Removes a maze from the dungeon
	 * 
	 * @param name
	 *            the name of the maze to remove
	 */
	public void removeMaze(String name) {
		if (StringUtils.isEmpty(name))
			return;

		mazes.remove(name);
	}

	public TileSet getItemTileSet() {
		// TODO Auto-generated method stub
		return null;
	}

	public DungeonLocation getStartLocation() {
		return startLocation;
	}

	public void setStartLocation(DungeonLocation startLocation) {
		this.startLocation = startLocation;
	}

	public String getItemTileSetName() {
		return itemTileSetName;
	}

	public void setItemTileSetName(String itemTileSetName) {
		this.itemTileSetName = itemTileSetName;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HashMap<String, Maze> getMazes() {
		return mazes;
	}

	public boolean isDisposed() {
		return disposed;
	}

}
