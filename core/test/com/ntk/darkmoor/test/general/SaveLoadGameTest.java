package com.ntk.darkmoor.test.general;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.ntk.darkmoor.config.SaveGame;
import com.ntk.darkmoor.config.Settings;
import com.ntk.darkmoor.engine.Dungeon;
import com.ntk.darkmoor.engine.Team;
import com.ntk.darkmoor.resource.Resources;

public class SaveLoadGameTest extends BaseTestCase {

	@Before
	public void setUp() throws IOException {
		Resources.setResourcePath(TEST_RESOURCES);
		Settings.loadSettings(TEST_RESOURCES, SETTINGS_CORRECT);
	}
	
	@Test
	public void testLoadGame() throws FileNotFoundException, IOException {
		
		SaveGame save = new SaveGame(TEST_RESOURCES + "savegame.xml");
		assertTrue(save.load());
		assertNotNull(save.getSlot(0));
		assertNotNull(save.getSlot(0).getTeam());
		assertEquals("slot 0", save.getSlot(0).getName());
		assertEquals("EOB_2", save.getDungeonName());
		
		Team team = new Team();
		assertEquals(true, team.load(save.getSlot(0).getTeam()));
		Dungeon dungeon = Resources.createDungeonResource(save.getDungeonName());
		assertNotNull(dungeon);
	}


}
