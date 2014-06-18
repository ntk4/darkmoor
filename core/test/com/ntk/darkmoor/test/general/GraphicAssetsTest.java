package com.ntk.darkmoor.test.general;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import com.ntk.darkmoor.resource.GraphicAssets;
import com.ntk.darkmoor.resource.TextureSet;

public class GraphicAssetsTest extends BaseTestCase {

	@Test
	public void testLoadInterfaceAssets() throws FileNotFoundException, IOException {

		GraphicAssets assets = GraphicAssets.getAssets("TextureSet.xml");
		TextureSet set = assets.load("Interface");
		assertNotNull(set);
		assertEquals("Interface", set.getName());
		assertEquals("interface.png", set.getTextureFile());
		assertNotNull(set.getMetadata());
		assertEquals(32, set.getMetadata().size);
		assertEquals(598, (int) set.getMetadata().get(29).getRectangle().x);
		assertEquals(402, (int) set.getMetadata().get(29).getRectangle().y);
		assertEquals(32, (int) set.getMetadata().get(29).getRectangle().width);
		assertEquals(16, (int) set.getMetadata().get(29).getRectangle().height);
	}

	@Test
	public void testSaveDataToXml() throws FileNotFoundException, IOException {
		GraphicAssets assets = GraphicAssets.getAssets("TextureSet.xml");
		TextureSet set1 = assets.load("Interface");

		assets = GraphicAssets.getAssets(TEST_RESOURCES + "TextureSet_temp.xml");
		assets.save(set1);
		GraphicAssets assets2 = GraphicAssets.getAssets("TextureSet_temp.xml");
		TextureSet set2 = assets2.load("Interface");

		new File(TEST_RESOURCES + "TextureSet_temp.xml").delete();
		compareTextureSets(set1, set2);
	}

	private void compareTextureSets(TextureSet set, TextureSet set2) {
		assertNotNull(set);
		assertNotNull(set2);
		assertEquals(set.getName(), set2.getName());
		assertEquals(set.getTextureFile(), set2.getTextureFile());
		assertNotNull(set.getMetadata());
		assertNotNull(set2.getMetadata());
		assertEquals(set.getMetadata().size, set2.getMetadata().size);
		assertEquals((int) set.getMetadata().get(29).getRectangle().x,
				(int) set2.getMetadata().get(29).getRectangle().x);
		assertEquals((int) set.getMetadata().get(29).getRectangle().y,
				(int) set2.getMetadata().get(29).getRectangle().y);
		assertEquals((int) set.getMetadata().get(29).getRectangle().width, (int) set2.getMetadata().get(29)
				.getRectangle().width);
		assertEquals((int) set.getMetadata().get(29).getRectangle().height, (int) set2.getMetadata().get(29)
				.getRectangle().height);
	}
}
