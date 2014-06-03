package com.ntk.darkmoor.resource;

import java.io.InputStream;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Resources {

	private AssetManager assetManager;
	
	public Resources() {
		assetManager = new AssetManager();
	}
	
	public Resources loadResources(String path) {
//		assetManager.load(path + "/", type)
		return this;
	}

	public static <T> T createAsset(Class<T> class1, String name) {
		// TODO Auto-generated method stub
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

	public static <T> T  createSharedAsset(Class<T> class1, String string, String string2) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void unlockSharedAsset(Class<BitmapFont> class1, BitmapFont font) {
		// TODO Auto-generated method stub
		
	}
}
