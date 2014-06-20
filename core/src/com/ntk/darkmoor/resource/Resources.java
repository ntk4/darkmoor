package com.ntk.darkmoor.resource;

import java.io.InputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.ntk.darkmoor.config.LanguagesManager;
import com.ntk.darkmoor.engine.Dungeon;
import com.ntk.darkmoor.engine.Item;
import com.ntk.darkmoor.exception.LoadException;
import com.ntk.darkmoor.exception.ResourceException;

public class Resources {

	private static final String DEVILSUMMONEREXPAND_TTF = "devilsummonerexpand.ttf";
	private static final String DALELANDS_ITALIC_TTF = "Dalelands Italic.ttf";
	private static String resourcePath = "data/";
	public static final String TEXTURE_SET_FILE = "TextureSet.xml";
	public static final String FONT_FILE = "fonts/font1.fnt";
	public static final String FONT_IMAGE_FILE = "fonts/font1.png";
	private static final String FONT_PATH = getResourcePath() + "fonts/";
	private static BitmapFont font48, font64;

	private static AssetManager assetManager;

	// private GraphicAssets graphicAssets;

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
		initFonts();
		getAssetManager().finishLoading();
	}

	private static void initFonts() {
		try {
			FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal(FONT_PATH
					+ DEVILSUMMONEREXPAND_TTF));
			FreeTypeFontParameter parameter = new FreeTypeFontParameter();
			parameter.size = 48;
			font48 = gen.generateFont(parameter);

			parameter.size = 64;
			font64 = gen.generateFont(parameter);

			gen.dispose();
		} catch (Exception e) {
			throw new LoadException(e);
		}
	}

	public static void loadResources() {
		LanguagesManager.getInstance(getStringTableFile());

		// TextureParameter param = new TextureParameter();
		// param.minFilter = TextureFilter.Linear;
		// param.genMipMaps = true;Gdx.files.internal(getResourcePath() + "chargen.png").exists()
		// param.magFilter = TextureFilter.
		// getAssetManager().load(getResourcePath() + FONT_FILE, BitmapFont.class);
		getAssetManager().load(getResourcePath() + "chargen.png", Texture.class);
		// getAssetManager().load(getResourcePath() + "items.png", Texture.class);
		// getAssetManager().load(getResourcePath() + "main menu.png", Texture.class);
		// getAssetManager().finishLoading();
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
		if ("font48".equals(name))
			return font48;
		else if ("font64".equals(name))
			return font64;
		else
			return font48;
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

	public static String getSavegameFilename() {
		return getResourcePath() + "savegame.xml";
	}

	public static Dungeon createDungeonResource(String dungeonName) {
		Element root = ResourceUtility.extractRootElement(getResourcePath() + "dungeon.xml");

		if (root == null) {
			throw new ResourceException("The dungeon file dungeon.xml could not be read");
		}

		for (Element element : root.getChildrenByName("dungeon")) {
			if (dungeonName.equalsIgnoreCase(element.getAttribute("name"))) {
				Dungeon dungeon = new Dungeon();
				if (!dungeon.load(element))
					throw new LoadException("Could not load dungeon: " + dungeonName);

				return dungeon;

			}

		}
		throw new ResourceException("No dungeon exists with the name " + dungeonName);

	}

	public static Animation createAnimationAsset(String string) {
		// TODO Auto-generated method stub
		return null;
	}
}
