package com.ntk.darkmoor.engine;

import com.badlogic.gdx.math.Vector2;
import com.ntk.darkmoor.stub.DungeonLocation;

public class Compass {

	public static enum CardinalPoint {
		North, South, West, East;

		public static CardinalPoint valueOf(int ordinal) {
			switch (ordinal) {
			case 0:
				return North;
			case 1:
				return South;
			case 2:
				return West;
			case 3:
				return East;

			}
			return North;
		}
	}

	public static enum CompassRotation {
		Rotate90, Rotate180, Rotate270,
	}

	private CardinalPoint direction;

	// Constructors
	public Compass() {
		direction = CardinalPoint.North;
	}

	public Compass(Compass compass) {
		direction = compass.getDirection();
	}

	// Static methods
	public static CardinalPoint rotate(CardinalPoint direction, CompassRotation rot) {
		CardinalPoint[][] points = new CardinalPoint[][] {//@formatter:off
			// NORTH
			new CardinalPoint[] { CardinalPoint.East,
									CardinalPoint.South,
									CardinalPoint.West,
									CardinalPoint.North, },

			// SOUTH
			new CardinalPoint[] { CardinalPoint.West,
									CardinalPoint.North,
									CardinalPoint.East,
									CardinalPoint.South, },

			// WEST
			new CardinalPoint[] { CardinalPoint.North,
									CardinalPoint.East,
									CardinalPoint.South,
									CardinalPoint.West, },

			// EAST
			new CardinalPoint[] { CardinalPoint.South,
									CardinalPoint.West,
									CardinalPoint.North,
									CardinalPoint.East, }, };//@formatter:on

		// TODO: make sure ordinal is what we need here
		return points[direction.ordinal()][rot.ordinal()];
	}

	public static CardinalPoint seekDirection(DungeonLocation from, DungeonLocation target) {
		Vector2 delta = new Vector2(target.getCoordinates().x - from.getCoordinates().x, target.getCoordinates().y
				- from.getCoordinates().y);

		// Move WEST
		if (delta.x < 0) {
			if (delta.y > 0)
				if (target.getDirection() == CardinalPoint.North)
					return CardinalPoint.South;
				else
					return CardinalPoint.West;
			else if (delta.y < 0)
				if (target.getDirection() == CardinalPoint.South)
					return CardinalPoint.North;
				else
					return CardinalPoint.West;
			else
				return CardinalPoint.West;
		}

		// Move EAST
		else if (delta.x > 0) {
			if (delta.y > 0)
				if (target.getDirection() == CardinalPoint.North)
					return CardinalPoint.South;
				else
					return CardinalPoint.East;
			else if (delta.y < 0)
				if (target.getDirection() == CardinalPoint.South)
					return CardinalPoint.North;
				else
					return CardinalPoint.East;
			else
				return CardinalPoint.East;
		}

		if (delta.y > 0)
			return CardinalPoint.South;
		else if (delta.y < 0)
			return CardinalPoint.North;

		return CardinalPoint.North;
	}

	public static boolean isFacing(DungeonLocation from, DungeonLocation target) {
		// ntk: not used!
		// Vector2 delta = new Vector2(target.getCoordinates().x - from.getCoordinates().x, target.getCoordinates().y
		// - from.getCoordinates().y);

		switch (from.getDirection()) {
		case North:
			return target.getDirection() == CardinalPoint.South;

		case South:
			return target.getDirection() == CardinalPoint.North;

		case West:
			return target.getDirection() == CardinalPoint.East;

		case East:
			return target.getDirection() == CardinalPoint.West;
		}

		return false;
	}

	public static boolean isSideFacing(CardinalPoint view, CardinalPoint side) {
		if (view == CardinalPoint.North && side == CardinalPoint.South)
			return true;
		if (view == CardinalPoint.South && side == CardinalPoint.North)
			return true;
		if (view == CardinalPoint.West && side == CardinalPoint.East)
			return true;
		if (view == CardinalPoint.East && side == CardinalPoint.West)
			return true;

		return false;
	}

	public static CardinalPoint getDirectionFromView(CardinalPoint from, CardinalPoint side) {
		CardinalPoint[][] tab = new CardinalPoint[][] {//@formatter:off
				{CardinalPoint.North, CardinalPoint.South, CardinalPoint.West, CardinalPoint.East},
				{CardinalPoint.South, CardinalPoint.North, CardinalPoint.East, CardinalPoint.West},
				{CardinalPoint.West, CardinalPoint.East, CardinalPoint.South, CardinalPoint.North},
				{CardinalPoint.East, CardinalPoint.West, CardinalPoint.North, CardinalPoint.South},
			}; //@formatter:on

		// TODO: make sure ordinal() is what we need here
		return tab[from.ordinal()][side.ordinal()];
	}

	public static CardinalPoint getOppositeDirection(CardinalPoint direction) {
		CardinalPoint[] val = new CardinalPoint[] {//@formatter:off
			CardinalPoint.South,
			CardinalPoint.North,
			CardinalPoint.East,
			CardinalPoint.West
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
