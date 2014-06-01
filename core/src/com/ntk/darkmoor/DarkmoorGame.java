package com.ntk.darkmoor;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ntk.darkmoor.resource.Resources;

public class DarkmoorGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Resources resources;
	
	@Override
	public void create () {
		resources = new Resources().loadResources("./data");
		batch = new SpriteBatch();
		img = new Texture("EOB2.jpg"); //size: 700x899
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0, 700, 899);
		batch.end();
	}

	public static void exit() {
		// TODO Auto-generated method stub
		
	}
}
