package com.ntk.darkmoor.stub;

import java.util.List;

import com.ntk.darkmoor.engine.Compass.CardinalPoint;
import com.ntk.darkmoor.engine.DungeonLocation;
import com.ntk.darkmoor.engine.Item;
import com.ntk.darkmoor.engine.Maze;


public class Team {

	private DungeonLocation location;

	public DungeonLocation getLocation() {
		return location;
	}

	public void setLocation(DungeonLocation location) {
		this.location = location;
	}

	public Hero getSelectedHero() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addExperience(int amount) {
		// TODO Auto-generated method stub
		
	}

	public void setDirection(CardinalPoint direction) {
		// TODO Auto-generated method stub
		
	}

	public boolean teleport(DungeonLocation target) {
		// TODO Auto-generated method stub
		return false;
	}

	public Item getItemInHand() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setItemInHand(Item item) {
		// TODO Auto-generated method stub
		
	}

	public List<Hero> getHeroes() {
		// TODO Auto-generated method stub
		return null;
	}

	public void offset(CardinalPoint direction, int i) {
		// TODO Auto-generated method stub
		
	}

	public int getHeroCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Hero getHero(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	public void dropHero(Hero hero) {
		// TODO Auto-generated method stub
		
	}

	public Maze getMaze() {
		// TODO Auto-generated method stub
		return null;
	}

}
