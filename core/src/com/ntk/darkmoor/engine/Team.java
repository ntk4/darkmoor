package com.ntk.darkmoor.engine;

import java.io.IOException;
import java.util.Date;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.GameScreen;
import com.ntk.darkmoor.engine.Compass.CardinalPoint;
import com.ntk.darkmoor.engine.Square.SquarePosition;
import com.ntk.darkmoor.engine.Square.SquareType;
import com.ntk.darkmoor.stub.Mouse;

public class Team implements Disposable {

	public enum HeroPosition {
		FrontLeft(0), FrontRight(1), MiddleLeft(2), MiddleRight(3), RearLeft(4), RearRight(5);

		private int value;

		private HeroPosition(int value) {
			this.value = value;
		}

		public int value() {
			return value;
		}

		public static HeroPosition valueOf(int value) {
			switch (value) {
			case 0:
				return FrontLeft;
			case 1:
				return FrontRight;
			case 2:
				return MiddleLeft;
			case 3:
				return MiddleRight;
			case 4:
				return RearLeft;
			case 5:
				return RearRight;
			}
			return FrontLeft;
		}
	}

	public static final String TAG = "Team";

	public static final int MAX_HEROES = 6;

	private DungeonLocation location;
	private int teamSpeed;
	private Hero[] heroes;
	private Hero selectedHero;
	private CardinalPoint direction;
	private Square frontSquare;
	private Item itemInHand;
	private Maze maze;
	private boolean canMove;
	private Square square;
	private long lastMove;
	private boolean hasMoved;

	public Team() {
		teamSpeed = 150;

		heroes = new Hero[6];
		for (int i = 0; i < 6; i++)
			heroes[i] = null;
	}

	public boolean init() {

		// Set initial location
		if (location == null) {
			location = new DungeonLocation();
			teleport(GameScreen.getDungeon().getStartLocation());
			location.setDirection(GameScreen.getDungeon().getStartLocation().getDirection());
		} else {
			teleport(location);
		}

		// Select the first hero
		selectedHero = heroes[0];

		return true;
	}

	/**
	 * Gets the entity in front the team at a given sqaure position
	 * 
	 * @param position
	 * @return
	 */
	public Entity getFrontEntity(SquarePosition position) {
		// Center position
		if (position == SquarePosition.Center)
			return null;

		int[][] id = new int[][] {
				new int[] {
						2, 2, 1, 1 }, // From NW
				new int[] {
						3, 3, 0, 0 }, // From NE
				new int[] {
						0, 0, 3, 3 }, // From SW
				new int[] {
						1, 1, 2, 2 }, // From SE
		};

		SquarePosition pos = SquarePosition.valueOf(id[position.value()][direction.value()]);

		return frontSquare.getMonster(pos);
	}

	public boolean load(Element xml) {
		if (xml == null || !"team".equalsIgnoreCase(xml.getName()))
			return false;

		for (int i = 0; i < 6; i++)
			heroes[i] = null;

		String name = null;
		for (int i = 0; i < xml.getChildCount(); i++) {
			Element child = xml.getChild(i);
			name = child.getName();

			if ("location".equalsIgnoreCase(name)) {
				location = new DungeonLocation(child);

			} else if ("position".equalsIgnoreCase(name)) {
				HeroPosition position = HeroPosition.valueOf(child.getAttribute("slot"));
				Hero hero = new Hero();
				if (child.getChildCount() > 0)
					hero.load(child.getChild(0));
				addHero(hero, position);

			} else if ("message".equalsIgnoreCase(name)) {
				GameMessage.addMessage(child.getAttribute("text"), GameColors.getColorFromArgb(
						Integer.parseInt(child.getAttribute("A")), Integer.parseInt(child.getAttribute("R")),
						Integer.parseInt(child.getAttribute("G")), Integer.parseInt(child.getAttribute("B"))));

			}

		}

		selectedHero = heroes[0];
		return true;
	}

	public Element save(XmlWriter writer) throws IOException {
		if (writer == null)
			return null;

		writer.element("team");

		location.save("location", writer);

		for (Hero hero : heroes) {
			if (hero != null) {
				writer.element("position").attribute("slot", getHeroPosition(hero).toString());
				hero.save(writer);
				writer.pop();
			}
		}

		for (ScreenMessage message : GameMessage.getMessages()) {
			writer.element("message").attribute("text", message.getMessage());
			writer.attribute("R", message.getColor().r);
			writer.attribute("G", message.getColor().g);
			writer.attribute("B", message.getColor().b);
			writer.attribute("A", message.getColor().a);
			writer.pop();
		}

		writer.pop();

		// TODO: ntk: tpo be tested!
		String xml = writer.toString();

		XmlReader reader = new XmlReader();
		return reader.parse(xml);
	}

	public void SetItemInHand(Item item) {
		// Set the item in the hand
		itemInHand = item;

		// Display a message
		if (itemInHand != null) {
			GameMessage.buildMessage(2, itemInHand.getName());

			// Change cursor
			Mouse.setTile(item.getTextureID());
		} else {
			Mouse.setTile(0);
		}
	}

	/**
	 * Does the Team can see this place // / http://tom.cs.byu.edu/~455/3DDDA.pdf
	 * http://www.tar.hu/gamealgorithms/ch22lev1sec1.html http://www.cse.yorku.ca/~amana/research/grid.pdf
	 * http://www.siggraph.org/education/materials/HyperGraph/scanline/outprims/drawline.htm#dda
	 * 
	 * @param maze
	 * @param location
	 * @return
	 */
	public boolean canSee(Maze maze, Vector2 location) {
		// Not the same maze
		if (this.maze != maze)
			return false;

		switch (this.location.getDirection()) {
		case North: {
			if (!new Rectangle(location.x - 1, location.y - 3, 3, 4).contains(location)
					&& location != new Vector2(location.x - 3, location.y - 3)
					&& location != new Vector2(location.x - 2, location.y - 3)
					&& location != new Vector2(location.x - 2, location.y - 2)
					&& location != new Vector2(location.x + 3, location.y - 3)
					&& location != new Vector2(location.x + 2, location.y - 3)
					&& location != new Vector2(location.x + 2, location.y - 2))
				return false;

			// Is there a wall between the Team and the location
			int dx = (int) (location.x - location.x);
			int dy = (int) (location.y - location.y);
			float delta = (float) dy / (float) dx;
			float y = 0;
			for (int pos = (int) location.y; pos >= location.y; pos--) {
				if (maze.getSquare(new Vector2(pos, location.y + (int) y)).getType() == SquareType.Wall)
					return false;

				y += delta;
			}

			return true;
		}

		case South: {
			if (!new Rectangle(location.x - 1, location.y, 3, 4).contains(location)
					&& location != new Vector2(location.x - 3, location.y + 3)
					&& location != new Vector2(location.x - 2, location.y + 3)
					&& location != new Vector2(location.x - 2, location.y + 2)
					&& location != new Vector2(location.x + 3, location.y + 3)
					&& location != new Vector2(location.x + 2, location.y + 3)
					&& location != new Vector2(location.x + 2, location.y + 2))
				return false;

			// Is there a wall between the Team and the location
			int dx = (int) (location.x - location.x);
			int dy = (int) (location.y - location.y);
			float delta = (float) dy / (float) dx;
			float y = 0;
			for (int pos = (int) location.y; pos <= location.y; pos++) {
				if (maze.getSquare(new Vector2(pos, location.y + (int) y)).getType() == SquareType.Wall)
					return false;

				y += delta;
			}

			return true;
		}

		case West: {
			if (!new Rectangle(location.x - 3, location.y - 1, 4, 3).contains(location)
					&& location != new Vector2(location.x - 3, location.y - 3)
					&& location != new Vector2(location.x - 3, location.y - 2)
					&& location != new Vector2(location.x - 2, location.y - 2)
					&& location != new Vector2(location.x - 3, location.y + 3)
					&& location != new Vector2(location.x - 3, location.y + 2)
					&& location != new Vector2(location.x - 2, location.y + 2))
				return false;

			// Is there a wall between the Team and the location
			int dx = (int) (location.x - location.x);
			int dy = (int) (location.y - location.y);
			float delta = (float) dy / (float) dx;
			float y = 0;
			for (int pos = (int) location.x; pos >= location.x; pos--) {
				if (maze.getSquare(new Vector2(pos, location.y + (int) y)).getType() == SquareType.Wall)
					return false;

				y += delta;
			}

			return true;
		}

		case East: {
			if (!new Rectangle(location.x, location.y - 1, 4, 3).contains(location)
					&& location != new Vector2(location.x + 3, location.y - 3)
					&& location != new Vector2(location.x + 3, location.y - 2)
					&& location != new Vector2(location.x + 2, location.y - 2)
					&& location != new Vector2(location.x + 3, location.y + 3)
					&& location != new Vector2(location.x + 3, location.y + 2)
					&& location != new Vector2(location.x + 2, location.y + 2))
				return false;

			// Is there a wall between the Team and the location
			int dx = (int) (location.x - location.x);
			int dy = (int) (location.y - location.y);
			float delta = (float) dy / (float) dx;
			float y = 0;
			for (int pos = (int) location.x; pos <= location.x; pos++) {
				if (maze.getSquare(new Vector2(pos, location.y + (int) y)).getType() == SquareType.Wall)
					return false;

				y += delta;
			}

			return true;
		}

		}

		return false;
	}

	/**
	 * Returns the distance between the Team and a location
	 * 
	 * @param location
	 * @return
	 */
	public Vector2 Distance(Vector2 location) {
		return new Vector2(location.x - location.x, location.y - location.y);
	}

	/**
	 * Move the team according its facing direction
	 * 
	 * @param front MoveForward / backward offset
	 * @param strafe Left / right offset
	 * @return True if move allowed, otherwise false
	 */
	public boolean walk(int front, int strafe) {

		switch (location.getDirection()) {
		case North:
			return move(new Vector2(front, strafe));

		case South:
			return move(new Vector2(-front, -strafe));

		case East:
			return move(new Vector2(-strafe, front));

		case West:
			return move(new Vector2(strafe, -front));
		}

		return false;
	}

	/**
	 * Move team despite the direction the team is facing (useful for forcefield)
	 * 
	 * @param direction Direction of the move
	 * @param count Number of square
	 */
	public void offset(CardinalPoint direction, int count) {
		Vector2 offset = null;

		switch (direction) {
		case North:
			offset = new Vector2(0, -1);
			break;
		case South:
			offset = new Vector2(0, 1);
			break;
		case West:
			offset = new Vector2(-1, 0);
			break;
		case East:
			offset = new Vector2(1, 0);
			break;
		}

		move(offset);
	}

	// / Move the team
	// / </summary>
	// / <param name="offset">Step offset</param>
	// / <returns>True if the team moved, or false</returns>
	private boolean move(Vector2 offset) {
		// Can't move and force is false
		if (!canMove)
			return false;

		// Get informations about the destination square
		Vector2 dst = location.getCoordinates();
		dst.add(offset);

		// Check all blocking states
		boolean state = true;

		// Is blocking
		Square dstsquare = maze.getSquare(dst);
		if (dstsquare.isBlocking())
			state = false;

		// Monsters
		if (dstsquare.getMonsterCount() > 0)
			state = false;

		// If can't pass through
		if (!state) {
			GameMessage.buildMessage(1);
			return false;
		}

		// Leave the current square
		if (square != null)
			square.onTeamLeave();

		location.getCoordinates().add(offset);
		lastMove = new Date().getTime();
		hasMoved = true;

		// Enter the new square
		square = maze.getSquare(location.getCoordinates());
		if (square != null)
			square.onTeamEnter();

		return true;
	}

	/**
	 * Teleport the team to a new maze
	 * 
	 * @param name name of the maze
	 * @return true on success
	 */
	public boolean teleport(String name) {
		DungeonLocation loc = new DungeonLocation(name, location.getCoordinates());
		return teleport(loc);
	}

	/**
	 * Teleports the team into the current dungeon
	 * 
	 * @param location Location in the dungeon
	 * @return True if teleportation is ok
	 */
	public boolean teleport(DungeonLocation location) {
		return teleport(location, GameScreen.getDungeon());
	}

	/**
	 * Teleport the team to a new location, but don't change direction
	 * 
	 * @param location Location in the dungeon
	 * @param dungeon True if teleportion is ok
	 * @return
	 */
	public boolean teleport(DungeonLocation location, Dungeon dungeon) {
		if (dungeon == null || location == null)
			return false;

		// Destination maze
		Maze maze = dungeon.getMaze(location.getMaze());
		if (maze == null)
			return false;

		this.maze = maze;

		// Leave current square
		if (square != null)
			square.onTeamLeave();

		// Change location
		location.setCoordinates(location.getCoordinates());
		location.setMaze(maze.getName());
		location.setDirection(location.getDirection());

		// New block
		square = maze.getSquare(location.getCoordinates());

		// Enter new block
		square.onTeamEnter();

		return true;
	}

	/**
	 * Make damage to the whole team
	 * 
	 * @param damage attack roll
	 * @param type type of saving throw
	 * @param difficulty difficulty
	 */
	public void damage(Dice damage, SavingThrowType type, int difficulty) {
		for (Hero hero : heroes)
			if (hero != null)
				hero.damage(damage, type, difficulty);
	}

	/**
	 * Add experience to the whole team
	 * 
	 * @param amount Amount to be distributed among the entire team
	 */
	public void addExperience(int amount) {
		if (amount == 0)
			return;

		int value = amount / getHeroCount();
		for (Hero hero : heroes)
			if (hero != null)
				hero.addExperience(value);
	}

	/**
	 * Returns next hero in the team
	 * 
	 * @return
	 */
	public Hero getNextHero() {
		int i = 0, heroCount = getHeroCount();
		for (i = 0; i < heroCount; i++) {
			if (heroes[i] == selectedHero) {
				i++;
				if (i == heroCount)
					i = 0;

				break;
			}
		}

		return heroes[i];
	}

	/**
	 * Returns previous hero
	 * 
	 * @return
	 */
	public Hero getPreviousHero() {
		int i = 0, heroCount = getHeroCount();
		for (i = 0; i < heroCount; i++) {
			if (heroes[i] == selectedHero) {
				i--;
				if (i < 0)
					i = heroCount - 1;

				break;
			}
		}

		return heroes[i];
	}

	/**
	 * Returns the position of a hero in the team
	 * 
	 * @param hero
	 * @return
	 */
	private HeroPosition getHeroPosition(Hero hero) {
		int pos = -1;

		for (int id = 0; id < heroes.length; id++) {
			if (heroes[id] == hero) {
				pos = id;
				break;
			}
		}

		if (pos == -1)
			throw new IllegalArgumentException("hero");

		return HeroPosition.valueOf(pos);
	}

	/**
	 * Gets if the hero is in front row
	 * 
	 * @param hero
	 * @return
	 */
	public boolean isHeroInFront(Hero hero) {
		return getHeroFromPosition(HeroPosition.FrontLeft) == hero
				|| getHeroFromPosition(HeroPosition.FrontRight) == hero;
	}

	// / Returns the ground position of a hero
	// / </summary>
	// / <param name="Hero">Hero handle</param>
	// / <returns>Ground position of the hero</returns>
	public SquarePosition getHeroGroundPosition(Hero Hero) {
		SquarePosition groundpos = SquarePosition.Center;

		// Get the hero position in the team
		HeroPosition pos = getHeroPosition(Hero);

		switch (location.getDirection()) {
		case North: {
			if (pos == HeroPosition.FrontLeft)
				groundpos = SquarePosition.NorthWest;
			else if (pos == HeroPosition.FrontRight)
				groundpos = SquarePosition.NorthEast;
			else if (pos == HeroPosition.MiddleLeft || pos == HeroPosition.RearLeft)
				groundpos = SquarePosition.SouthWest;
			else
				groundpos = SquarePosition.SouthEast;
		}
			break;
		case East: {
			if (pos == HeroPosition.FrontLeft)
				groundpos = SquarePosition.NorthEast;
			else if (pos == HeroPosition.FrontRight)
				groundpos = SquarePosition.SouthEast;
			else if (pos == HeroPosition.MiddleLeft || pos == HeroPosition.RearLeft)
				groundpos = SquarePosition.NorthWest;
			else
				groundpos = SquarePosition.SouthWest;
		}
			break;
		case South: {
			if (pos == HeroPosition.FrontLeft)
				groundpos = SquarePosition.SouthEast;
			else if (pos == HeroPosition.FrontRight)
				groundpos = SquarePosition.SouthWest;
			else if (pos == HeroPosition.MiddleLeft || pos == HeroPosition.RearLeft)
				groundpos = SquarePosition.NorthEast;
			else
				groundpos = SquarePosition.NorthWest;
		}
			break;
		case West: {
			if (pos == HeroPosition.FrontLeft)
				groundpos = SquarePosition.SouthWest;
			else if (pos == HeroPosition.FrontRight)
				groundpos = SquarePosition.NorthWest;
			else if (pos == HeroPosition.MiddleLeft || pos == HeroPosition.RearLeft)
				groundpos = SquarePosition.SouthEast;
			else
				groundpos = SquarePosition.NorthEast;
		}
			break;
		}

		return groundpos;
	}

	/**
	 * Returns the Hero at a given position in the team
	 * 
	 * @param pos
	 * @return
	 */
	public Hero getHeroFromPosition(HeroPosition pos) {
		return heroes[pos.value()];
	}

	/**
	 * Returns the Hero under the mouse location
	 * 
	 * @param location Screen location
	 * @return Hero handle or null
	 */
	public Hero getHeroFromLocation(Vector2 location) {

		for (int y = 0; y < 3; y++)
			for (int x = 0; x < 2; x++) {
				// find the hero under the location
				if (new Rectangle(366 + x * 144, 2 + y * 104, 130, 104).contains(location))
					return heroes[y * 2 + x];
			}

		return null;
	}

	public void dropHero(HeroPosition position) {
		heroes[position.value()] = null;
		reorderHeroes();
	}

	/**
	 * Removes a hero from the team
	 * 
	 * @param hero the hero to remove
	 */
	public void dropHero(Hero hero) {
		if (hero == null)
			return;

		for (int i = 0; i < heroes.length; i++) {
			if (heroes[i] == hero) {
				heroes[i] = null;
				reorderHeroes();
				return;
			}
		}
	}

	public void reorderHeroes() {
		int i = 0;
		Hero[] newheroes = new Hero[6];
		for (Hero hero : heroes) {
			if (hero != null) {
				newheroes[i] = hero;
				heroes[i++] = null;
			}

		}
		heroes = newheroes;
	}

	public void addHero(Hero hero, HeroPosition position) {
		heroes[position.value()] = hero;
	}

	public DungeonLocation getLocation() {
		return location;
	}

	public void setLocation(DungeonLocation location) {
		this.location = location;
	}

	public boolean isDead() {
		for (Hero hero : heroes) {
			if (hero != null && !hero.isDead())
				return false;
		}
		return true;
	}

	public boolean isUnconscious() {
		for (Hero hero : heroes) {
			if (hero != null && !hero.isUnconscious())
				return false;
		}
		return true;
	}

	public boolean canMove() {
		if (lastMove + teamSpeed > new Date().getTime())
			return false;

		return true;
	}

	public int getHeroCount() {
		if (heroes == null)
			return 0;

		int count = 0;
		for (Hero hero : heroes) {
			if (hero != null)
				count++;
		}

		return count;
	}

	public CardinalPoint getFrontWallSide() {
		return Compass.getOppositeDirection(direction);
	}

	public void setDirection(CardinalPoint direction) {
		location.setDirection(direction);
	}


	public DungeonLocation getFrontLocation() {
		DungeonLocation location = new DungeonLocation(this.location);

		switch (location.getDirection()) {
		case North:
			location.setCoordinates(new Vector2(location.getCoordinates().x, location.getCoordinates().y - 1));
			break;
		case South:
			location.setCoordinates(new Vector2(location.getCoordinates().x, location.getCoordinates().y + 1));
			break;
		case West:
			location.setCoordinates(new Vector2(location.getCoordinates().x - 1, location.getCoordinates().y));
			break;
		case East:
			location.setCoordinates(new Vector2(location.getCoordinates().x + 1, location.getCoordinates().y));
			break;
		}

		return location;
	}

	public CardinalPoint getDirection() {
		return location.getDirection();
	}

	public Square getFrontSquare() {
		return maze.getSquare(getFrontLocation().getCoordinates());
	}

	public int getTeamSpeed() {
		return teamSpeed;
	}

	public Hero[] getHeroes() {
		return heroes;
	}

	public Hero getSelectedHero() {
		return selectedHero;
	}

	public Item getItemInHand() {
		return itemInHand;
	}

	public void setItemInHand(Item itemInHand) {
		this.itemInHand = itemInHand;
	}

	public Maze getMaze() {
		return maze;
	}

	public boolean isCanMove() {
		return canMove;
	}

	public Square getSquare() {
		return square;
	}

	public long getLastMove() {
		return lastMove;
	}

	public boolean isHasMoved() {
		return hasMoved;
	}

	public void setTeamSpeed(int teamSpeed) {
		this.teamSpeed = teamSpeed;
	}

	public void setSelectedHero(Hero selectedHero) {
		this.selectedHero = selectedHero;
	}

	public void setLastMove(long lastMove) {
		this.lastMove = lastMove;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
