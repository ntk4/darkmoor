package com.ntk.darkmoor.engine;

import java.io.IOException;

import org.ntk.commons.StringUtils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.engine.Compass.CardinalPoint;
import com.ntk.darkmoor.engine.Square.SquarePosition;

public class DungeonLocation {

	private String maze;
	private Vector2 coordinate;
	private SquarePosition position;
	private CardinalPoint direction;

	public DungeonLocation() {
	}

	public DungeonLocation(Element xml) {
		load(xml);
	}

	// Copy constructor
	public DungeonLocation(DungeonLocation loc) {
		if (loc == null)
			return;

		maze = loc.getMaze();
		coordinate = loc.getCoordinates();
		position = loc.getPosition();
		direction = loc.getDirection();
	}

	public DungeonLocation(String maze, Vector2 coordinate) {
		this(maze, coordinate, CardinalPoint.North, SquarePosition.NorthWest);
	}

	public DungeonLocation(String maze, Vector2 coordinate, CardinalPoint direction, SquarePosition position) {
		this.maze = maze;
		this.coordinate = coordinate;
		this.direction = direction;
		this.position = position;
	}

	public Maze getMaze(Dungeon dungeon) {
		if (dungeon == null)
			return null;

		return dungeon.getMaze(maze);
	}

	public boolean save(String name, XmlWriter writer) throws IOException {
		if (writer == null || StringUtils.isEmpty(name))
			return false;

		writer.element(name).attribute("maze", maze);
		writer.attribute("x", coordinate.x).attribute("y", coordinate.y);
		writer.attribute("direction", direction.toString()).attribute("position", position.toString());
		writer.pop();

		return true;
	}

	public boolean load(Element node) {
		if (node == null)
			return false;

		if (!StringUtils.isEmpty(node.getAttribute("maze"))) {
			maze = node.getAttribute("maze");

		}
		if (!StringUtils.isEmpty(node.getAttribute("x")) && !StringUtils.isEmpty(node.getAttribute("y"))) {
			coordinate = new Vector2(Integer.parseInt(node.getAttribute("x")), Integer.parseInt(node.getAttribute("y")));

		}
		if (!StringUtils.isEmpty(node.getAttribute("direction"))) {
			direction = CardinalPoint.valueOf(node.getAttribute("direction"));

		}
		if (!StringUtils.isEmpty(node.getAttribute("position"))) {
			position = SquarePosition.valueOf(node.getAttribute("position"));
		}

		return true;
	}

	@Override
	public String toString() {
		return String.format("%dx%d in %s, looking %s, sub %s", coordinate.x, coordinate.y, maze, direction, position);
	}

	public String toStringShort() {
		return String.format("%dx%d in \"%s\"", coordinate.x, coordinate.y, maze);
	}

	public Square getSquare(Dungeon dungeon) {
		if (dungeon == null)
			return null;

		Maze maze = dungeon.getMaze(this.maze);
		if (maze == null)
			return null;

		return maze.getSquare(coordinate);
	}

	public void Offset(CardinalPoint direction, int count) {
		switch (direction) {
		case North:
			coordinate.add(0, -count);
			break;
		case South:
			coordinate.add(0, count);
			break;
		case West:
			coordinate.add(-count, 0);
			break;
		case East:
			coordinate.add(count, 0);
			break;
		}

	}

	public boolean isFacing(DungeonLocation location) {
		Vector2 offset = new Vector2(location.coordinate.x - coordinate.x, location.coordinate.y - coordinate.y);

		switch (direction) {
		case North: {
			if (offset.x <= 0 && offset.y <= 0)
				return true;
		}
			break;
		case South: {
			if (offset.x >= 0 && offset.y >= 0)
				return true;
		}
			break;
		case West: {
			if (offset.x <= 0 && offset.y >= 0)
				return true;
		}
			break;
		case East: {
			if (offset.x >= 0 && offset.y <= 0)
				return true;
		}
			break;
		}

		return false;
	}

	/**
	 * Face a given location
	 * 
	 * @param target
	 */
	public void faceTo(DungeonLocation target) {
		if (target == null)
			throw new IllegalArgumentException("target");

		// TODO: ntk: what about the implementation?
	}

	public String getMaze() {
		return maze;
	}

	public void setMaze(String maze) {
		this.maze = maze;
	}

	public Vector2 getCoordinates() {
		return coordinate;
	}

	public void setCoordinates(Vector2 coordinate) {
		this.coordinate.x = coordinate.x;
		this.coordinate.y = coordinate.y;
	}

	public SquarePosition getPosition() {
		return position;
	}

	public void setPosition(SquarePosition position) {
		this.position = position;
	}

	public CardinalPoint getDirection() {
		return direction;
	}

	public void setDirection(CardinalPoint direction) {
		this.direction = direction;
	}

}
