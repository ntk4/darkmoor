package com.ntk.darkmoor.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.engine.Compass.CardinalPoint;
import com.ntk.darkmoor.engine.actor.AlcoveActor;
import com.ntk.darkmoor.engine.actor.Door;
import com.ntk.darkmoor.engine.actor.EventSquare;
import com.ntk.darkmoor.engine.actor.ForceField;
import com.ntk.darkmoor.engine.actor.Pit;
import com.ntk.darkmoor.engine.actor.PressurePlate;
import com.ntk.darkmoor.engine.actor.SquareActor;
import com.ntk.darkmoor.engine.actor.Stair;
import com.ntk.darkmoor.engine.actor.Teleporter;
import com.ntk.darkmoor.engine.actor.WallSwitch;
import com.ntk.darkmoor.resource.Resources;
import com.ntk.darkmoor.stub.GameScreen;
import com.ntk.darkmoor.stub.GameTime;
import com.ntk.darkmoor.stub.MouseButtons;

/**
 * Represents a block in a maze
 * 
 * 
 * Events<br/>
 * OnTeamEnter Team enter in the block<br/>
 * OnTeamStand Team stand in the block<br/>
 * OnTeamLeave Team leave the block<br/>
 * OnDropItem An item is dropped on the ground<br/>
 * OnCollectItem An item is picked up off the ground<br/>
 * OnClick The user clicked on the block<br/>
 * 
 * @author Nick
 * 
 */
public class Square {

	public enum SquareType {
		/** Ground block */
		Ground,

		/** Wall block */
		Wall,

		/** Illusionary wall */
		Illusion;

		public static SquareType parse(String attribute) {
			if (Illusion.toString().equals(attribute))
				return Illusion;
			else if (Wall.toString().equals(attribute))
				return Wall;
			else
				return Ground;
		}
	}

	//@formatter:off
	/**Sub position in a square in the maze
	 .-------.
	 | A | B |
	 | +---+ |
	 |-| E |-|
	 | +---+ |
	 | C | D |
	 '-------'
	*/ //@formatter:on
	public enum SquarePosition {

		/** North west */
		NorthWest(0),

		/** North east */
		NorthEast(1),

		/** South west */
		SouthWest(2),

		/** South east */
		SouthEast(3),

		/** Center position (not for items !) */
		Center(4);

		private int value;

		SquarePosition(int value) {
			this.value = value;
		}

		public int value() {
			return value;
		}

		public static SquarePosition valueOf(int position) {
			switch (position) {
			case 0:
				return NorthWest;
			case 1:
				return NorthEast;
			case 2:
				return SouthWest;
			case 3:
				return SouthEast;
			case 4:
				return Center;
			default:
				return Center;
			}
		}
	}

	public static final String TAG = "square";

	private SquareActor actor;

	private Maze maze;

	private SquareType type;

	private Monster[] monsters;

	private int[] decorations;

	private ArrayList<List<Item>> items;

	private int monsterCount;

	private boolean noMonster;

	private boolean noGhost;

	private Vector2 location;

	private boolean pitTarget;

	public Square(Maze maze) {
		this.maze = maze;
		this.type = SquareType.Wall;
		this.monsters = new Monster[4];

		this.decorations = new int[] { -1, -1, -1, -1 };

		this.items = new ArrayList<List<Item>>(4);
		items.add(new ArrayList<Item>());
		items.add(new ArrayList<Item>());
		items.add(new ArrayList<Item>());
		items.add(new ArrayList<Item>());
	}

	public void init() {
		for (int i = 0; i < 4; i++) {
			// Monsters
			if (monsters[i] != null)
				monsters[i].onSpawn();
		}
	}

	public void dispose() {
		for (int i = 0; i < 4; i++) {
			if (monsters[i] != null)
				monsters[i].dispose();

			monsters[i] = null;

			decorations[i] = -1;
			items.get(i).clear();
		}

		if (actor != null) {
			actor.dispose();
			actor = null;
		}

	}

	public boolean load(Element xml) {
		if (xml == null)
			return false;

		String[] cardinalNames = CardinalPoint.stringValues();

		String name = null;
		for (int i = 0; i < xml.getChildCount(); i++) {
			Element child = xml.getChild(i);
			name = child.getName();

			if ("monster".equalsIgnoreCase(name)) {
				Monster monster = new Monster();
				monster.load(child);
				monster.teleport(this, SquarePosition.valueOf(child.getAttribute("position")));

			} else if ("item".equalsIgnoreCase(name)) {
				SquarePosition loc = SquarePosition.valueOf(child.getAttribute("location"));
				Item item = Resources.getItemAsset(child.getAttribute("name"));
				if (item != null)
					items.get(loc.value()).add(item);

			} else if ("noghost".equalsIgnoreCase(name)) {
				noGhost = Boolean.parseBoolean(child.getAttribute("value"));

			} else if ("nomonster".equalsIgnoreCase(name)) {
				noMonster = Boolean.parseBoolean(child.getAttribute("value"));

			} else if ("type".equalsIgnoreCase(name)) {
				type = SquareType.parse(child.getAttribute("value"));

			} else if ("decoration".equalsIgnoreCase(name)) {
				decorations[0] = Integer.parseInt(child.getAttribute(cardinalNames[0]));
				decorations[1] = Integer.parseInt(child.getAttribute(cardinalNames[1]));
				decorations[2] = Integer.parseInt(child.getAttribute(cardinalNames[2]));
				decorations[3] = Integer.parseInt(child.getAttribute(cardinalNames[3]));

			} else if (WallSwitch.TAG.equalsIgnoreCase(name)) {
				actor = new WallSwitch(this);
				actor.load(child);

			} else if (Door.TAG.equalsIgnoreCase(name)) {
				actor = new AlcoveActor(this);
				actor.load(child);

			} else if (Teleporter.TAG.equalsIgnoreCase(name)) {
				actor = new Teleporter(this);
				actor.load(child);

			} else if (PressurePlate.TAG.equalsIgnoreCase(name)) {
				actor = new PressurePlate(this);
				actor.load(child);

			} else if (Pit.TAG.equalsIgnoreCase(name)) {
				actor = new Pit(this);
				actor.load(child);

			} else if (ForceField.TAG.equalsIgnoreCase(name)) {
				actor = new ForceField(this);
				actor.load(child);

			} else if (Stair.TAG.equalsIgnoreCase(name)) {
				actor = new Stair(this);
				actor.load(child);

			} else if (EventSquare.TAG.equalsIgnoreCase(name)) {
				actor = new EventSquare(this);
				actor.load(child);

			} else if (AlcoveActor.TAG.equalsIgnoreCase(name)) {
				actor = new AlcoveActor(this);
				actor.load(child);
			}
		}

		return true;
	}

	public boolean save(XmlWriter writer) throws IOException {
		if (writer == null)
			return false;

		writer.element(TAG);

		writer.element("type").attribute("value", type.toString()).pop();

		if (actor != null)
			actor.save(writer);

		writer.element("decoration");
		writer.attribute(CardinalPoint.North.toString(), getDecorationId(CardinalPoint.North));
		writer.attribute(CardinalPoint.South.toString(), getDecorationId(CardinalPoint.South));
		writer.attribute(CardinalPoint.West.toString(), getDecorationId(CardinalPoint.West));
		writer.attribute(CardinalPoint.East.toString(), getDecorationId(CardinalPoint.East));
		writer.pop();

		// Items
		for (int i = 0; i < 4; i++) {
			if (items.get(i).size() == 0)
				continue;

			for (Item item : items.get(i)) {
				writer.element("item").attribute("location", SquarePosition.valueOf(i).toString());
				writer.attribute("name", item.getName()).pop();
			}
		}

		for (Monster monster : monsters) {
			if (monster != null)
				monster.save(writer);
		}

		if (noMonster) {
			writer.element("nomonster").attribute("value", noMonster).pop();
		}

		if (noGhost) {
			writer.element("noghost").attribute("value", noGhost).pop();
		}

		writer.pop();

		return true;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		if (actor != null)
			sb.append(actor);

		else if (monsterCount > 0)
			sb.append(" " + monsterCount + " monster(s)");

		else if (noMonster)
			sb.append(" (no monster)");

		else if (noGhost)
			sb.append(" (no ghost)");

		else if (isWall()) {
			if (isIllusion()) {
				sb.append(" Illusion");
			} else
				sb.append(" Wall");
		} else {
			sb.append(" Floor");
		}

		if (getItemCount() > 0)
			sb.append(" " + getItemCount() + " item(s)");

		return sb.toString();
	}

	public void update(GameTime time) {

		// Count the number of monster
		monsterCount = 0;
		if (monsters[0] != null)
			monsterCount++;
		if (monsters[1] != null)
			monsterCount++;
		if (monsters[2] != null)
			monsterCount++;
		if (monsters[3] != null)
			monsterCount++;

		// Update monsters
		if (monsterCount > 0)
			for (int i = 0; i < 4; i++) {
				Monster monster = monsters[i];
				if (monster == null)
					continue;

				// Monster is dead
				if (monster.isDead()) {
					monster.onDeath();
					monsters[i] = null;
				} else
					monster.update(time);

			}

		// actor
		if (actor != null)
			actor.update(time);
	}

	public boolean onClick(Vector2 location, CardinalPoint side, MouseButtons button) {

		// actor interaction
		if (actor != null)
			return actor.onClick(location, side, button);

		// Decoration interaction
		Team team = GameScreen.getTeam();
		Decoration decoration = team.getMaze().getDecoration(team.getFrontLocation().getCoordinates(),
				Compass.getOppositeDirection(team.getDirection()));
		if (decoration != null) {
			GameMessage.addMessage("Decoration: OnClick()");

			return false;
		}

		return false;
	}

	// / <summary>
	// / A hero used an item on the wall
	// / </summary>
	// / <param name="item">Item handle</param>
	// / <returns>True if the event is processed</returns>
	public boolean onHack(CardinalPoint side, Item item) {
		Decoration deco = maze.getDecoration(location, side);
		if (deco != null && deco.getOnHackId() != -1) {
			// If forced decoration, then erase all other decorations
			if (deco.isForceDisplay()) {
				for (int id = 0; id < 4; id++) {
					if (decorations[id] != -1)
						decorations[id] = deco.getOnHackId();
				}
			}

			// change only this decoration
			else
				decorations[side.value()] = deco.getOnHackId();

			return true;
		}

		GameMessage.addMessage("Square: OnHack()");

		return false;
	}

	// / <summary>
	// / A hero used an empty hand on the wall
	// / </summary>
	// / <returns>True if the event is processed</returns>
	public boolean onBash(CardinalPoint side, Item item) {
		Decoration deco = maze.getDecoration(location, side);
		if (deco != null && deco.getOnHackId() != -1) {
			// If forced decoration, then erase all other decorations
			if (deco.isForceDisplay()) {
				for (int id = 0; id < 4; id++) {
					if (decorations[id] != -1)
						decorations[id] = deco.getOnHackId();
				}
			}

			// change only this decoration
			else
				decorations[side.value()] = deco.getOnHackId();

			return true;
		}

		GameMessage.addMessage("Square: OnBash()");
		return false;
	}

	// / <summary>
	// / Team stand on the block
	// / </summary>
	public void onTeamStand() {
		if (actor != null)
			actor.onTeamStand();
	}

	public void onMonsterStand(Monster monster) {
		if (monster == null)
			return;

		if (actor != null)
			actor.onMonsterStand(monster);
	}

	public void onTeamEnter() {
		if (actor != null)
			actor.onTeamEnter();
	}

	public void onTeamLeave() {
		if (actor != null)
			actor.onTeamLeave();
	}

	public void onMonsterEnter(Monster monster) {
		if (monster == null)
			return;

		if (actor != null)
			actor.onMonsterEnter(monster);
	}

	public void onMonsterLeave(Monster monster) {
		if (monster == null)
			return;

		if (actor != null)
			actor.onMonsterLeave(monster);
	}

	public void onDroppedItem(Item item) {
		if (item == null)
			return;

		if (actor != null)
			actor.onItemDropped(item);
	}

	public void onCollectedItem(Item item) {
		if (item == null)
			return;

		if (actor != null)
			actor.onItemCollected(item);
	}

	public boolean hasDecoration(CardinalPoint from, CardinalPoint side) {
		return getDecorationId(from, side) != -1;
	}

	public boolean hasDecoration(CardinalPoint side) {
		return decorations[side.value()] != -1;
	}

	public int getDecorationId(CardinalPoint from, CardinalPoint side) {
		// Get desired side
		return decorations[Compass.getDirectionFromView(from, side).value()];
	}

	public int getDecorationId(CardinalPoint side) {
		// Is there a forced decoration
		// foreach (CardinalPoint cp in Enum.GetValues(typeof(CardinalPoint)))
		// {
		// Decoration deco = Maze.getDecoration(location, cp);
		// if (deco != null && deco.isForceDisplay())
		// return 0;
		// }

		// The decoration of the desired side
		return decorations[side.value()];
	}

	public boolean addMonster(Monster monster) {
		return false;

		// ntk: unreachable code was commented out. is this done somewhere else?
		/*
		 * if (monster == null) return false;
		 * 
		 * // Find the first free slot for (int i = 0; i < 4; i++) if (monsters[i] == null) { monsters[i] = monster;
		 * monster.teleport(this); monster.onSpawn(); return true; }
		 * 
		 * 
		 * return false;
		 */}

	public boolean addMonster(Monster monster, SquarePosition position) {
		return false;

		// ntk: unreachable code was commented out. is this done somewhere else?
		/*
		 * if (position == SquarePosition.Center || getMonster(position) != null || monster == null) return false;
		 * 
		 * monsters[position.value()] = monster;
		 * 
		 * return true;
		 */
	}

	public Monster getMonster(SquarePosition position) {
		// No monster here
		if (monsterCount == 0)
			return null;

		// Only one monster, so returns this one
		else if (monsterCount == 1) {
			for (int i = 0; i < 4; i++)
				if (monsters[i] != null)
					return monsters[i];
		}

		if (position == SquarePosition.Center)
			return null;

		return monsters[position.value()];
	}

	public void removeMonster(SquarePosition position) {
		if (position == SquarePosition.Center)
			return;

		monsters[position.value()] = null;
	}

	public List<Item> getItemsFromSide(CardinalPoint side) {
		return items.get(side.value());
	}

	public List<Item> getItemsFromSide(CardinalPoint from, CardinalPoint side) {
		return getItemsFromSide(Compass.getDirectionFromView(from, side));
	}

	public Item collectItemFromSide(CardinalPoint side) {
		return collectItem(SquarePosition.valueOf(side.value()));
	}

	public boolean dropItemFromSide(CardinalPoint side, Item item) {
		if (item == null) // || !HasAlcove(side))
			return false;

		items.get(side.value()).add(item);

		return true;
	}

	public List<Item> getItems(SquarePosition position) {
		return items.get(position.value());
	}

	public List<Item> getItems(CardinalPoint from, SquarePosition position) {
		CardinalPoint[][] tab = new CardinalPoint[][] {
				{ CardinalPoint.North, CardinalPoint.South, CardinalPoint.West, CardinalPoint.East },
				{ CardinalPoint.South, CardinalPoint.North, CardinalPoint.East, CardinalPoint.West },
				{ CardinalPoint.West, CardinalPoint.East, CardinalPoint.South, CardinalPoint.North },
				{ CardinalPoint.East, CardinalPoint.West, CardinalPoint.North, CardinalPoint.South }, };

		// TODO: ntk: result to be checked! we map enum values without seeming similar
		return getItems(SquarePosition.valueOf(tab[from.value()][position.value()].value()));
	}

	public Item collectItem(SquarePosition position) {
		// No item in the middle of a block
		if (position == SquarePosition.Center)
			return null;

		int count = items.get(position.value()).size();
		if (count == 0)
			return null;

		Item item = items.get(position.value()).get(count - 1);
		items.get(position.value()).remove(count - 1);

		// Call the script
		onCollectedItem(item);

		return item;
	}

	public boolean dropItem(SquarePosition position, Item item) {
		// No item in the middle of a block
		if (position == SquarePosition.Center)
			throw new IllegalArgumentException("position: No items in the middle of a maze block !");

		// Can't drop item in wall
		if (isWall())
			return false;

		// actor refuses items
		if (actor != null && !actor.isAcceptItems())
			return false;

		// Add the item to the ground
		items.get(position.value()).add(item);

		// Call the script
		onDroppedItem(item);

		return true;
	}

	public List<List<Item>> getItems(CardinalPoint location) {
		// List of items
		List<List<Item>> items = new ArrayList<List<Item>>();

		switch (location) {
		case North: {
			items.set(0, this.items.get(0));
			items.set(1, this.items.get(1));
			items.set(2, this.items.get(2));
			items.set(3, this.items.get(3));
		}
			break;
		case East: {
			items.set(0, this.items.get(1));
			items.set(1, this.items.get(3));
			items.set(2, this.items.get(0));
			items.set(3, this.items.get(2));
		}
			break;
		case South: {
			items.set(0, this.items.get(3));
			items.set(1, this.items.get(2));
			items.set(2, this.items.get(1));
			items.set(3, this.items.get(0));
		}
			break;
		case West: {
			items.set(0, this.items.get(2));
			items.set(1, this.items.get(0));
			items.set(2, this.items.get(3));
			items.set(3, this.items.get(1));
		}
			break;
		}

		return items;
	}

	public boolean isBlocking() {
		if (actor != null && actor.isBlocking())
			return true;

		else if (isIllusion())
			return false;

		else if (isWall())
			return true;

		else if (hasMonster())
			return true;

		else if (hasDecorations()) {
			for (CardinalPoint side : CardinalPoint.values()) {
				Decoration deco = maze.getDecoration(location, side);
				if (deco != null && deco.isBlocking())
					return true;
			}
		}

		return false;
	}

	private boolean isIllusion() {
		return type == SquareType.Illusion;
	}

	public boolean hasMonster() {
		return monsterCount > 0;
	}

	public boolean hasDecorations() {
		return (decorations[0] != -1 || decorations[1] != -1 || decorations[2] != -1 || decorations[3] != -1);
	}

	public boolean isWall() {
		return type != SquareType.Ground;
	}

	public int getItemCount() {
		int count = 0;
		for (List<Item> list : items)
			count += list.size();

		return count;
	}

	public SquareActor getActor() {
		return actor;
	}

	public Maze getMaze() {
		return maze;
	}

	public SquareType getType() {
		return type;
	}

	public Monster[] getMonsters() {
		return monsters;
	}

	public int[] getDecorations() {
		return decorations;
	}

	public ArrayList<List<Item>> getItems() {
		return items;
	}

	public int getMonsterCount() {
		return monsterCount;
	}

	public boolean isNoMonster() {
		return noMonster;
	}

	public boolean isNoGhost() {
		return noGhost;
	}

	public Vector2 getLocation() {
		return location;
	}

	public boolean isPitTarget() {
		return pitTarget;
	}

	public void setPitTarget(boolean pitTarget) {
		this.pitTarget = pitTarget;
	}

	public void setLocation(Vector2 location2) {
		this.location = location2;
	}

	public void setMaze(Maze maze2) {
		this.maze = maze2;
	}

	public void setType(SquareType stype) {
		this.type = stype;
	}

	public void setMonster(int index, Monster monster) {
		monsters[index] = monster;
	}

}
