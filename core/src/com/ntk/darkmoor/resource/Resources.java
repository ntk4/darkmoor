package com.ntk.darkmoor.resource;

import com.badlogic.gdx.assets.AssetManager;

public class Resources {

	private AssetManager assetManager;
	
	public Resources() {
		assetManager = new AssetManager();
	}
	
	public Resources loadResources(String path) {
//		assetManager.load(path + "/", type)
		return this;
	}
}
