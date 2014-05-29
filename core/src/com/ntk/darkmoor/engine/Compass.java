package com.ntk.darkmoor.engine;

import com.badlogic.gdx.math.Vector2;
import com.ntk.darkmoor.stub.DungeonLocation;

public class Compass {

	public static enum CardinalPoint {
		NORTH, SOUTH, WEST, EAST;
	}

	public static enum CompassRotation {
		ROTATE_90, ROTATE_180, ROTATE_270,
	}

	private CardinalPoint direction;

	// Constructors
	public Compass() {
		direction = CardinalPoint.NORTH;
	}

	public Compass(Compass compass) {
		direction = compass.getDirection();
	}

	// Static methods
	public static CardinalPoint rotate(CardinalPoint direction, CompassRotation rot) {
		CardinalPoint[][] points = new CardinalPoint[][] {//@formatter:off
			// NORTH
			new CardinalPoint[] { CardinalPoint.EAST,
									CardinalPoint.SOUTH,
									CardinalPoint.WEST,
									CardinalPoint.NORTH, },

			// SOUTH
			new CardinalPoint[] { CardinalPoint.WEST,
									CardinalPoint.NORTH,
									CardinalPoint.EAST,
									CardinalPoint.SOUTH, },

			// WEST
			new CardinalPoint[] { CardinalPoint.NORTH,
									CardinalPoint.EAST,
									CardinalPoint.SOUTH,
									CardinalPoint.WEST, },

			// EAST
			new CardinalPoint[] { CardinalPoint.SOUTH,
									CardinalPoint.WEST,
									CardinalPoint.NORTH,
									CardinalPoint.EAST, }, };//@formatter:on

		// TODO: make sure ordinal is what we need here
		return points[direction.ordinal()][rot.ordinal()];
	}

	public static CardinalPoint seekDirection(DungeonLocation from, DungeonLocation target) {
		Vector2 delta = new Vector2(target.getCoordinates().x - from.getCoordinates().x, target.getCoordinates().y
				- from.getCoordinates().y);

		// Move WEST
		if (delta.x < 0) {
			if (delta.y > 0)
				if (target.getDirection() == CardinalPoint.NORTH)
					return CardinalPoint.SOUTH;
				else
					return CardinalPoint.WEST;
			else if (delta.y < 0)
				if (target.getDirection() == CardinalPoint.SOUTH)
					return CardinalPoint.NORTH;
				else
					return CardinalPoint.WEST;
			else
				return CardinalPoint.WEST;
		}

		// Move EAST
		else if (delta.x > 0) {
			if (delta.y > 0)
				if (target.getDirection() == CardinalPoint.NORTH)
					return CardinalPoint.SOUTH;
				else
					return CardinalPoint.EAST;
			else if (delta.y < 0)
				if (target.getDirection() == CardinalPoint.SOUTH)
					return CardinalPoint.NORTH;
				else
					return CardinalPoint.EAST;
			else
				return CardinalPoint.EAST;
		}

		if (delta.y > 0)
			return CardinalPoint.SOUTH;
		else if (delta.y < 0)
			return CardinalPoint.NORTH;

		return CardinalPoint.NORTH;
	}

	public static boolean isFacing(DungeonLocation from, DungeonLocation target) {
		// ntk: not used!
		// Vector2 delta = new Vector2(target.getCoordinates().x - from.getCoordinates().x, target.getCoordinates().y
		// - from.getCoordinates().y);

		switch (from.getDirection()) {
		case NORTH:
			return target.getDirection() == CardinalPoint.SOUTH;

		case SOUTH:
			return target.getDirection() == CardinalPoint.NORTH;

		case WEST:
			return target.getDirection() == CardinalPoint.EAST;

		case EAST:
			return target.getDirection() == CardinalPoint.WEST;
		}

		return false;
	}

	public static boolean isSideFacing(CardinalPoint view, CardinalPoint side) {
		if (view == CardinalPoint.NORTH && side == CardinalPoint.SOUTH)
			return true;
		if (view == CardinalPoint.SOUTH && side == CardinalPoint.NORTH)
			return true;
		if (view == CardinalPoint.WEST && side == CardinalPoint.EAST)
			return true;
		if (view == CardinalPoint.EAST && side == CardinalPoint.WEST)
			return true;

		return false;
	}

	public static CardinalPoint getDirectionFromView(CardinalPoint from, CardinalPoint side) {
		CardinalPoint[][] tab = new CardinalPoint[][] {//@formatter:off
				{CardinalPoint.NORTH, CardinalPoint.SOUTH, CardinalPoint.WEST, CardinalPoint.EAST},
				{CardinalPoint.SOUTH, CardinalPoint.NORTH, CardinalPoint.EAST, CardinalPoint.WEST},
				{CardinalPoint.WEST, CardinalPoint.EAST, CardinalPoint.SOUTH, CardinalPoint.NORTH},
				{CardinalPoint.EAST, CardinalPoint.WEST, CardinalPoint.NORTH, CardinalPoint.SOUTH},
			}; //@formatter:on

		// TODO: make sure ordinal() is what we need here
		return tab[from.ordinal()][side.ordinal()];
	}

	public static CardinalPoint getOppositeDirection(CardinalPoint direction) {
		CardinalPoint[] val = new CardinalPoint[] {//@formatter:off
			CardinalPoint.SOUTH,
			CardinalPoint.NORTH,
			CardinalPoint.EAST,
			CardinalPoint.WEST
		}; //@formatter:on

		// TODO: make sure ordinal() is what we need here
		return val[direction.ordinal()];
	}

	public CardinalPoint getDirection() {
		return direction;
	}

	public void setDirection(CardinalPoint direction) {
		this.direction = direction;
	}

}
