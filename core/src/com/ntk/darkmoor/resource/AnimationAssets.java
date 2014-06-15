package com.ntk.darkmoor.resource;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.ntk.darkmoor.exception.LoadException;

public class AnimationAssets implements Disposable {

	public static final String ANIMATIONS_FILE = Resources.getResourcePath() + "Animation.xml";

	private static AnimationAssets instance;
	private Map<String, Animation> animationCache;
	private Element rootElement;

	/**
	 * Used only for the JUnit tests that can't access Gdx.files.*
	 * 
	 * @param AnimationsXmlFile
	 * @return
	 */
	public static AnimationAssets getInstance(String animationsXmlFile) {
		if (instance != null)
			return instance;
		instance = new AnimationAssets(animationsXmlFile);
		return instance;
	}

	/**
	 * Singleton access method to be used internally. For any reference to Animations see getAnimation
	 * 
	 * @return
	 */
	private static AnimationAssets getInstance() {
		if (instance != null)
			return instance;
		instance = new AnimationAssets(ANIMATIONS_FILE);
		return instance;
	}

	public static Animation getAnimation(String graphicsFile, String animationName) {
		return getInstance().load(graphicsFile, animationName);
	}

	private AnimationAssets(String fileName) {
		this.rootElement = ResourceUtility.extractRootElement(fileName);
		animationCache = new HashMap<String, Animation>();
	}

	private Animation load(String graphicsFile, String AnimationName) throws LoadException {

		Animation Animation = animationCache.get(AnimationName);
		if (Animation != null)
			return Animation;
		try {

			if (rootElement == null) {
				throw new LoadException("Animation resources have not been initialized, cannot load " + AnimationName);
			}

			Array<Element> children = rootElement.getChildrenByName("animation");
			GraphicAssets graphics = GraphicAssets.getAssets(graphicsFile);
			for (Element child : children) {
				if (AnimationName.equalsIgnoreCase(child.getAttribute("name"))) {

					Animation = load(graphics, child);
					animationCache.put(AnimationName, Animation);
					break;
				}
			}
		} catch (Exception e) {
			throw new LoadException(e);
		}
		return Animation;
	}

	private Animation load(GraphicAssets graphics, Element child) {
		String framerate = child.getChildByName("framerate").getAttribute("value");
		int beginIndex = framerate.indexOf('.') + 1;
		int millis = Integer.parseInt(framerate.substring(beginIndex, beginIndex + 3));

		String textureSetName = child.getChildByName("tileset").getAttribute("name");
		boolean loop = "loop".equalsIgnoreCase(child.getChildByName("loop").getAttribute("value"));

		TextureSet textureSet = graphics.load(textureSetName);
		Array<Element> textureElements = child.getChildrenByName("tile");
		Array<Sprite> sprites = new Array<Sprite>(textureElements.size);

		if (!ResourceUtility.isStandaloneMode()) {
			for (Element element : textureElements) {
				sprites.add(textureSet.getSprite(Integer.parseInt(element.getAttribute("id"))));
			}
		}

		Animation anim = new Animation((float) millis / 1000, sprites, loop ? PlayMode.LOOP : PlayMode.NORMAL);
		return anim;
	}

	@Override
	public void dispose() {
		animationCache.clear();
		animationCache = null;
		rootElement = null;
	}
}
