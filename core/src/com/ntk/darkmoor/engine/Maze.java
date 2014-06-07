package com.ntk.darkmoor.engine;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ntk.commons.StringUtils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.config.Log;
import com.ntk.darkmoor.engine.Compass.CardinalPoint;
import com.ntk.darkmoor.engine.Square.SquarePosition;
import com.ntk.darkmoor.engine.ViewField.ViewFieldPosition;
import com.ntk.darkmoor.engine.actor.Door;
import com.ntk.darkmoor.engine.actor.Door.DoorType;
import com.ntk.darkmoor.engine.actor.Pit;
import com.ntk.darkmoor.engine.actor.Stair;
import com.ntk.darkmoor.engine.graphics.TileDrawing;
import com.ntk.darkmoor.resource.Resources;
import com.ntk.darkmoor.stub.Display;
import com.ntk.darkmoor.stub.GameScreen;
import com.ntk.darkmoor.stub.GameTime;
import com.ntk.darkmoor.stub.Monster;
import com.ntk.darkmoor.stub.SpriteEffects;
import com.ntk.darkmoor.stub.TileSet;

public class Maze {

	public static final String TAG = "maze";
	private String name;
	private Dungeon dungeon;
	private ArrayList<List<Square>> squares;
	private ArrayList<ThrownItem> thrownItems;
	private ArrayList<MazeZone> zones;
	private int ceilingPitDeco;
	private int doorDeco;
	private int floorPitDeco;
	private boolean disposed;
	private TileSet wallTileset;
	private String wallTilesetName;
	private String decorationName;
	private DecorationSet decoration;
	private TileSet doorTileset;
	private String description;
	private Vector2 size;
	private Rectangle rectangle;
	private DoorType defaultDoorType;
	private float experienceMultiplier;

	public Maze(Dungeon dungeon) {
		name = "No name";
		this.dungeon = dungeon;

		squares = new ArrayList<List<Square>>();
		thrownItems = new ArrayList<ThrownItem>();
		zones = new ArrayList<MazeZone>();

		doorDeco = -1;
		ceilingPitDeco = -1;
		floorPitDeco = -1;

		disposed = false;
	}

	public boolean init() {
		loadWallTileSet();
		loadDecoration();
		loadDoorTileSet();

		for (List<Square> list : squares)
			for (Square square : list) {
				square.init();

				// Find pit destination squares
				if (square.getActor() instanceof Pit) {
					Pit pit = (Pit) square.getActor();

					if (pit != null && pit.getTarget() != null) {
						// Maze exists
						Maze maze = dungeon.getMaze(pit.getTarget().getMaze());
						if (maze == null)
							continue;

						// Destination exists
						Square blk = maze.getSquare(pit.getTarget().getCoordinates());
						if (blk == null)
							continue;

						blk.setPitTarget(true);
					}
				}
			}

		return true;
	}

	private boolean loadDoorTileSet() {
		if (doorTileset != null)
			doorTileset.dispose();

		doorTileset = Resources.createSharedAsset(TileSet.class, "Doors", "Doors");
		if (doorTileset == null) {
			Log.error("[Maze] Failed to create door tileset.");
			return false;
		}

		return true;
	}

	private boolean loadDecoration() {
		if (StringUtils.isEmpty(decorationName))
			return false;

		if (decoration != null)
			decoration.dispose();

		decoration = Resources.createSharedAsset(DecorationSet.class, decorationName, decorationName);
		if (decoration == null) {
			Log.error("[Maze] Failed to create decoration '" + decorationName + "' for the maze '" + name + "'.");
			return false;
		}

		return true;
	}

	private boolean loadWallTileSet() {
		Resources.unlockSharedAsset(TileSet.class, wallTileset);

		wallTileset = Resources.createSharedAsset(TileSet.class, wallTilesetName, wallTilesetName);
		if (wallTileset == null) {
			Log.error("[Maze] Failed to create wall tileset for the maze \"" + name + "\".");
			return false;
		}

		return true;
	}

	public void dispose() {
		// Dispose each sqaure
		for (List<Square> list : squares)
			for (Square square : list)
				square.dispose();

		Resources.unlockSharedAsset(TileSet.class, doorTileset);
		doorTileset = null;

		Resources.unlockSharedAsset(DecorationSet.class, decoration);
		decoration = null;

		Resources.unlockSharedAsset(TileSet.class, wallTileset);
		wallTileset = null;

		squares.clear();
		description = null;
		dungeon = null;
		thrownItems.clear();
		decorationName = null;
		wallTilesetName = null;
		size = new Vector2();
		zones.clear();
		name = "";

		disposed = true;
	}

	public void update(GameTime time) {
		{
			// TODO: URGENT TOP PRIORITY Update squares
			// Remove this lines (glouton cpu).
			for (List<Square> list : squares)
				for (Square square : list) {
					square.update(time);
				}

			/*
			 * // Remove dead monsters Monsters.RemoveAll( delegate(Monster monster) { // Monster alive if
			 * (!monster.IsDead) return false;
			 * 
			 * // Drop the content of his pocket if (monster.ItemsInPocket.Count > 0) { Square block =
			 * GetBlock(monster.Location.Position);
			 * 
			 * //ItemSet itemset = ResourceManager.CreateSharedAsset<ItemSet>("Main"); foreach (string name in
			 * monster.ItemsInPocket) block.DropItem(monster.Location.Position,
			 * ResourceManager.CreateAsset<Item>(name)); }
			 * 
			 * return true; });
			 */

			// Update flying items
			for (ThrownItem item : thrownItems)
				item.update(time, this);

			// Remove all blocked flying items
			int i = 0;
			// TODO: ntk: make sure the following loop works
			while (i < thrownItems.size()) {
				ThrownItem ti = thrownItems.get(i);
				if (ti.getDistance() > 0) {
					i++;
					continue;
				}

				SquarePosition pos = ti.getLocation().getPosition();
				getSquare(ti.getLocation().getCoordinates()).dropItem(pos, ti.getItem());
				thrownItems.remove(ti);
			}
		}
	}

	// TODO: ntk: Is any monster handling needed here?

	public Decoration getDecoration(Vector2 location, CardinalPoint side) {
		if (location == null || decoration == null)
			return null;

		Square square = getSquare(location);
		if (square == null)
			return null;

		// if a decoration is found on the desired side
		int id = square.getDecorationId(side);
		if (id != -1)
			return decoration.getDecoration(id);

		// If there's a forced decoration
		for (int i = 0; i < 4; i++) {
			id = square.getDecorations()[i];
			if (id == -1)
				continue;

			Decoration deco = decoration.getDecoration(id);
			if (deco != null && deco.isForceDisplay())
				return deco;
		}

		return null;
	}

	public boolean contains(Vector2 pos) {
		// reminder: size is used as width, height
		return new Rectangle(0, 0, size.x, size.y).contains(pos);
	}

	public boolean isDoorNorthSouth(Vector2 location) {
		if (location == null)
			throw new IllegalArgumentException("location");

		Vector2 left = new Vector2(location.x - 1, location.y);
		if (!contains(left))
			return false;

		Square block = getSquare(left);
		return (block.isWall());
	}

	public Square getSquare(Vector2 location) {
		if (!rectangle.contains(location))
			return new Square(this);

		return squares.get((int) location.y).get((int) location.x);
	}

	public boolean setSquare(Vector2 location, Square square) {
		if (square == null || !rectangle.contains(location))
			return false;

		squares.get((int) location.y).set((int) location.x, square);
		square.setLocation(location);
		square.setMaze(this);

		return true;
	}

	/**
	 * Checks if a point is visible from another point ie: is a monster visible from the current point of view of the
	 * team ?
	 * 
	 * @param from
	 * @param to
	 * @param dir
	 * @param distance
	 * @return
	 */
	public boolean isVisible(Vector2 from, Vector2 to, Compass dir, int distance) {
		// TODO: ntk: missing implementation!
		return false;
	}

	/**
	 * Returns all objects flying depending the view point
	 * 
	 * @param location
	 * @param direction
	 *            Looking direction
	 * @return Returns an array of flying items: Position 0 : North West, Position 1 : North East, Position 2 : South
	 *         West, Position 3 : South East
	 */
	public List<List<ThrownItem>> getFlyingItems(Vector2 location, CardinalPoint direction) {

		// TODO: ntk: data structures here are candidates for optimization

		List<List<ThrownItem>> tmp = new ArrayList<List<ThrownItem>>(5);
		tmp.set(0, new ArrayList<ThrownItem>());
		tmp.set(1, new ArrayList<ThrownItem>());
		tmp.set(2, new ArrayList<ThrownItem>());
		tmp.set(3, new ArrayList<ThrownItem>());
		tmp.set(4, new ArrayList<ThrownItem>());

		for (ThrownItem item : thrownItems) {
			// Not in the same maze
			// if (item.Location.Maze != location.Maze)
			// continue;

			// Same coordinate
			if (item.getLocation().getCoordinates() == location)
				tmp.get(item.getLocation().getPosition().value()).add(item);
		}

		// Swap according to view point direction
		List<List<ThrownItem>> items = new ArrayList<List<ThrownItem>>(5);
		switch (direction) {
		case North: {
			items.set(0, tmp.get(0));
			items.set(1, tmp.get(1));
			items.set(2, tmp.get(2));
			items.set(3, tmp.get(3));
		}
			break;
		case East: {
			items.set(0, tmp.get(1));
			items.set(1, tmp.get(3));
			items.set(2, tmp.get(0));
			items.set(3, tmp.get(2));
		}
			break;
		case South: {
			items.set(0, tmp.get(3));
			items.set(1, tmp.get(2));
			items.set(2, tmp.get(1));
			items.set(3, tmp.get(0));
		}
			break;
		case West: {
			items.set(0, tmp.get(2));
			items.set(1, tmp.get(0));
			items.set(2, tmp.get(3));
			items.set(3, tmp.get(1));
		}
			break;
		}

		items.set(4, tmp.get(4));

		return items;
	}

	public boolean load(Element xml) {
		if (xml == null || !TAG.equalsIgnoreCase(xml.getName()))
			return false;

		Square block = null;

		Element node = null;
		String name = null;
		
		for (int i = 0; i < xml.getChildCount(); i++) {
			node = xml.getChild(i);
			name = node.getName();

			if ("tileset".equalsIgnoreCase(name)) {
				wallTilesetName = node.getAttribute("wall");
				decorationName = node.getAttribute("decoration");

			} else if ("description".equalsIgnoreCase(name)) {
				description = node.getText();

			} else if ("flyingitems".equalsIgnoreCase(name)) {
				for (int j = 0; j < node.getChildCount(); j++) {
					Element subnode = node.getChild(j);
					ThrownItem item = new ThrownItem(null);
					item.load(subnode);
					thrownItems.add(item);
				}

			} else if (MazeZone.TAG.equalsIgnoreCase(name)) {
				MazeZone zone = new MazeZone();
				zone.load(node);
				zones.add(zone);

			} else if ("decorations".equalsIgnoreCase(name)) {
				floorPitDeco = Integer.parseInt(node.getAttribute("floorpit"));
				ceilingPitDeco = Integer.parseInt(node.getAttribute("ceilingpit"));
				doorDeco = Integer.parseInt(node.getAttribute("door"));

			} else if ("doors".equalsIgnoreCase(name)) {
				defaultDoorType = DoorType.valueOf(node.getAttribute("type"));

			} else if ("squares".equalsIgnoreCase(name)) {

				// Resize maze
				size = new Vector2(Integer.parseInt(node.getAttribute("width")), Integer.parseInt(node
						.getAttribute("height")));
				Vector2 location = new Vector2();

				for (int j = 0; j < node.getChildCount(); j++) {
					Element subnode = node.getChild(j);
					if (Square.TAG.equalsIgnoreCase(subnode.getName())) {
						// Add a row
						block = new Square(this);
						block.setLocation(location);
						block.load(subnode);

						squares.get((int) location.y).set((int) location.x, block);
					}

					// Next location
					location.x++;
					if (location.x == size.x) {
						location.y++;
						location.x = 0;
					}
				}
			}
		}

		return true;
	}

	public boolean save(XmlWriter writer) throws IOException {
		if (writer == null)
			return false;

		writer.element(TAG).attribute("name", name);

		// Tilesets
		writer.element("tileset").attribute("wall", wallTilesetName).attribute("decoration", decorationName).pop();

		writer.element("description").text(description).pop();

		writer.element("doors").attribute("type", defaultDoorType.toString()).pop();

		writer.element("decorations").attribute("ceilingpit", ceilingPitDeco).attribute("floorpit", floorPitDeco)
				.attribute("door", doorDeco).pop();

		writer.element("squares").attribute("width", size.x).attribute("y", size.y).pop();

		for (List<Square> list : squares) {
			for (Square square : list) {
				square.save(writer);
			}
		}

		writer.pop();

		// Zones
		for (MazeZone zone : zones)
			zone.save(writer);

		// flying items
		if (!thrownItems.isEmpty()) {
			writer.element("flyingitems");
			for (ThrownItem item : thrownItems)
				item.save(writer);
			writer.pop();
		}

		writer.pop();

		return true;
	}

	public void draw(SpriteBatch batch, DungeonLocation location) {
		if (wallTileset == null)
			return;

		// TODO: ntk: scissor handling according to Gdx method
		// Clear the spritebatch
		batch.end();
		Display.pushScissor(new Rectangle(0, 0, 352, 240));
		batch.begin();

		ViewField pov = new ViewField(this, location);

		// TODO Backdrop
		// The background is assumed to be x-flipped when party.x & party.y & party.direction = 1.
		// I.e. all kind of moves and rotations from the current position will result in the background being x-flipped.
		boolean flipbackdrop = ((int) (location.getCoordinates().x + location.getCoordinates().y + location
				.getDirection().value()) & 1) == 0;
		SpriteEffects effect = flipbackdrop ? SpriteEffects.FLIP_HORIZONTALLY : SpriteEffects.NONE;

		// TODO: ntk: draw the texture
		// batch.drawTile(wallTileset, 0, new Vector2(), Color.WHITE, 0.0f, effect, 0.0f);

		// maze block draw order
		// A E B D C
		// F J G I H
		// K M L
		// N ^ O

		// row -3
		drawSquare(batch, pov, ViewFieldPosition.A, location.getDirection());
		drawSquare(batch, pov, ViewFieldPosition.E, location.getDirection());
		drawSquare(batch, pov, ViewFieldPosition.B, location.getDirection());
		drawSquare(batch, pov, ViewFieldPosition.D, location.getDirection());
		drawSquare(batch, pov, ViewFieldPosition.C, location.getDirection());

		// row -2
		drawSquare(batch, pov, ViewFieldPosition.F, location.getDirection());
		drawSquare(batch, pov, ViewFieldPosition.J, location.getDirection());
		drawSquare(batch, pov, ViewFieldPosition.G, location.getDirection());
		drawSquare(batch, pov, ViewFieldPosition.I, location.getDirection());
		drawSquare(batch, pov, ViewFieldPosition.H, location.getDirection());

		// row -1
		drawSquare(batch, pov, ViewFieldPosition.K, location.getDirection());
		drawSquare(batch, pov, ViewFieldPosition.M, location.getDirection());
		drawSquare(batch, pov, ViewFieldPosition.L, location.getDirection());

		// row 0
		drawSquare(batch, pov, ViewFieldPosition.N, location.getDirection());
		drawSquare(batch, pov, ViewFieldPosition.Team, location.getDirection());
		drawSquare(batch, pov, ViewFieldPosition.O, location.getDirection());

		// Clear the spritebatch
		batch.end();
		Display.popScissor();
		batch.begin();

	}

	private void drawSquare(SpriteBatch batch, ViewField field, ViewFieldPosition position, CardinalPoint view) {
		if (field == null)
			return;

		Square square = field.getBlock(position);
		Vector2 point;
		Decoration deco = null;
		List<List<Item>> list = square.getItems(view);

		// ceiling pit
		if (square.isPitTarget()) {
			// TODO
			TileDrawing td = DisplayCoordinates.getCeilingPit(position);
			// if (td != null)
			// batch.DrawTile(OverlayTileset, td.ID, td.Location, Color.White, 0.0f, td.Effect, 0.0f);
			// ***batch.DrawTile(ItemsTileset, td.ID, td.Location, td.SwapX, td.SwapY);
		}

		// Items on ground before a door
		// If there is a deco that hide ground items, skip
		deco = getDecoration(square.getLocation(), Compass.getOppositeDirection(view));
		if (deco == null || (deco != null && !deco.isHideItems())) {
			if (!square.isWall() || (deco != null && !deco.isHideItems())) {
				for (int i = 0; i < 2; i++) {
					if (list.get(i).size() == 0)
						continue;

					for (Item item : list.get(i)) {
						point = DisplayCoordinates.getGroundPosition(position, SquarePosition.valueOf(i));
						if (point.x != 0 || point.y != 0) // not is empty
						{
							// TODO: ntk: draw the texture
							// batch.drawTile(dungeon.getItemTileSet(), item.getGroundTileID(), point,
							// DisplayCoordinates.getDistantColor(position), 0.0f,
							// DisplayCoordinates.getItemScaleFactor(position), SpriteEffects.NONE, 0.0f);
						}
					}
				}
			}
		}

		// Walls
		if (square.isWall()) {
			// Walls
			for (TileDrawing tmp : DisplayCoordinates.getWalls(position)) {
				Color color = Color.WHITE;
				int tileid = tmp.getID();

				// if (swap)
				// {
				// color = Color.Red;
				// tileid += 9;
				// }

				// TODO: ntk: draw the texture
				// batch.drawTile(wallTileset, tileid, tmp.getLocation(), color, 0.0f, tmp.getEffect(), 0.0f);
			}
		}

		// Decoration
		if (square.hasDecorations()) {
			// Is there a forced decoration
			for (int i = 0; i < 4; i++) {
				deco = decoration.getDecoration(square.getDecorationId(CardinalPoint.valueOf(i)));
				if (deco != null && deco.isForceDisplay())
					deco.drawDecoration(batch, decoration, position, true);
			}

			// For each directions, draws the decoration
			for (CardinalPoint side : DisplayCoordinates.getDrawingWallSides()[position.value()]) {
				// Decoration informations
				deco = decoration.getDecoration(square.getDecorationId(view, side));
				if (deco == null)
					continue;

				deco.drawDecoration(batch, decoration, position, side == CardinalPoint.South);
			}
		}

		// Actor
		if (square.getActor() != null)
			square.getActor().draw(batch, field, position, view);

		// Items on ground after a door
		if (!square.isWall()) {
			// If there is a deco that hide ground items, skip
			deco = getDecoration(square.getLocation(), Compass.getOppositeDirection(view));
			if (deco == null || (deco != null && !deco.isHideItems())) {
				// Both ground positions
				for (int i = 2; i < 4; i++) {
					// No items
					if (list.get(i).isEmpty())
						continue;

					// Foreach item on the ground
					for (Item item : list.get(i)) {
						// Get screen coordinate
						point = DisplayCoordinates.getGroundPosition(position, SquarePosition.valueOf(i));
						if (point.x != 0 || point.y != 0) // not is empty
						{
							// TODO: ntk: draw the texture
							// batch.drawTile(dungeon.getItemTileSet(), item.getGroundTileID(), point,
							// DisplayCoordinates.getDistantColor(position), 0.0f,
							// DisplayCoordinates.getItemScaleFactor(position), SpriteEffects.NONE, 0.0f);
						}
					}
				}
			}
		}

		// Monsters
		if (square.getMonsterCount() > 0) {
			// Drawing order for monsters
			int[][] order = new int[][] { new int[] { 0, 1, 2, 3 }, // North
					new int[] { 3, 2, 1, 0 }, // South
					new int[] { 2, 0, 3, 1 }, // West
					new int[] { 1, 3, 0, 2 }, // East
			};

			for (int i = 0; i < 4; i++) {
				Monster monster = square.getMonster(SquarePosition.valueOf(order[view.value()][i]));
				if (monster != null)
					monster.draw(batch, view, position);
			}
		}

		// Flying items
		List<List<ThrownItem>> flyings = getFlyingItems(square.getLocation(), view);
		for (SquarePosition pos : SquarePosition.values()) {
			point = DisplayCoordinates.getFlyingItem(position, pos);
			// if (point == Point.Empty)
			// continue;

			// Swap the tile if throwing on the right side
			SpriteEffects fx = SpriteEffects.NONE;
			if (pos == SquarePosition.NorthEast || pos == SquarePosition.SouthEast)
				fx = SpriteEffects.FLIP_HORIZONTALLY;

			for (ThrownItem fi : flyings.get(pos.value())) {
				// TODO: ntk: draw the texture
				// batch.drawTile(dungeon.getItemTileSet(), fi.getItem().getThrowTileID(), point,
				// DisplayCoordinates.getDistantColor(position), 0.0f,
				// DisplayCoordinates.getItemScaleFactor(position), fx, 0.0f);
			}
		}
	}

	public void drawMiniMap(SpriteBatch batch, Point location) {
		if (batch == null)
			return;

		Team team = GameScreen.getTeam();

		Color color;

		for (int y = 0; y < size.y; y++)
			for (int x = 0; x < size.x; x++) {
				Square block = getSquare(new Vector2(x, y));

				switch (block.getType()) {
				case Wall:
					color = Color.BLACK;
					break;
				case Illusion:
					color = Color.GRAY;
					break;
				default:
					color = Color.WHITE;
					break;
				}

				if (block.getMonsterCount() > 0)
					color = GameColors.Red;
				if (block.getActor() instanceof Door)
					color = GameColors.Yellow;
				if (block.getActor() instanceof Pit)
					color = GameColors.DarkBlue;
				if (block.getActor() instanceof Stair)
					color = GameColors.LightGreen;

				if (team.getLocation().getCoordinates().x == x && team.getLocation().getCoordinates().y == y
						&& team.getMaze() == this)
					color = GameColors.Blue;

				// TODO: ntk: draw the rectangle with a ShapeRenderer
				// batch.fillRectangle(new Rectangle(location.x + x * 4, location.y + y * 4, 4, 4), color);

			}
	}

	public void resize(Vector2 newsize) {
		// Rows
		if (newsize.y > size.y) {
			for (int y = (int) size.y; y < newsize.y; y++)
				insertRow(y);
		} else if (newsize.y < size.y) {
			for (int y = (int) size.y - 1; y >= newsize.y; y--)
				removeRow(y);
		}

		// Columns
		if (newsize.x > size.x) {
			for (int x = (int) size.x; x < newsize.x; x++)
				insertColumn(x);
		} else if (newsize.x < size.x) {
			for (int x = (int) size.x - 1; x >= newsize.x; x--)
				removeColumn(x);
		}

		size = newsize;

		for (int y = 0; y < size.y; y++)
			for (int x = 0; x < size.x; x++) {
				squares.get(y).get(x).setLocation(new Vector2(x, y));
			}
	}

	private void removeColumn(int columnid) {
		// Remove the column
		for (List<Square> row : squares) {
			row.remove(columnid);
		}

		// Offset objects
		// Rectangle zone = new Rectangle(columnid * level.BlockDimension.Width, 0, level.Dimension.Width,
		// level.Dimension.Height);
		// OffsetObjects(zone, new Point(-level.BlockDimension.Width, 0));

	}

	private void insertColumn(int columnid) {
		// Insert the column
		for (List<Square> row : squares) {
			if (columnid >= row.size()) {
				row.add(new Square(this));
			} else {
				row.add(columnid, new Square(this));

				// Offset objects
				// Rectangle zone = new Rectangle(columnid * level.BlockDimension.Width, 0, level.Dimension.Width,
				// level.Dimension.Height);
				// OffsetObjects(zone, new Point(level.BlockDimension.Width, 0));
			}
		}
	}

	private void removeRow(int rowid) {
		// Removes the row
		if (rowid >= squares.size())
			return;

		squares.remove(rowid);

		// Offset objects
		// Rectangle zone = new Rectangle(0, rowid * level.BlockDimension.Height, level.Dimension.Width,
		// level.Dimension.Height);
		// OffsetObjects(zone, new Point(0, -level.BlockDimension.Height));

	}

	private void insertRow(int rowid) {
		// Build the row
		List<Square> row = new ArrayList<Square>((int) size.x);
		for (int x = 0; x < size.x; x++)
			row.add(new Square(this));

		// Adds the row at the end
		if (rowid >= squares.size()) {
			squares.add(row);
		}

		// Or insert the row
		else {
			squares.add(rowid, row);

			// Offset objects
			// Rectangle zone = new Rectangle(0, rowid * level.BlockDimension.Height, level.Dimension.Width,
			// level.Dimension.Height);
			// OffsetObjects(zone, new Point(0, level.BlockDimension.Height));
		}
	}

	/**
	 * Offsets EVERY object (monsters, items...) in the maze
	 * 
	 * @param zone
	 *            Each object in this rectangle
	 * @param offset
	 *            Offset to move
	 */
	void OffsetObjects(Rectangle zone, Vector2 offset) {
		// TODO: ntk: not implemented or not needed?
		/*
		 * // Move entities foreach (Entity entity in Entities.Values) { if (zone.Contains(entity.Location)) {
		 * entity.Location = new Point( entity.Location.X + offset.X, entity.Location.Y + offset.Y); } }
		 * 
		 * 
		 * // Mode SpawnPoints foreach (SpawnPoint spawn in SpawnPoints.Values) { if (zone.Contains(spawn.Location)) {
		 * spawn.Location = new Point( spawn.Location.X + offset.X, spawn.Location.Y + offset.Y); } }
		 */
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCeilingPitDeco() {
		return ceilingPitDeco;
	}

	public void setCeilingPitDeco(int ceilingPitDeco) {
		this.ceilingPitDeco = ceilingPitDeco;
	}

	public int getDoorDeco() {
		return doorDeco;
	}

	public void setDoorDeco(int doorDeco) {
		this.doorDeco = doorDeco;
	}

	public int getFloorPitDeco() {
		return floorPitDeco;
	}

	public void setFloorPitDeco(int floorPitDeco) {
		this.floorPitDeco = floorPitDeco;
	}

	public String getWallTilesetName() {
		return wallTilesetName;
	}

	public void setWallTilesetName(String wallTilesetName) {
		this.wallTilesetName = wallTilesetName;
	}

	public String getDecorationName() {
		return decorationName;
	}

	public void setDecorationName(String decorationName) {
		this.decorationName = decorationName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Vector2 getSize() {
		return size;
	}

	public void setSize(Vector2 size) {
		this.size = size;
	}

	public Dungeon getDungeon() {
		return dungeon;
	}

	public ArrayList<List<Square>> getSquares() {
		return squares;
	}

	public ArrayList<ThrownItem> getThrownItems() {
		return thrownItems;
	}

	public ArrayList<MazeZone> getZones() {
		return zones;
	}

	public boolean isDisposed() {
		return disposed;
	}

	public TileSet getWallTileset() {
		return wallTileset;
	}

	public DecorationSet getDecoration() {
		return decoration;
	}

	public TileSet getDoorTileset() {
		return doorTileset;
	}

	public Rectangle getRectangle() {
		return rectangle;
	}

	public DoorType getDefaultDoorType() {
		return defaultDoorType;
	}

	public float getExperienceMultiplier() {
		return experienceMultiplier;
	}

	public void setExperienceMultiplier(float experienceMultiplier) {
		this.experienceMultiplier = experienceMultiplier;
	}

	public DecorationSet geDecoration() {
		return decoration;
	}

}
