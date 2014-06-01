package com.ntk.darkmoor.stub;

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

}
