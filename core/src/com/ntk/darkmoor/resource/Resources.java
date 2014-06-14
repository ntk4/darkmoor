package com.ntk.darkmoor.resource;

import java.io.InputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.ntk.darkmoor.exception.LoadException;

public class Resources {

	public static final String RESOURCE_PATH = "/data";
	public static final String TEXTURE_SET_FILE = RESOURCE_PATH + "TextureSet.xml";
	
	private AssetManager assetManager;
	private GraphicAssets graphicAssets;
	
	public Resources() {
		assetManager = new AssetManager();
		
	}
	
	public Resources loadResources(String path) {
//		assetManager.load(path + "/", type)
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

	public static <T> T lockSharedAsset(Class<T> class1, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public static InputStream load(String filename) {
		// TODO Auto-generated method stub
		return null;
	}

	public static <T> T  createSharedAsset(Class<T> class1, String string, String string2) {
		// TODO Auto-generated method stub
		return null;
	}

	public static <T> T unlockSharedAsset(Class<T> class1, Object obj) {
		// TODO Auto-generated method stub
		return null;
	}
}
