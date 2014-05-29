package com.ntk.darkmoor.resource;

import com.badlogic.gdx.assets.AssetManager;
import com.ntk.darkmoor.stub.Monster;

public class Resources {

	private AssetManager assetManager;
	
	public Resources() {
		assetManager = new AssetManager();
	}
	
	public Resources loadResources(String path) {
//		assetManager.load(path + "/", type)
		return this;
	}

	public static Monster createAsset(Class<?> class1, String monsterName) {
		// TODO Auto-generated method stub
		return null;
	}
}
