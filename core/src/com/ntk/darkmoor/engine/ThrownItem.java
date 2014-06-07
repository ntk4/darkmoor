package com.ntk.darkmoor.engine;

import java.io.IOException;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.engine.Square.SquarePosition;
import com.ntk.darkmoor.engine.actor.Door;
import com.ntk.darkmoor.resource.Resources;
import com.ntk.darkmoor.stub.GameTime;

/**
 * All thrown objects in the maze (item, fireball, acid cloud....) <br/>
 * http://eob.wikispaces.com/eob.thrownitem
 * 
 * @author Nick
 * 
 */
public class ThrownItem {

	private DungeonLocation location;
	private Entity caster;
	private Item item;
	private long speed;
	private int distance;
	private long lastUpdate;

	public ThrownItem(Square square) {
		if (square != null)
			location = new DungeonLocation(square.getMaze().getName(), square.getLocation());
	}

	/**
	 * Constructor
	 * 
	 * @param owner Entity who threw the item
	 * @param item Item
	 * @param location Start location
	 * @param speed Time in ms taken to cross a block
	 * @param distance How many block the item have to fly before falling on the ground
	 */
	public ThrownItem(Entity owner, Item item, DungeonLocation location, long speed, int distance) {
		this.caster = owner;
		this.item = item;
		this.location = new DungeonLocation(location);
		this.speed = speed;
		this.distance = distance;
	}

	public boolean update(GameTime time, Maze maze) {
		// Item can't move any more
		if (distance == 0)
			return true;

		lastUpdate += time.getElapsedGameTime();

		if (lastUpdate > speed) {
			lastUpdate -= speed;

			// Find the next block according to the direction
			Vector2 dst = null;
			switch (location.getDirection()) {
			case North:
				dst = new Vector2(location.getCoordinates().x, location.getCoordinates().y - 1);
				break;
			case East:
				dst = new Vector2(location.getCoordinates().x + 1, location.getCoordinates().y);
				break;
			case South:
				dst = new Vector2(location.getCoordinates().x, location.getCoordinates().y + 1);
				break;
			case West:
				dst = new Vector2(location.getCoordinates().x - 1, location.getCoordinates().y);
				break;
			}

			// Blocked by a wall, fall before the block
			Square square = maze.getSquare(dst);

			// Can item pass through a door ?
			if (square.getActor() != null && square.getActor() instanceof Door) {
				Door door = (Door) square.getActor();
				if (!door.canItemsPassThrough(item)) {
					distance = 0;
				}
			}

			// Wall is blocking
			else if (square.isBlocking()) {
				distance = 0;
			}

			// Blocked by an obstacle, but fall on the block
			if ((square.getActor() != null && square.getActor().isCanPassThrough()) || square.getMonsterCount() > 0) {
				location.setCoordinates(dst);

				SquarePosition gp = location.getPosition();
				switch (location.getDirection()) {
				case North:
					if (gp == SquarePosition.NorthEast || gp == SquarePosition.SouthEast)
						location.setPosition(SquarePosition.SouthEast);
					else
						location.setPosition(SquarePosition.SouthWest);
					break;
				case South:
					if (gp == SquarePosition.NorthEast || gp == SquarePosition.SouthEast)
						location.setPosition(SquarePosition.NorthEast);
					else
						location.setPosition(SquarePosition.NorthWest);
					break;
				case West:
					if (gp == SquarePosition.NorthEast || gp == SquarePosition.NorthWest)
						location.setPosition(SquarePosition.NorthEast);
					else
						location.setPosition(SquarePosition.SouthEast);
					break;
				case East:
					if (gp == SquarePosition.NorthEast || gp == SquarePosition.NorthWest)
						location.setPosition(SquarePosition.NorthWest);
					else
						location.setPosition(SquarePosition.SouthWest);
					break;
				}

				// Get monster and hit it
				if (square.getMonsterCount() > 0) {
					Monster[] monsters = maze.getSquare(location.getCoordinates()).getMonsters();
					for (Monster monster : monsters)
						if (monster != null) {
							Attack attack = new Attack(caster, monster, item);
							if (attack.isAHit())
								distance = 0;
						}
				}
				return true;
			}

			// Drop the item at good ground position
			if (distance == 0) {
				SquarePosition gp = location.getPosition();
				switch (location.getDirection()) {
				case North:
					if (gp == SquarePosition.NorthEast || gp == SquarePosition.SouthEast)
						location.setPosition(SquarePosition.NorthEast);
					else
						location.setPosition(SquarePosition.NorthWest);
					break;
				case South:
					if (gp == SquarePosition.NorthEast || gp == SquarePosition.SouthEast)
						location.setPosition(SquarePosition.SouthEast);
					else
						location.setPosition(SquarePosition.SouthWest);
					break;
				case West:
					if (gp == SquarePosition.NorthEast || gp == SquarePosition.NorthWest)
						location.setPosition(SquarePosition.NorthWest);
					else
						location.setPosition(SquarePosition.SouthWest);
					break;
				case East:
					if (gp == SquarePosition.NorthEast || gp == SquarePosition.NorthWest)
						location.setPosition(SquarePosition.NorthEast);
					else
						location.setPosition(SquarePosition.SouthEast);
					break;
				}

				return true;
			} else {
				distance--;
				location.setCoordinates(dst);
			}
		}

		return false;
	}

	public boolean load(Element xml) {
		if (xml == null)
			return false;

		String name = null;
		for (int i = 0; i < xml.getChildCount(); i++) {
			Element child = xml.getChild(i);
			name = child.getName();

			if ("location".equalsIgnoreCase(name)) {
				location.load(child);
			} else if ("item".equalsIgnoreCase(name)) {
				Item item = Resources.createAsset(Item.class, "Main"); // ntk: how is this used??
				this.item = Resources.createAsset(Item.class, child.getAttribute("name"));
			} else if ("distance".equalsIgnoreCase(name)) {
				distance = Integer.parseInt(child.getAttribute("value"));
			} else if ("speed".equalsIgnoreCase(name)) {
				speed = Integer.parseInt(child.getAttribute("value"));
			}
		}

		return true;
	}

	public boolean save(XmlWriter writer) throws IOException {
		if (writer == null)
			return false;

		writer.element("thrownitem");
		location.save("location", writer);

		writer.element("item").attribute("name", item.getName()).pop();
		writer.element("speed").attribute("value", speed).pop();
		writer.element("distance").attribute("name", distance).pop();

		writer.pop();

		return true;
	}

	public DungeonLocation getLocation() {
		return location;
	}

	public Entity getCaster() {
		return caster;
	}

	public Item getItem() {
		return item;
	}

	public long getSpeed() {
		return speed;
	}

	public int getDistance() {
		return distance;
	}

	public long getLastUpdate() {
		return lastUpdate;
	}

}
