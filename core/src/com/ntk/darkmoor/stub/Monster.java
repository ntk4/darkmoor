package com.ntk.darkmoor.stub;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ntk.darkmoor.engine.Attack;
import com.ntk.darkmoor.engine.Compass.CardinalPoint;
import com.ntk.darkmoor.engine.DungeonLocation;
import com.ntk.darkmoor.engine.Entity;
import com.ntk.darkmoor.engine.Maze;
import com.ntk.darkmoor.engine.Square;
import com.ntk.darkmoor.engine.Square.SquarePosition;
import com.ntk.darkmoor.engine.ViewField.ViewFieldPosition;

public class Monster extends Entity {

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

	public boolean attack(DungeonLocation location) {
		// TODO Auto-generated method stub
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

	public void teleport(DungeonLocation target) {
		// TODO Auto-generated method stub
		
	}

	public void onSpawn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hit(Attack attack) {
		// TODO Auto-generated method stub
		
	}

	public void draw(SpriteBatch batch, CardinalPoint view, ViewFieldPosition position) {
		// TODO Auto-generated method stub
		
	}

	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	public void onDeath() {
		// TODO Auto-generated method stub
		
	}

	public void update(GameTime time) {
		// TODO Auto-generated method stub
		
	}

	public void teleport(Square square) {
		// TODO Auto-generated method stub
		
	}

	public void teleport(Square square, SquarePosition valueOf) {
		// TODO Auto-generated method stub
		
	}

}
