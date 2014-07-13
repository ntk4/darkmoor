package com.ntk.darkmoor.test.general;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.ntk.darkmoor.config.Settings;
import com.ntk.darkmoor.engine.DisplayCoordinates;
import com.ntk.darkmoor.engine.Dungeon;
import com.ntk.darkmoor.resource.Resources;

public class MazeElementsTest extends BaseTestCase {

	@Before
	public void setUp() throws IOException {
		Resources.setResourcePath("../android/assets/data/");
		Settings.loadSettings(TEST_RESOURCES, SETTINGS_CORRECT);
	}
	
	@Test
	public void testLoadMazeElements() throws FileNotFoundException, IOException {
		
		DisplayCoordinates.load();
	}
	
	@Test
	public void testLoadDungeon() throws FileNotFoundException, IOException {
		
		testLoadMazeElements();
		
		Dungeon dungeon = Resources.createDungeonResource("EOB_2", "Temple_01");
		assertNotNull(dungeon);
		assertTrue(dungeon.init());
	}


}
