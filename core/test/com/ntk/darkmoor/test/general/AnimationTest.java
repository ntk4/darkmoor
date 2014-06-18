package com.ntk.darkmoor.test.general;

import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.ntk.darkmoor.resource.AnimationAssets;
import com.ntk.darkmoor.resource.Resources;

public class AnimationTest extends BaseTestCase {


	@Before
	public void setUp() {
		Resources.setResourcePath(TEST_RESOURCES);
		// Resources.loadGameStartupResources(); //doesn't work on JUnit
	}
	
	@Test
	public void testAnimationAssets() throws FileNotFoundException, IOException {
//		GraphicAssets.getAssets(TEST_RESOURCES + "TextureSet.xml"); //initialization only
//		AnimationAssets.getInstance(TEST_RESOURCES + "Animation.xml"); //initialization only
		
		Animation animation = AnimationAssets.getAnimation("TextureSet.xml", "Animations");
		testAnimationAttributes(animation);
	}

	private void testAnimationAttributes(Animation animation) {
		assertNotNull(animation);
	}
	
}
