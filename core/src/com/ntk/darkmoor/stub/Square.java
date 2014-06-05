package com.ntk.darkmoor.stub;

import java.io.IOException;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.engine.Compass.CardinalPoint;
import com.ntk.darkmoor.engine.Dungeon;
import com.ntk.darkmoor.engine.Item;
import com.ntk.darkmoor.engine.Maze;
import com.ntk.darkmoor.engine.actor.SquareActor;

public class Square {

	public enum SquareType {
		/** Ground block */
		Ground,

		/** Wall block */
		Wall,

		/** Illusionary wall */
		Illusion,
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

	public static final String TAG = null;
	private SquareActor actor;

	public Square(Maze maze) {
		// TODO Auto-generated constructor stub
	}

	public Square getSquare(Dungeon dungeon) {
		// TODO Auto-generated method stub
		return null;
	}

	public SquareActor getActor() {
		return actor;
	}

	public void setActor(SquareActor actor) {
		this.actor = actor;
	}

	public void save(XmlWriter writer) throws IOException {
		// TODO Auto-generated method stub

	}

	public void load(Element child) {
		// TODO Auto-generated method stub

	}

	public Vector2 getLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	public static List<Item> getItemsFromSide(CardinalPoint direction, CardinalPoint side) {
		// TODO Auto-generated method stub
		return null;
	}

	public Maze getMaze() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean dropItemFromSide(CardinalPoint side, Item itemInHand) {
		// TODO Auto-generated method stub
		return true;
	}

	public Item collectItemFromSide(CardinalPoint side) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isWall() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setType(SquareType ground) {
		// TODO Auto-generated method stub

	}

	public void init() {
		// TODO Auto-generated method stub

	}

	public void setPitTarget(boolean b) {
		// TODO Auto-generated method stub

	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void dropItem(SquarePosition pos, Item item) {
		// TODO Auto-generated method stub

	}

	public void update(GameTime time) {
		// TODO Auto-generated method stub

	}

	public int getDecoration(int i) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getDecorationId(CardinalPoint side) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setLocation(Vector2 location) {
		// TODO Auto-generated method stub

	}

	public void setMaze(Maze maze) {
		// TODO Auto-generated method stub

	}

	public boolean isPitTarget() {
		// TODO Auto-generated method stub
		return false;
	}

	public List<Item>[] getItems(CardinalPoint view) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean hasDecorations() {
		// TODO Auto-generated method stub
		return false;
	}

	public int getDecorationId(CardinalPoint view, CardinalPoint side) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getMonsterCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Monster getMonster(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	public SquareType getType() {
		// TODO Auto-generated method stub
		return SquareType.Ground;
	}

}
