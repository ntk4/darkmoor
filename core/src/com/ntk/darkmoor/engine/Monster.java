package com.ntk.darkmoor.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
import com.ntk.darkmoor.engine.Compass.CompassRotation;
import com.ntk.darkmoor.engine.Square.SquarePosition;
import com.ntk.darkmoor.engine.ViewField.ViewFieldPosition;
import com.ntk.darkmoor.engine.interfaces.IMonster;
import com.ntk.darkmoor.resource.Resources;
import com.ntk.darkmoor.stub.AudioSample;
import com.ntk.darkmoor.stub.GameMechanics;
import com.ntk.darkmoor.stub.GameScreen;
import com.ntk.darkmoor.stub.GameTime;
import com.ntk.darkmoor.stub.ScriptInterface;
import com.ntk.darkmoor.stub.TileSet;

/**
 * List of monsters : http://members.tripod.com/~stanislavs/games/eob1mons.htm<br/>
 * http://dmweb.free.fr/?q=node/1363<br/>
 * http://wiki.themanaworld.org/index.php/User:Crush/Monster_Database
 * 
 * http://wiki.themanaworld.org/index.php/Monster_Database
 * 
 * @author Nick
 * 
 */
public class Monster extends Entity {

	/**
	 * A size modifier applies to the creature’s Armor Class (AC) and attack bonus,as well as to certain skills. A
	 * creature’s size also determines how far it can reach to make a melee attack and how much space it occupies in a
	 * block.
	 */
	public enum MonsterSize {
		/** means there can be 4 creatures per tile */
		Small,

		/** means there can be 2 creatures per tile */
		Normal,

		/** means there can be only one creature per tile */
		Big;
	}

	/**
	 * Monster artificial intelligence
	 * 
	 */
	public enum MonsterBehaviour {
		/** Go right up to the party and attack */
		Aggressive,

		/** Try to keep distance from party and use ranged attacks (spells and thrown items) */
		RangeAttack,

		/** Avoid the party, only attacking when cornered */
		RunAway,

		/** Stay in one square, unmoving */
		Guard,

		/** Invincible, never attacks, wanders around randomly */
		Friendly,

		/** Invincible, never attacks or moves */
		FriendlyUnmoving,
	}

	public static final String TAG = "monster";

	private List<String> itemsInPocket;
	private Dice damageDice;
	private Dice hitDice;
	private MonsterBehaviour defaultBehaviour;
	private int drawOffsetDuration;
	private int hitDisplayDuration;
	private boolean disposed;
	private boolean canMove;
	private Maze maze;
	private Square square;
	private long lastAction;
	private SquarePosition position;
	private long lastHit;
	private int reward;
	private TileSet tileset;
	private String tileSetName;
	private int tile;
	private String name;
	private String weaponName;
	private Item weapon;
	private long lastDrawOffset;
	private MonsterBehaviour currentBehaviour;
	private Vector2 drawOffset;
	private CardinalPoint direction;
	private boolean hasHealMagic;
	private byte detectionRange;
	private int sightRange;
	/** Gets or sets if the monster receives a variety of AI boosts (opening doors, using switch, cast spell...) */
	private boolean smartAI;
	private boolean teleports;
	/** Gets or sets how likely a monster is to pick up an item on the ground */
	private float pickupRate;
	/** Gets or sets how often a monster steals from the party instead of attacking. */
	public float stealRate;
	/** When sets, the creature can see the party even if it is under the effect of the 'Invisibility' spell. */
	private boolean backRowAttack;
	private boolean canSeeInvisible;
	/**
	 * Defines the attack speed of the creature. This is the minimum amount of time required between two attacks. in
	 * millis
	 */
	private int attackSpeed;
	/** Resistance to magical spells involving poison. */
	private boolean poisonImmunity;
	/**
	 * The monster is non material. These creatures ignore normal attacks but take damage from some spells. These
	 * creatures can pass through all doors of any type.
	 */
	private boolean nonMaterial;
	/** The amount of time while the attack graphic is displayed. */
	private int attackDisplayDuration;
	private int magicCastingLevel;

	// Sounds
	private String attackSoundName;
	private String hurtSoundName;
	private String dieSoundName;
	private String moveSoundName;

	private AudioSample attackSound;
	private AudioSample hurtSound;
	private AudioSample dieSound;
	private AudioSample moveSound;

	private ScriptInterface<IMonster> script;

	private int baseAttack;

	private boolean usesStairs;

	private boolean fillSquare;

	private boolean fleesAfterAttack;

	private boolean hasDrainMagic;

	private boolean throwWeapons;

	private boolean flying;

	public Monster() {
		itemsInPocket = new ArrayList<String>();
		damageDice = new Dice();
		hitDice = new Dice();
		defaultBehaviour = MonsterBehaviour.Aggressive;

		drawOffsetDuration = (int) (1000 * (1 + GameMechanics.getRandom().nextDouble()));

		hitDisplayDuration = 500;
		attackSoundName = "";
		hurtSoundName = "";
		moveSoundName = "";
		dieSoundName = "";
	}

	public void dispose() {
		Resources.unlockSharedAsset(TileSet.class, tileset);
		tileset = null;

		disposed = true;
	}

	public boolean move(Vector2 offset) {
		// Can't move
		if (!canMove)
			return false;

		Team team = GameScreen.getTeam();

		// Get informations about the destination block
		Vector2 dst = getLocation().getCoordinates();
		dst.add(offset);

		// Check all blocking states
		boolean canmove = true;

		// The team
		if (team.getLocation().getMaze() == getLocation().getMaze() && team.getLocation().getCoordinates() == dst)
			canmove = false;

		// A wall
		Square dstblock = maze.getSquare(dst);
		if (dstblock.isBlocking())
			canmove = false;

		// Stairs
		// if (dstblock.Stair != null)
		// canmove = false;

		// Monsters
		if (dstblock.getMonsterCount() > 0)
			canmove = false;

		// blocking door
		// if (dstblock.Door != null && dstblock.Door.IsBlocking)
		// canmove = false;

		if (canmove) {
			// Leave the current block
			square.onMonsterLeave(this);

			getLocation().getCoordinates().add(offset);
			lastAction = new Date().getTime();

			// Enter the new block
			square.onMonsterEnter(this);
		}

		return canmove;
	}

	/**
	 * teleport the monster to a given location
	 * 
	 * @param square
	 * @return
	 */
	public boolean teleport(Square square) {
		if (!teleport(square, SquarePosition.NorthWest))
			if (!teleport(square, SquarePosition.NorthEast))
				if (!teleport(square, SquarePosition.SouthEast))
					if (!teleport(square, SquarePosition.SouthWest))
						return false;

		return true;
	}

	/**
	 * teleport the monster to a given location
	 * 
	 * @param target
	 * @param pos
	 */
	public boolean teleport(Square target, SquarePosition pos) {
		if (square == null)
			return false;

		// Move to another square
		if (this.square != target) {
			// Remove from previous location
			if (square != null)
				square.setMonster(position.value(), null);

			this.square = target;

			// Add the monster to the new square
			position = pos;
			square.setMonster(pos.value(), this);
		}

		// Move to a subsquare
		else {
			// Remove from previous position
			if (square == null || square.getMonster(pos) != null)
				return false;

			square.setMonster(position.value(), null);

			// Move to the new position
			if (square != null)
				square.setMonster(pos.value(), this);

			position = pos;
		}

		return true;
	}

	public boolean teleport(DungeonLocation target) {
		if (target == null)
			return false;

		if (GameScreen.getDungeon() == null)
			return false;

		Maze maze = GameScreen.getDungeon().getMaze(target.getMaze());
		if (maze == null)
			return false;

		return teleport(maze.getSquare(target.getCoordinates()), target.getPosition());
	}

	@Override
	public void hit(Attack attack) {
		if (attack == null)
			return;

		lastAttack = attack;
		if (lastAttack.isAMiss())
			return;

		hitPoint.damage(lastAttack.getHit());

		lastHit = new Date().getTime();

		onHit();

		// Reward the team for having killed the entity
		if (isDead() && attack.getStriker() instanceof Hero) {
			GameScreen.getTeam().addExperience(reward);
		}
	}

	/** Fired when the monster dies */
	public void onDeath() {
		Log.debug("[Monster] {0} die.", name);

		// Drop items on the ground
		for (String item : itemsInPocket)
			square.dropItem(position, Resources.createAsset(Item.class, item));

		// Reward the team
		for (Hero hero : GameScreen.getTeam().getHeroes())
			if (hero != null)
				hero.addExperience(reward / GameScreen.getTeam().getHeroCount());
	}

	/** Fired when the creature is first spawned */
	public void onSpawn() {
		// Trace.WriteDebugLine("[Monster] {0} spawn at {1}.", Name, getLocation().ToStringShort());

		// tileset
		tileset = Resources.createSharedAsset(TileSet.class, tileSetName, tileSetName);

		// Give a weapon to the monster
		if (!StringUtils.isEmpty(weaponName))
			weapon = Resources.createAsset(Item.class, weaponName);

		// Last time updated
		lastUpdate = new Date().getTime();
	}

	/** Fired when the monster is hit */
	public void onHit() {
		Log.debug("[Monster] {0} hit.", name);
	}

	// ////////////////////////// Update & draw ///////////////////////////////////////////////
	// / Update the monster logic
	// / </summary>
	// / <param name="time">Elapsed game time</param>
	public void update(GameTime time) {
		// if (Script.Instance != null)
		// Script.Instance.OnUpdate(this);

		Team team = GameScreen.getTeam();

		// Draw offset
		if (lastDrawOffset + drawOffsetDuration < new Date().getTime()) {
			drawOffset = new Vector2(GameMechanics.getRandom().nextInt(8) - 4, GameMechanics.getRandom().nextInt(8) - 4);
			lastDrawOffset = new Date().getTime();
		}

		// Not entity time to update
		if (lastUpdate + moveSpeed > new Date().getTime())
			return;

		switch (currentBehaviour) {
		// Case: Aggressive
		case Aggressive:
			break;

		// Case: RangeAttack
		case RangeAttack:
			break;

		// Case: Run away
		case RunAway:
			break;

		// Case: Guard
		case Guard: {
			// heal if needed
			if (canHeal() && hitPoint.getRatio() < 0.5f)
				heal();

			// Not in the same maze
			else if (team.getMaze() != maze)
				break;

			// Facing the team
			else if (Compass.seekDirection(getLocation(), team.getLocation()) != direction) {
				direction = Compass.seekDirection(getLocation(), team.getLocation());
			}

			// Can get closer while staying in the same square ?
			else if (canGetCloserTo(team.getLocation())) {
				getCloserTo(team.getLocation());
			}

			// Can do close attack ?
			else if (canDoCloseAttack(team.getLocation())) {
				attack(team.getLocation());
			}

			// If neat the target
			else if (isNear(team.getLocation())) {
				// Face the target
				// if (getLocation().IsFacing(Team.location))
				// getLocation().FaceTo(Team.location);

				// No choice, attack !
				if (!canUseAmmo()) {
					// currentBehaviour = Aggressive;
				}
			}
		}
			break;

		// Case: Friendly
		case Friendly:
			break;

		// Case: Friendly unmoving
		case FriendlyUnmoving:
			break;

		}

		lastUpdate = new Date().getTime();
	}

	/**
	 * Checks if the monster can do close combat with the target
	 * 
	 * @param location2
	 * @return
	 */
	private boolean canDoCloseAttack(DungeonLocation target) {
		// Not in the same maze
		if (target.getMaze() != getLocation().getMaze())
			return false;

		// If can get closer to the target
		if (canGetCloserTo(target))
			return false;

		// Find the distance
		Vector2 dist = new Vector2(target.getCoordinates().x - getLocation().getCoordinates().x,
				target.getCoordinates().y - getLocation().getCoordinates().y);

		// Close to the target (up, down or left, right)
		return (dist.x == 0 && Math.abs(dist.y) == 1) || (Math.abs(dist.x) == 1 && dist.y == 0);
	}

	private boolean canUseAmmo() {
		return false;
	}

	private boolean canHeal() {
		if (!hasHealMagic)
			return false;

		return true;
	}

	/**
	 * Try to move closer to the target while remaining in the square
	 * 
	 * @param target
	 * @return
	 */
	public boolean canGetCloserTo(DungeonLocation target) {
		if (target == null)
			return false;

		// Monster is alone in the square
		if (position == SquarePosition.Center)
			return false;

		Vector2 dist = new Vector2(getLocation().getCoordinates().x - target.getCoordinates().x, getLocation()
				.getCoordinates().y - target.getCoordinates().y);

		switch (position) {
		// Case: North west
		case NorthWest: {

			//
			if (dist.x < 0) {
				if (square.getMonsters()[SquarePosition.SouthEast.value()] == null
						|| square.getMonsters()[SquarePosition.SouthWest.value()] == null)
					return true;
			}
			//
			else if (dist.x > 0) {
			}

			// Above
			if (dist.y > 0) {
			}

			// Below
			else if (dist.y < 0) {
				if (square.getMonster(SquarePosition.SouthWest) == null
						|| square.getMonster(SquarePosition.SouthEast) == null)
					return true;
			}
		}
			break;

		// Case: North east
		case NorthEast: {

			// Right
			if (dist.x < 0) {
			}

			// Left
			else if (dist.x > 0) {
				if (square.getMonster(SquarePosition.NorthWest) == null
						|| square.getMonster(SquarePosition.SouthWest) == null)
					return true;
			}

			// Below
			if (dist.y < 0) {
				if (square.getMonsters()[SquarePosition.SouthEast.value()] == null
						|| square.getMonsters()[SquarePosition.SouthWest.value()] == null)
					return true;
			} else if (dist.y > 0) {
			}
		}
			break;

		// Case: South west
		case SouthWest: {
			// Right
			if (dist.x < 0) {
				if (square.getMonster(SquarePosition.SouthEast) == null
						|| square.getMonster(SquarePosition.NorthEast) == null)
					return true;
			}

			// Left
			else if (dist.x > 0) {
			}

			// Above
			if (dist.y > 0) {
				if (square.getMonster(SquarePosition.NorthWest) == null
						|| square.getMonster(SquarePosition.NorthEast) == null)
					return true;
			}

			// Below
			else if (dist.y < 0) {
			}
		}
			break;

		// Case: South east
		case SouthEast: {

			// Left
			if (dist.x > 0) {
				// No monster in SW
				if (square.getMonster(SquarePosition.SouthWest) == null
						|| square.getMonster(SquarePosition.NorthWest) == null)
					return true;
			}

			// Right
			else if (dist.x < 0) {
			}

			// Below
			if (dist.y < 0) {
			}

			// Above
			else if (dist.y > 0) {
				if (square.getMonster(SquarePosition.NorthEast) == null
						|| square.getMonster(SquarePosition.NorthWest) == null)
					return true;
			}
		}
			break;

		}

		return false;
	}

	// / <summary>
	// / Moves closer to the target while remaining in the square
	// / </summary>
	// / <param name="target">Target point</param>
	public void getCloserTo(DungeonLocation target) {
		// Can we get closer ?
		if (!canGetCloserTo(target))
			return;

		Vector2 dist = new Vector2(getLocation().getCoordinates().x - target.getCoordinates().x, getLocation()
				.getCoordinates().y - target.getCoordinates().y);

		switch (position) {
		// Case: North west
		case NorthWest: {
			//
			if (dist.x < 0) {
				if (square.getMonsters()[SquarePosition.SouthEast.value()] == null)
					teleport(square, SquarePosition.SouthEast);

				else if (square.getMonsters()[SquarePosition.SouthWest.value()] == null)
					teleport(square, SquarePosition.SouthWest);
			}
			//
			else if (dist.x > 0) {
			}

			// Above
			if (dist.y > 0) {
			}

			// Below
			else if (dist.y < 0) {
				if (square.getMonster(SquarePosition.SouthWest) == null)
					teleport(square, SquarePosition.SouthWest);

				else if (square.getMonster(SquarePosition.SouthEast) == null)
					teleport(square, SquarePosition.SouthEast);
			}
		}
			break;

		// Case: North east
		case NorthEast: {
			// Right
			if (dist.x < 0) {
			}

			// Left
			else if (dist.x > 0) {
				if (square.getMonster(SquarePosition.NorthWest) == null)
					teleport(square, SquarePosition.NorthWest);
				else if (square.getMonster(SquarePosition.SouthWest) == null)
					teleport(square, SquarePosition.SouthWest);
			}

			// Below
			if (dist.y < 0) {
				if (square.getMonsters()[SquarePosition.SouthEast.value()] == null)
					teleport(square, SquarePosition.SouthEast);

				else if (square.getMonsters()[SquarePosition.SouthWest.value()] == null)
					teleport(square, SquarePosition.SouthWest);
			} else if (dist.y > 0) {
			}
		}
			break;

		// Case: South west
		case SouthWest: {

			// Right
			if (dist.x < 0) {
				if (square.getMonster(SquarePosition.SouthEast) == null)
					teleport(square, SquarePosition.SouthEast);
				else if (square.getMonster(SquarePosition.NorthEast) == null)
					teleport(square, SquarePosition.NorthEast);
			}

			// Left
			else if (dist.x > 0) {
			}

			// Above
			if (dist.y > 0) {
				if (square.getMonster(SquarePosition.NorthWest) == null)
					teleport(square, SquarePosition.NorthWest);
				else if (square.getMonster(SquarePosition.NorthEast) == null)
					teleport(square, SquarePosition.NorthEast);
			}

			// Below
			else if (dist.y < 0) {
			}
		}
			break;

		// Case: South east
		case SouthEast: {

			// Left
			if (dist.x > 0) {
				// No monster in SW
				if (square.getMonster(SquarePosition.SouthWest) == null)
					teleport(square, SquarePosition.SouthWest);

				// No monster in NW
				if (square.getMonster(SquarePosition.NorthWest) == null)
					teleport(square, SquarePosition.NorthWest);
			}

			// Right
			else if (dist.x < 0) {
			}

			// Below
			if (dist.y < 0) {
			}

			// Above
			else if (dist.y > 0) {
				if (square.getMonster(SquarePosition.NorthEast) == null)
					teleport(square, SquarePosition.NorthEast);

				else if (square.getMonster(SquarePosition.NorthWest) == null)
					teleport(square, SquarePosition.NorthWest);
			}
		}
			break;

		}

	}

	// / <summary>
	// / Try to move closer to the target while remaining in the square
	// / </summary>
	// / <param name="target">Target point</param>
	// / <returns>True if can get closer</returns>
	public boolean isNear(DungeonLocation target) {
		if (target == null)
			throw new IllegalArgumentException("target");

		if (getLocation().getMaze() != target.getMaze())
			return false;

		Vector2 dist = new Vector2(getLocation().getCoordinates().x - target.getCoordinates().x, getLocation()
				.getCoordinates().y - target.getCoordinates().y);

		return Math.abs(dist.x) < 5 || Math.abs(dist.y) < 5;
	}

	/**
	 * Draw the monster
	 * 
	 * @param batch SpriteBatch to use
	 * @param view View direction
	 * @param pos Position of the monster in the field of view
	 */
	public void draw(SpriteBatch batch, CardinalPoint view, ViewFieldPosition pos) {
		if (batch == null || tileset == null || square == null)
			return;

		// Translate subsquare position according looking point
		int[][] sub = new int[][] {
				new int[] {
						0, 1, 2, 3, 4 }, // North
				new int[] {
						3, 2, 1, 0, 4 }, // South
				new int[] {
						1, 3, 0, 2, 4 }, // West
				new int[] {
						2, 0, 3, 1, 4 }, // East
		};

		// Find the good square location
		SquarePosition squarepos;
		if (square.getMonsterCount() == 1)
			squarepos = SquarePosition.Center;
		else
			squarepos = SquarePosition.valueOf(sub[view.value()][position.value()]);

		// Screen coordinate
		// Vector2 position = DisplayCoordinates.GetGroundPosition(pos, squarepos);
		Vector2 position = DisplayCoordinates.getMonsterLocation(pos, squarepos);
		position.add(DisplayCoordinates.getScaleFactor(pos, drawOffset));

		// Display color
		Color tint = DisplayCoordinates.getDistantColor(pos);

		// Display in red if monster is hit
		if (lastHit + hitDisplayDuration > new Date().getTime())
			tint = Color.RED;

		// Draw
		// TODO: ntk: draw the texture
		// batch.drawTile(tileset, getTileID(view), position, tint, 0.0f, DisplayCoordinates.getMonsterScaleFactor(pos),
		// needSwapSprite(view) ? SpriteEffects.FLIP_HORIZONTALLY : SpriteEffects.NONE, 0.0f);

	}

	/**
	 * Returns the tile id of the monster depending the view point
	 * 
	 * @param point View direction of the viewer
	 * @return ID of the tile to display the monster
	 */
	private int getTileID(CardinalPoint point) {
		int[][] id = new int[][] {
				// From N S W E Looking
				new int[] {
						3, 0, 2, 1 }, // North
				new int[] {
						0, 3, 1, 2 }, // South
				new int[] {
						1, 2, 3, 0 }, // West
				new int[] {
						2, 1, 0, 3 }, // East
		};

		return id[direction.value()][point.value()] + tile;
	}

	/**
	 * Gets if the sprite need to be horizontally flipped
	 * 
	 * @param point View direction of the viewer
	 * @return True if the sprite need to be flipped
	 */
	private boolean needSwapSprite(CardinalPoint point) {
		boolean[][] id = new boolean[][] {
				// From N S W E Looking
				new boolean[] {
						false, false, true, false }, // North
				new boolean[] {
						false, false, false, true }, // South
				new boolean[] {
						false, true, false, false }, // West
				new boolean[] {
						true, false, false, false }, // East
		};

		return id[direction.value()][point.value()];
	}

	public void heal() {
		if (!canHeal())
			return;

		hitPoint.add(2);
	}

	/** Fired when the monster attacks */
	public void onAttack() {
	}

	// / Checks map between monster and the team to see if can throw projectiles
	// / </summary>
	// / <param name="target">Target location</param>
	// / <returns>True if possible</returns>
	public boolean canDoRangeAttack(DungeonLocation target) {
		// Not in the same maze
		if (target.getMaze() != getLocation().getMaze())
			return false;

		// x or y lined
		if (target.getCoordinates().x == getLocation().getCoordinates().x) {
			return true;
		} else if (target.getCoordinates().y == getLocation().getCoordinates().y) {
			return true;
		} else
			return false;

	}

	// / Gets if the monster can see the given location
	// / </summary>
	// / <returns>True if the point is in range of sight</returns>
	public boolean canSee(DungeonLocation location) {
		if (location == null)
			return false;

		// Not in the same maze
		if (getLocation().getMaze() != location.getMaze())
			return false;

		// Not in sight zone
		if (!getSightZone().contains(location.getCoordinates()))
			return false;

		// Check in straight line
		Vector2 vector = new Vector2(getLocation().getCoordinates().x - location.getCoordinates().x, getLocation()
				.getCoordinates().y - location.getCoordinates().y);
		while (vector.x != 0 || vector.y != 0) {
			if (vector.x > 0)
				vector.x--;
			else if (vector.x < 0)
				vector.x++;

			if (vector.y > 0)
				vector.y--;
			else if (vector.y < 0)
				vector.y++;

			Square block = maze.getSquare(new Vector2(location.getCoordinates().x + vector.x, getLocation()
					.getCoordinates().y + vector.y));
			if (block.isWall())
				return false;
		}

		// Location is visible
		return true;
	}

	private Rectangle getSightZone() {
		if (getLocation() == null)
			return new Rectangle();

		Rectangle zone = null;

		// Calculates the area view
		switch (getLocation().getDirection()) {
		case North:
			zone = new Rectangle(getLocation().getCoordinates().x - 1, getLocation().getCoordinates().y - sightRange,
					3, sightRange);
			break;
		case South:
			zone = new Rectangle(getLocation().getCoordinates().x - 1, getLocation().getCoordinates().y + 1, 3,
					sightRange);
			break;
		case West:
			zone = new Rectangle(getLocation().getCoordinates().x - sightRange, getLocation().getCoordinates().y - 1,
					sightRange, 3);
			break;
		case East:
			zone = new Rectangle(getLocation().getCoordinates().x + 1, getLocation().getCoordinates().y - 1,
					sightRange, 3);
			break;
		}

		return zone;
	}

	// / Can the monster detect a presence near him
	// / </summary>
	// / <param name="location">getLocation() to detect</param>
	// / <returns>True if the monster can fell the location</returns>
	public boolean canDetect(DungeonLocation location) {
		if (location == null)
			return false;

		// Not in the same maze
		if (getLocation().getMaze() != location.getMaze())
			return false;

		// Not in sight zone
		if (!getDetectionZone().contains(location.getCoordinates()))
			return false;

		return true;
	}

	private Rectangle getDetectionZone() {
		if (getLocation() == null)
			return null;

		return new Rectangle(getLocation().getCoordinates().x - detectionRange / 2, getLocation().getCoordinates().y
				- detectionRange / 2, detectionRange, detectionRange);
	}

	// / Does the monster facing a given direction
	// / </summary>
	// / <param name="direction">Direction to check</param>
	// / <returns>True if facing, or false</returns>
	public boolean isFacing(CardinalPoint direction) {

		return getLocation().getDirection() == direction;
	}

	// / Turns the monster to a given direction
	// / </summary>
	// / <param name="direction">Direction to face to</param>
	// / <returns>True if the monster facing the direction</returns>
	public boolean turnTo(CardinalPoint direction) {
		if (!canMove)
			return false;

		getLocation().setDirection(direction);
		lastAction = new Date().getTime();

		return true;
	}

	// / Turns the monster to a given location
	// / </summary>
	// / <param name="location">getLocation() to face to</param>
	// / <returns>True if facing the location, or false</returns>
	public boolean turnTo(DungeonLocation location) {
		// Still facing
		if (getLocation().isFacing(location))
			return true;

		// Face to the location
		getLocation().setDirection(Compass.rotate(getLocation().getDirection(), CompassRotation.Rotate90));

		// Does the monster face the direction
		// TODO: ntk: shouldn't it be getLocation().isFacing?
		return location.isFacing(location);
	}

	@Override
	public String toString() {
		return String.format(name);
	}

	public DungeonLocation getLocation() {
		if (square == null)
			return null;

		return new DungeonLocation(square.getMaze().getName(), square.getLocation());
	}

	/**
	 * Try to attack a location
	 * 
	 * @param location
	 * @return
	 */
	public boolean attack(DungeonLocation location) {
		// TODO: ntk: why empty implementation?
		return false;
	}

	@Override
	public int getBaseSaveBonus() {
		// TODO: ntk: remove hack
		// HACK
		return 2;// +Experience.GetLevelFromXP(Reward) / 2;
	}

	public boolean canMove() {
		if (currentBehaviour == MonsterBehaviour.FriendlyUnmoving || currentBehaviour == MonsterBehaviour.Guard)
			return false;

		if (lastAction + moveSpeed > new Date().getTime())
			return false;

		return true;
	}

	public List<String> getItemsInPocket() {
		return itemsInPocket;
	}

	public Dice getDamageDice() {
		return damageDice;
	}

	public Dice getHitDice() {
		return hitDice;
	}

	public MonsterBehaviour getDefaultBehaviour() {
		return defaultBehaviour;
	}

	public int getDrawOffsetDuration() {
		return drawOffsetDuration;
	}

	public int getHitDisplayDuration() {
		return hitDisplayDuration;
	}

	public boolean isDisposed() {
		return disposed;
	}

	public boolean isCanMove() {
		return canMove;
	}

	public Maze getMaze() {
		return maze;
	}

	public Square getSquare() {
		return square;
	}

	public long getLastAction() {
		return lastAction;
	}

	public SquarePosition getPosition() {
		return position;
	}

	public long getLastHit() {
		return lastHit;
	}

	public int getReward() {
		return reward;
	}

	public TileSet getTileset() {
		return tileset;
	}

	public String getTileSetName() {
		return tileSetName;
	}

	public int getTile() {
		return tile;
	}

	public String getName() {
		return name;
	}

	public String getWeaponName() {
		return weaponName;
	}

	public Item getWeapon() {
		return weapon;
	}

	public long getLastDrawOffset() {
		return lastDrawOffset;
	}

	public MonsterBehaviour getCurrentBehaviour() {
		return currentBehaviour;
	}

	public Vector2 getDrawOffset() {
		return drawOffset;
	}

	public CardinalPoint getDirection() {
		return direction;
	}

	public boolean hasHealMagic() {
		return hasHealMagic;
	}

	public byte getDetectionRange() {
		return detectionRange;
	}

	public int getSightRange() {
		return sightRange;
	}

	public boolean isSmartAI() {
		return smartAI;
	}

	public boolean isTeleports() {
		return teleports;
	}

	public float getPickupRate() {
		return pickupRate;
	}

	public float getStealRate() {
		return stealRate;
	}

	public boolean isBackRowAttack() {
		return backRowAttack;
	}

	public boolean isCanSeeInvisible() {
		return canSeeInvisible;
	}

	public int getAttackSpeed() {
		return attackSpeed;
	}

	public boolean isPoisonImmunity() {
		return poisonImmunity;
	}

	public boolean isNonMaterial() {
		return nonMaterial;
	}

	public int getAttackDisplayDuration() {
		return attackDisplayDuration;
	}

	public String getAttackSoundName() {
		return attackSoundName;
	}

	public String getHurtSoundName() {
		return hurtSoundName;
	}

	public String getDieSoundName() {
		return dieSoundName;
	}

	public String getMoveSoundName() {
		return moveSoundName;
	}

	public AudioSample getAttackSound() {
		return attackSound;
	}

	public AudioSample getHurtSound() {
		return hurtSound;
	}

	public AudioSample getDieSound() {
		return dieSound;
	}

	public AudioSample getMoveSound() {
		return moveSound;
	}

	public ScriptInterface<IMonster> getScript() {
		return script;
	}

	public int getMagicCastingLevel() {
		return magicCastingLevel;
	}

	public int getBaseAttack() {
		return baseAttack;
	}

	public boolean isUsesStairs() {
		return usesStairs;
	}

	public boolean isFillSquare() {
		return fillSquare;
	}

	public boolean isFleesAfterAttack() {
		return fleesAfterAttack;
	}

	public boolean hasDrainMagic() {
		return hasDrainMagic;
	}

	public boolean hasThrowWeapons() {
		return throwWeapons;
	}

	public boolean isFlying() {
		return flying;
	}

	public void setPosition(SquarePosition newValue) {
		// Remove from previous position
		if (square != null) {
			square.getMonsters()[position.value()] = null;
			square.getMonsters()[newValue.value()] = this;
		}

		position = newValue;
	}

	public boolean load(Element xml) {
		if (xml == null || !TAG.equalsIgnoreCase(xml.getName()))
			return false;

		this.name = xml.getAttribute("name");
		this.position = SquarePosition.valueOf(xml.getAttribute("position"));

		String name = null, value = null;
		for (int i = 0; i < xml.getChildCount(); i++) {
			Element child = xml.getChild(i);
			name = child.getName();
			if (child.getAttributes().containsKey("value"))
				value = child.getAttribute("value");

			if ("attackspeed".equalsIgnoreCase(name)) {
				attackSpeed = Integer.parseInt(value);

			} else if ("location".equalsIgnoreCase(name)) {
				// TODO: ntk: check this. normally there was a field to load into
				if (getLocation() != null)
					getLocation().load(child);

			} else if ("direction".equalsIgnoreCase(name)) {
				direction = CardinalPoint.valueOf(value);

			} else if ("tiles".equalsIgnoreCase(name)) {
				tileSetName = child.getAttribute("name");
				tile = Integer.parseInt(child.getAttribute("id"));

			} else if ("pocket".equalsIgnoreCase(name)) {
				itemsInPocket.add(child.getAttribute("item"));

			} else if ("script".equalsIgnoreCase(name)) {
				script = new ScriptInterface<IMonster>();
				script.load(child);

			} else if ("damage".equalsIgnoreCase(name)) {
				damageDice.load(child);

			} else if ("hitdice".equalsIgnoreCase(name)) {
				hitDice.load(child);

			} else if ("armorclass".equalsIgnoreCase(name)) {
				armorClass = Integer.parseInt(value);

			} else if ("castinglevel".equalsIgnoreCase(name)) {
				magicCastingLevel = Integer.parseInt(value);

			} else if ("stealrate".equalsIgnoreCase(name)) {
				stealRate = Float.parseFloat(value.replace(',', '.'));

			} else if ("pickuprate".equalsIgnoreCase(name)) {
				pickupRate = Float.parseFloat(value.replace(',', '.'));

			} else if ("baseattack".equalsIgnoreCase(name)) {
				baseAttack = Integer.parseInt(value);

			} else if ("sightrange".equalsIgnoreCase(name)) {
				sightRange = Byte.parseByte(value);

			} else if ("behaviour".equalsIgnoreCase(name)) {
				defaultBehaviour = MonsterBehaviour.valueOf(child.getAttribute("default"));
				currentBehaviour = MonsterBehaviour.valueOf(child.getAttribute("current"));

			} else if ("nonmaterial".equalsIgnoreCase(name)) {
				nonMaterial = Boolean.parseBoolean(value);

			} else if ("poisonimmunity".equalsIgnoreCase(name)) {
				poisonImmunity = Boolean.parseBoolean(value);

			} else if ("canseeinvisible".equalsIgnoreCase(name)) {
				canSeeInvisible = Boolean.parseBoolean(value);

			} else if ("backrowattack".equalsIgnoreCase(name)) {
				backRowAttack = Boolean.parseBoolean(value);

			} else if ("teleports".equalsIgnoreCase(name)) {
				teleports = Boolean.parseBoolean(value);

			} else if ("usestairs".equalsIgnoreCase(name)) {
				usesStairs = Boolean.parseBoolean(value);

			} else if ("fillsquare".equalsIgnoreCase(name)) {
				fillSquare = Boolean.parseBoolean(value);

			} else if ("flees".equalsIgnoreCase(name)) {
				fleesAfterAttack = Boolean.parseBoolean(value);

			} else if ("drains".equalsIgnoreCase(name)) {
				hasDrainMagic = Boolean.parseBoolean(value);

			} else if ("heals".equalsIgnoreCase(name)) {
				hasHealMagic = Boolean.parseBoolean(value);

			} else if ("throwweapons".equalsIgnoreCase(name)) {
				throwWeapons = Boolean.parseBoolean(value);

			} else if ("flying".equalsIgnoreCase(name)) {
				flying = Boolean.parseBoolean(value);

			} else if ("smartai".equalsIgnoreCase(name)) {
				smartAI = Boolean.parseBoolean(value);

			} else if ("reward".equalsIgnoreCase(name)) {
				reward = Integer.parseInt(value);

			} else if ("weaponname".equalsIgnoreCase(name)) {
				weaponName = child.getAttribute("name");

			} else if ("sound".equalsIgnoreCase(name)) {
				// TODO: ntk: load the sound
			} else {
				super.load(child);
			}
		}

		return true;
	}

	public boolean save(XmlWriter writer) throws IOException {
		if (writer == null)
			return false;

		writer.element(TAG).attribute("name", name).attribute("position", position.toString());

		super.save(writer);

		if (script != null)
			script.save("script", writer);

		damageDice.save("damage", writer);
		hitDice.save("hitdice", writer);

		writer.element("tiles").attribute("name", tileSetName).attribute("id", tile).pop();
		writer.element("direction").attribute("value", direction.toString()).pop();

		for (String name : itemsInPocket)
			writer.element("pocket").attribute("item", name).pop();

		writer.element("armorclass").attribute("value", armorClass).pop();
		writer.element("baseattack").attribute("value", baseAttack).pop();
		writer.element("sightrange").attribute("value", sightRange).pop();
		writer.element("reward").attribute("value", reward).pop();
		writer.element("sound").attribute("event", "hit").attribute("name", attackSoundName).pop();
		writer.element("sound").attribute("event", "hurt").attribute("name", hurtSoundName).pop();
		writer.element("sound").attribute("event", "walk").attribute("name", moveSoundName).pop();
		writer.element("sound").attribute("event", "die").attribute("name", dieSoundName).pop();
		writer.element("behaviour").attribute("default", defaultBehaviour.toString())
				.attribute("current", currentBehaviour.toString()).pop();
		writer.element("fillsquare").attribute("value", fillSquare).pop();
		writer.element("flees").attribute("value", fleesAfterAttack).pop();
		writer.element("drains").attribute("value", hasDrainMagic).pop();
		writer.element("heals").attribute("value", hasHealMagic).pop();
		writer.element("castinglevel").attribute("value", magicCastingLevel).pop();
		writer.element("throwweapons").attribute("value", throwWeapons).pop();
		writer.element("weaponname").attribute("name", weaponName).pop();
		writer.element("usestairs").attribute("value", usesStairs).pop();
		writer.element("flying").attribute("value", flying).pop();
		writer.element("smartai").attribute("value", smartAI).pop();
		writer.element("teleports").attribute("value", teleports).pop();
		writer.element("pickuprate").attribute("value", String.valueOf(pickupRate).replace('.', ',')).pop();
		writer.element("stealrate").attribute("value", String.valueOf(stealRate).replace('.', ',')).pop();
		writer.element("backrowattack").attribute("value", backRowAttack).pop();
		writer.element("canseeinvisible").attribute("value", canSeeInvisible).pop();
		writer.element("poisonimmunity").attribute("value", poisonImmunity).pop();
		writer.element("nonmaterial").attribute("value", nonMaterial).pop();
		writer.element("attackspeed").attribute("value", attackSpeed).pop();

		writer.pop();

		return true;
	}

}
