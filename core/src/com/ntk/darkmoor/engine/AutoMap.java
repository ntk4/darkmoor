package com.ntk.darkmoor.engine;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ntk.darkmoor.engine.gui.GUI;
import com.ntk.darkmoor.resource.Resources;
import com.ntk.darkmoor.stub.Display;
import com.ntk.darkmoor.stub.TileSet;

public class AutoMap extends ScreenAdapter {
	
	private SpriteBatch batch;
	private TileSet tileSet;

	public AutoMap(SpriteBatch batch) {
		this.batch = batch;
	}
	
	@Override
	public void resume() {
		super.resume();

		this.tileSet = Resources.createAsset(TileSet.class, "AutoMap");
	}

	@Override
	public void dispose() {
		super.dispose();
		
		batch = null;
		
		if (tileSet != null) 
			tileSet.dispose();
		tileSet = null;
	}
	
	public void draw() {
		// Clears the background
		Display.clearBuffers();

		batch.begin();

		// TODO: ntk: uncomment the drawTile as soon as a Gdx equivalent is found
		// Background
//		batch.drawTile(tileSet, 1, new Vector2(), Color.WHITE);

		// Some WIP
		GUI.getMenuFont().setColor(Color.WHITE);
		GUI.getMenuFont().draw(batch, "TODO...", 100, 100);	

		// Draw the cursor or the item in the hand
//		batch.drawTile(tileSet, 0, Gdx.input.getX(), Gdx.input.getY(), Color.WHITE);

		batch.end();
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public void setBatch(SpriteBatch batch) {
		this.batch = batch;
	}

	public TileSet getTileSet() {
		return tileSet;
	}

	public void setTileSet(TileSet tileSet) {
		this.tileSet = tileSet;
	}
}
