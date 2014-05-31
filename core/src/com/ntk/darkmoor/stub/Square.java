package com.ntk.darkmoor.stub;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.engine.Compass.CardinalPoint;

public class Square {

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

}
