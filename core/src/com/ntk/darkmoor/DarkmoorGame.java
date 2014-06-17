package com.ntk.darkmoor;

import java.io.IOException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ntk.darkmoor.config.Settings;
import com.ntk.darkmoor.resource.Resources;

public class DarkmoorGame extends Game {
	public static final int GAME_HEIGHT = 400;
	public static final int GAME_WIDTH = 640;
	
	public static final int DISPLAY_HEIGHT = 720;
	public static final int DISPLAY_WIDTH = 1280;
	
	public static final String DATA_ASSET_FOLDER = "./data";
	
	SpriteBatch batch;
	Texture img;
	Resources resources;
	OrthographicCamera camera;
	Viewport viewport;

	@Override
	public void create() {
		resources = new Resources().loadResources(DATA_ASSET_FOLDER);
		batch = new SpriteBatch();
		loadStartupData();
		camera = new OrthographicCamera(DISPLAY_WIDTH, DISPLAY_HEIGHT);
		viewport = new FitViewport(DISPLAY_WIDTH, DISPLAY_HEIGHT);
		setScreen(new MainMenu(this));
	}

	private void loadStartupData() {
		Resources.loadGameStartupResources();
		try {
			Settings.loadSettings(Gdx.files.internal("settings.properties"));
		} catch (IOException e) {
			exit();
		}
	}

	// @Override
	// public void render () {
	// Gdx.gl.glClearColor(1, 0, 0, 1);
	// Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	// batch.begin();
	// batch.draw(img, 0, 0, 700, 899);
	// batch.end();
	// }

	public static void exit() {
		// TODO Auto-generated method stub

	}

	public SpriteBatch getBatch() {
		return batch;
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		viewport.update(width, height);
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public Viewport getViewport() {
		return viewport;
	}
}
