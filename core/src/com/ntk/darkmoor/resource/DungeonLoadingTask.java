package com.ntk.darkmoor.resource;

import com.badlogic.gdx.utils.async.AsyncTask;
import com.ntk.darkmoor.engine.Dungeon;

public class DungeonLoadingTask implements AsyncTask<Dungeon> {

	private String dungeonName;
	
	public DungeonLoadingTask(String dungeonName) {
		this.dungeonName = dungeonName;
	}
	
	@Override
	public Dungeon call() throws Exception {
		
		Dungeon dungeon = Resources.createDungeonResource(dungeonName, null);
		dungeon.init();
		Resources.setDungeon(dungeon);
		return dungeon;
	}

}
