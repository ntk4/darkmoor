package com.ntk.darkmoor.resource;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.utils.Timer.Task;
import com.ntk.darkmoor.engine.Dungeon;

public class AssetLoadingTask extends Task {

	private AssetManager assetManager;
	private String resourcePath;
	private String dungeonName;
	
	public AssetLoadingTask(AssetManager assetManager, String resourcePath, String dungeonName) {
		this.assetManager = assetManager;
		this.resourcePath = resourcePath;
		this.dungeonName = dungeonName;
	}
	
	@Override
	public void run() {

		 TextureParameter param = new TextureParameter();
		 param.minFilter = TextureFilter.Nearest;
		 param.genMipMaps = true;
		 param.magFilter = TextureFilter.Linear;

		assetManager.load(resourcePath + "items.png", Texture.class, param);
		assetManager.load(resourcePath + "Heads.png", Texture.class, param);
		assetManager.load(resourcePath + "interface.png", Texture.class, param);
		assetManager.load(resourcePath + "wall-forest.png", Texture.class, param);
		assetManager.load(resourcePath + "wall-temple.png", Texture.class, param);
		assetManager.load(resourcePath + "wall-catacomb.png", Texture.class, param);
		assetManager.load(resourcePath + "Decoration-Forest.png", Texture.class, param);
		assetManager.load(resourcePath + "Decoration-Temple.png", Texture.class, param);
		assetManager.load(resourcePath + "Decoration-Catacomb.png", Texture.class, param);
		assetManager.load(resourcePath + "Doors_new.png", Texture.class, param);
		assetManager.finishLoading();
		
		Dungeon dungeon = Resources.createDungeonResource(dungeonName, null);
		dungeon.init();
		Resources.setDungeon(dungeon);
	}

}
