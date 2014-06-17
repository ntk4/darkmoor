package com.ntk.darkmoor.resource;

import java.io.InputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.ntk.darkmoor.config.LanguagesManager;
import com.ntk.darkmoor.engine.Item;
import com.ntk.darkmoor.exception.LoadException;

public class Resources {

	private static String resourcePath = "data/";
	public static final String TEXTURE_SET_FILE = "TextureSet.xml";
	public static final String FONT_FILE = "fonts/font1.fnt";
	public static final String FONT_IMAGE_FILE = "fonts/font1.png";
	private static BitmapFont bitmapFont;

	private static AssetManager assetManager;

	// private GraphicAssets graphicAssets;

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
		GraphicAssets textureAssets = GraphicAssets.getAssets(TEXTURE_SET_FILE);
		return textureAssets.load(name);
	}
	
	public static Texture createTextureAsset(String name) {
		return assetManager.get(getResourcePath() + name + ".png");
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
		LanguagesManager.getInstance(getStringTableFile());

		// TextureParameter param = new TextureParameter();
		// param.minFilter = TextureFilter.Linear;
		// param.genMipMaps = true;Gdx.files.internal(getResourcePath() + "chargen.png").exists()
		// param.magFilter = TextureFilter.
		getAssetManager().load(getResourcePath() + FONT_FILE, BitmapFont.class);
		// getAssetManager().load(getResourcePath() + "chargen.png", Texture.class);
		getAssetManager().load(getResourcePath() + "items.png", Texture.class);
		getAssetManager().load(getResourcePath() + "mainmenu.png", Texture.class);
		getAssetManager().finishLoading();
	}
	
	public static void loadResources() {
		LanguagesManager.getInstance(getStringTableFile());

		// TextureParameter param = new TextureParameter();
		// param.minFilter = TextureFilter.Linear;
		// param.genMipMaps = true;Gdx.files.internal(getResourcePath() + "chargen.png").exists()
		// param.magFilter = TextureFilter.
//		getAssetManager().load(getResourcePath() + FONT_FILE, BitmapFont.class);
		 getAssetManager().load(getResourcePath() + "chargen.png", Texture.class);
//		getAssetManager().load(getResourcePath() + "items.png", Texture.class);
//		getAssetManager().load(getResourcePath() + "main menu.png", Texture.class);
//		getAssetManager().finishLoading();
	}

	public static BitmapFont lockSharedFontAsset(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public static BitmapFont createSharedFontAsset(String name) {
		return createFontAsset(name); // TODO: ntk: define what shared means
	}

	public static void unlockSharedFontAsset(BitmapFont font) {
		// TODO Auto-generated method stub

	}

	public static BitmapFont createFontAsset(String name) {
		if (bitmapFont != null)
			return bitmapFont;
		try {
			bitmapFont = assetManager.get(getResourcePath() + FONT_FILE, BitmapFont.class);
			return bitmapFont;
		} catch (Exception e) {
			throw new LoadException(e);
		}
	}

	public static AssetManager getAssetManager() {
		if (assetManager == null)
			assetManager = new AssetManager();
		return assetManager;
	}

	public static Item getItemAsset(String name) {
		return ItemAssets.getItem(name);
	}

	public static String getResourcePath() {
		return resourcePath;
	}

	public static String getStringTableFile() {
		return resourcePath + LanguagesManager.DEFAULT_FILE;
	}

	public static String getTextureSetFile() {
		return resourcePath + TEXTURE_SET_FILE;
	}

	public static String getFontFile() {
		return resourcePath + FONT_FILE;
	}

	public static String getFontImageFile() {
		return resourcePath + FONT_IMAGE_FILE;
	}

	/**
	 * The setter is useful for tests
	 * 
	 * @param resourcePath
	 */
	public static void setResourcePath(String resourcePath) {
		Resources.resourcePath = resourcePath;
	}
}
