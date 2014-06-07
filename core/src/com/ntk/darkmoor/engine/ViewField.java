package com.ntk.darkmoor.engine;

import com.badlogic.gdx.math.Vector2;
import com.ntk.darkmoor.stub.Square;

public class ViewField {

	public enum ViewFieldPosition {
		A(0), B(1), C(2), D(3), E(4),

		F(5), G(6), H(7), I(8), J(9),

		K(10), L(11), M(12),

		N(13), Team(14), O(15);

		private int value;

		private ViewFieldPosition(int value) {
			this.value = value;
		}

		public int value() {
			return value;
		}
	}

	private Maze maze;
	private Square[] blocks;
	private boolean teamVisible;

	public ViewField(Maze maze, DungeonLocation location) {
		this.maze = maze;
		blocks = new Square[16];

		// Cone of vision : 15 blocks + 1 block for the Point of View
		//
		// ABCDE
		// FGHIJ
		// KLM
		// N^O
		//
		// ^ => Point of view
		switch (location.getDirection()) {
		case North: {
			blocks[0] = maze.getSquare(new Vector2(location.getCoordinates().x - 2, location.getCoordinates().y - 3));
			blocks[1] = maze.getSquare(new Vector2(location.getCoordinates().x - 1, location.getCoordinates().y - 3));
			blocks[2] = maze.getSquare(new Vector2(location.getCoordinates().x, location.getCoordinates().y - 3));
			blocks[3] = maze.getSquare(new Vector2(location.getCoordinates().x + 1, location.getCoordinates().y - 3));
			blocks[4] = maze.getSquare(new Vector2(location.getCoordinates().x + 2, location.getCoordinates().y - 3));

			blocks[5] = maze.getSquare(new Vector2(location.getCoordinates().x - 2, location.getCoordinates().y - 2));
			blocks[6] = maze.getSquare(new Vector2(location.getCoordinates().x - 1, location.getCoordinates().y - 2));
			blocks[7] = maze.getSquare(new Vector2(location.getCoordinates().x, location.getCoordinates().y - 2));
			blocks[8] = maze.getSquare(new Vector2(location.getCoordinates().x + 1, location.getCoordinates().y - 2));
			blocks[9] = maze.getSquare(new Vector2(location.getCoordinates().x + 2, location.getCoordinates().y - 2));

			blocks[10] = maze.getSquare(new Vector2(location.getCoordinates().x - 1, location.getCoordinates().y - 1));
			blocks[11] = maze.getSquare(new Vector2(location.getCoordinates().x, location.getCoordinates().y - 1));
			blocks[12] = maze.getSquare(new Vector2(location.getCoordinates().x + 1, location.getCoordinates().y - 1));

			blocks[13] = maze.getSquare(new Vector2(location.getCoordinates().x - 1, location.getCoordinates().y));
			blocks[15] = maze.getSquare(new Vector2(location.getCoordinates().x + 1, location.getCoordinates().y));

		}
			break;

		case South: {
			blocks[0] = maze.getSquare(new Vector2(location.getCoordinates().x + 2, location.getCoordinates().y + 3));
			blocks[1] = maze.getSquare(new Vector2(location.getCoordinates().x + 1, location.getCoordinates().y + 3));
			blocks[2] = maze.getSquare(new Vector2(location.getCoordinates().x, location.getCoordinates().y + 3));
			blocks[3] = maze.getSquare(new Vector2(location.getCoordinates().x - 1, location.getCoordinates().y + 3));
			blocks[4] = maze.getSquare(new Vector2(location.getCoordinates().x - 2, location.getCoordinates().y + 3));

			blocks[5] = maze.getSquare(new Vector2(location.getCoordinates().x + 2, location.getCoordinates().y + 2));
			blocks[6] = maze.getSquare(new Vector2(location.getCoordinates().x + 1, location.getCoordinates().y + 2));
			blocks[7] = maze.getSquare(new Vector2(location.getCoordinates().x, location.getCoordinates().y + 2));
			blocks[8] = maze.getSquare(new Vector2(location.getCoordinates().x - 1, location.getCoordinates().y + 2));
			blocks[9] = maze.getSquare(new Vector2(location.getCoordinates().x - 2, location.getCoordinates().y + 2));

			blocks[10] = maze.getSquare(new Vector2(location.getCoordinates().x + 1, location.getCoordinates().y + 1));
			blocks[11] = maze.getSquare(new Vector2(location.getCoordinates().x, location.getCoordinates().y + 1));
			blocks[12] = maze.getSquare(new Vector2(location.getCoordinates().x - 1, location.getCoordinates().y + 1));

			blocks[13] = maze.getSquare(new Vector2(location.getCoordinates().x + 1, location.getCoordinates().y));
			blocks[15] = maze.getSquare(new Vector2(location.getCoordinates().x - 1, location.getCoordinates().y));
		}
			break;

		case East: {
			blocks[0] = maze.getSquare(new Vector2(location.getCoordinates().x + 3, location.getCoordinates().y - 2));
			blocks[1] = maze.getSquare(new Vector2(location.getCoordinates().x + 3, location.getCoordinates().y - 1));
			blocks[2] = maze.getSquare(new Vector2(location.getCoordinates().x + 3, location.getCoordinates().y));
			blocks[3] = maze.getSquare(new Vector2(location.getCoordinates().x + 3, location.getCoordinates().y + 1));
			blocks[4] = maze.getSquare(new Vector2(location.getCoordinates().x + 3, location.getCoordinates().y + 2));

			blocks[5] = maze.getSquare(new Vector2(location.getCoordinates().x + 2, location.getCoordinates().y - 2));
			blocks[6] = maze.getSquare(new Vector2(location.getCoordinates().x + 2, location.getCoordinates().y - 1));
			blocks[7] = maze.getSquare(new Vector2(location.getCoordinates().x + 2, location.getCoordinates().y));
			blocks[8] = maze.getSquare(new Vector2(location.getCoordinates().x + 2, location.getCoordinates().y + 1));
			blocks[9] = maze.getSquare(new Vector2(location.getCoordinates().x + 2, location.getCoordinates().y + 2));

			blocks[10] = maze.getSquare(new Vector2(location.getCoordinates().x + 1, location.getCoordinates().y - 1));
			blocks[11] = maze.getSquare(new Vector2(location.getCoordinates().x + 1, location.getCoordinates().y));
			blocks[12] = maze.getSquare(new Vector2(location.getCoordinates().x + 1, location.getCoordinates().y + 1));

			blocks[13] = maze.getSquare(new Vector2(location.getCoordinates().x, location.getCoordinates().y - 1));
			blocks[15] = maze.getSquare(new Vector2(location.getCoordinates().x, location.getCoordinates().y + 1));
		}
			break;

		case West: {
			blocks[0] = maze.getSquare(new Vector2(location.getCoordinates().x - 3, location.getCoordinates().y + 2));
			blocks[1] = maze.getSquare(new Vector2(location.getCoordinates().x - 3, location.getCoordinates().y + 1));
			blocks[2] = maze.getSquare(new Vector2(location.getCoordinates().x - 3, location.getCoordinates().y));
			blocks[3] = maze.getSquare(new Vector2(location.getCoordinates().x - 3, location.getCoordinates().y - 1));
			blocks[4] = maze.getSquare(new Vector2(location.getCoordinates().x - 3, location.getCoordinates().y - 2));

			blocks[5] = maze.getSquare(new Vector2(location.getCoordinates().x - 2, location.getCoordinates().y + 2));
			blocks[6] = maze.getSquare(new Vector2(location.getCoordinates().x - 2, location.getCoordinates().y + 1));
			blocks[7] = maze.getSquare(new Vector2(location.getCoordinates().x - 2, location.getCoordinates().y));
			blocks[8] = maze.getSquare(new Vector2(location.getCoordinates().x - 2, location.getCoordinates().y - 1));
			blocks[9] = maze.getSquare(new Vector2(location.getCoordinates().x - 2, location.getCoordinates().y - 2));

			blocks[10] = maze.getSquare(new Vector2(location.getCoordinates().x - 1, location.getCoordinates().y + 1));
			blocks[11] = maze.getSquare(new Vector2(location.getCoordinates().x - 1, location.getCoordinates().y));
			blocks[12] = maze.getSquare(new Vector2(location.getCoordinates().x - 1, location.getCoordinates().y - 1));

			blocks[13] = maze.getSquare(new Vector2(location.getCoordinates().x, location.getCoordinates().y + 1));
			blocks[15] = maze.getSquare(new Vector2(location.getCoordinates().x, location.getCoordinates().y - 1));
		}
			break;
		}

		// Team's position
		blocks[14] = maze.getSquare(location.getCoordinates());
	}

	public Maze getMaze() {
		return maze;
	}

	public Square getBlock(ViewFieldPosition position) {
		return blocks[position.value()];
	}

	public Square[] getBlocks() {
		return blocks;
	}

	public boolean isTeamVisible() {
		return teamVisible;
	}

}
