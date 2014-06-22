package com.ntk.darkmoor.test.general;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.utils.XmlReader;
import com.ntk.darkmoor.engine.DecorationSet;
import com.ntk.darkmoor.resource.DecorationAssets;
import com.ntk.darkmoor.resource.ResourceUtility;
import com.ntk.darkmoor.resource.Resources;

public class DecorationAssetsTest extends BaseTestCase {

	@Before
	public void setUp() {
		Resources.setResourcePath("../android/assets/data/");
		// Resources.loadGameStartupResources(); //doesn't work on JUnit
	}

	@Test
	public void testLoadDecorationAssets() {

		DecorationSet deco = DecorationAssets.getAssets().getDecorationSet("Temple");
		
		assertNotNull(deco);
	}
}
