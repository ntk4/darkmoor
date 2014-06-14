package com.ntk.darkmoor.resource;

import java.io.InputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.ntk.darkmoor.config.LanguagesManager;
import com.ntk.darkmoor.engine.Item;
import com.ntk.darkmoor.exception.LoadException;

public class Resources {

	public static final String RESOURCE_PATH = "/data";
	public static final String STRING_TABLE_FILE = RESOURCE_PATH + LanguagesManager.DEFAULT_FILE;
	public static final String TEXTURE_SET_FILE = RESOURCE_PATH + "TextureSet.xml";
	public static final String FONT_FILE = RESOURCE_PATH + "fonts/font.fnt";
	public static final String FONT_IMAGE_FILE = RESOURCE_PATH + "fonts/font_0.png";
	private static BitmapFont bitmapFont;

	private AssetManager assetManager;

	// private GraphicAssets graphicAssets;

	public Resources() {
		assetManager = new AssetManager();

	}

	public Resources loadResources(String path) {
		// assetManager.load(path + "/", type)
		return this;
	}

	public static <T> T createAsset(Class<T> class1, String name) {

		try {
			return class1.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static TextureSet createTextureSetAsset(String name) {
		GraphicAssets textureAssets = new GraphicAssets(TEXTURE_SET_FILE);
		return textureAssets.load(name);
	}

	public static TextureSet loadSharedTextureSetAsset(String name) {
		return null;
	}

	public static <T> T lockSharedAsset(Class<T> class1, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public static InputStream load(String filename) {
		// TODO Auto-generated method stub
		return null;
	}

	public static <T> T createSharedAsset(Class<T> class1, String string, String string2) {
		// TODO Auto-generated method stub
		return null;
	}

	public static <T> T unlockSharedAsset(Class<T> class1, Object obj) {
		// TODO Auto-generated method stub
		return null;
	}

	public static TextureSet createSharedTextureSetAsset(String wallTilesetName, String wallTilesetName2) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void unlockSharedTextureSetAsset(TextureSet wallTileset) {
		// TODO Auto-generated method stub

	}

	public static TextureSet lockSharedTextureSetAsset(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void loadGameStartupResources() {
		LanguagesManager.getInstance(STRING_TABLE_FILE);
	}

	public static BitmapFont lockSharedFontAsset(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public static BitmapFont createSharedFontAsset(String name) {
		return createFontAsset(name); //TODO: ntk: define what shared means
	}

	public static void unlockSharedFontAsset(BitmapFont font) {
		// TODO Auto-generated method stub

	}

	public static BitmapFont createFontAsset(String name) {
		if (bitmapFont != null)
			return bitmapFont;
		try {
			bitmapFont = new BitmapFont(Gdx.files.internal(FONT_FILE), Gdx.files.internal(FONT_IMAGE_FILE), false);
			return bitmapFont;
		} catch (Exception e) {
			throw new LoadException(e);
		}
	}

	public static Item createItemAsset(String name) {
		return null;
	}
}
