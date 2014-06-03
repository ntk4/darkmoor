package com.ntk.darkmoor.stub;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.engine.Dungeon;
import com.ntk.darkmoor.engine.Compass.CardinalPoint;

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
	}

	public static final String TAG = null;
	private SquareActor actor;

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

	public void save(XmlWriter writer) {
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

}
