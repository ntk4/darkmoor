package com.ntk.darkmoor.engine;

import java.util.ArrayList;
import java.util.Date;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.ntk.darkmoor.engine.Compass.CardinalPoint;
import com.ntk.darkmoor.engine.Square.SquarePosition;
import com.ntk.darkmoor.engine.ViewField.ViewFieldPosition;
import com.ntk.darkmoor.resource.Resources;
import com.ntk.darkmoor.stub.GameMechanics;
import com.ntk.darkmoor.stub.GameScreen;
import com.ntk.darkmoor.stub.GameTime;
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

	private ArrayList<String> itemsInPocket;
	private Dice damageDice;
	private Dice hitDice;
	private MonsterBehaviour defaultBehaviour;
	private int drawOffsetDuration;
	private int hitDisplayDuration;
	private TileSet tileset;
	private boolean disposed;
	private boolean canMove;
	private DungeonLocation location;
	private Maze maze;
	private Square square;
	private long lastAction;
	private SquarePosition position;
	private long lastHit;
	private int reward;

	public Monster() {
		itemsInPocket = new ArrayList<String>();
		damageDice = new Dice();
		hitDice = new Dice();
		defaultBehaviour = MonsterBehaviour.Aggressive;

		drawOffsetDuration = (int) (1000 * (1 + GameMechanics.getRandom().nextDouble()));

		hitDisplayDuration = 500;
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
		Vector2 dst = location.getCoordinates();
		dst.add(offset);

		// Check all blocking states
		boolean canmove = true;

		// The team
		if (team.getLocation().getMaze() == location.getMaze() && team.getLocation().getCoordinates() == dst)
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

			location.getCoordinates().add(offset);
			lastAction = new Date().getTime();

			// Enter the new block
			square.onMonsterEnter(this);
		}

		return canmove;
	}

	/**
	 * Teleport the monster to a given location
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
	 * Teleport the monster to a given location
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

	public DungeonLocation getLocation() {
		return null;
	}

	public boolean canSee(DungeonLocation location) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean canDetect(DungeonLocation location) {
		// TODO Auto-generated method stub
		return false;
	}

	public void turnTo(DungeonLocation location) {
		// TODO Auto-generated method stub

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

	public static int getSightRange() {
		// TODO Auto-generated method stub
		return 3;
	}

	public static Maze getMaze() {
		// TODO Auto-generated method stub
		return null;
	}

	public void onSpawn() {
		// TODO Auto-generated method stub

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

	private void onHit() {
		// TODO Auto-generated method stub

	}

	public void draw(SpriteBatch batch, CardinalPoint view, ViewFieldPosition position) {
		// TODO Auto-generated method stub

	}

	public void onDeath() {
		// TODO Auto-generated method stub

	}

	public void update(GameTime time) {
		// TODO Auto-generated method stub

	}

}
