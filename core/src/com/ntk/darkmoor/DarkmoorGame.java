package com.ntk.darkmoor;

import java.io.IOException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ntk.darkmoor.config.SaveGame;
import com.ntk.darkmoor.config.Settings;
import com.ntk.darkmoor.resource.Resources;

public class DarkmoorGame extends Game {
	public static final int GAME_HEIGHT = 400;
	public static final int GAME_WIDTH = 640;

	public static final int DISPLAY_HEIGHT = 720;
	public static final int DISPLAY_WIDTH = 1280;

	public static final String DATA_ASSET_FOLDER = "./data";

	private static DarkmoorGame instance;	
	
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Viewport viewport;
	private MainMenu mainMenu;
	private GameScreen gameScreen;
	private Music theme;

	@Override
	public void create() {
		instance = this;
		loadStartupData();
		camera = new OrthographicCamera(DISPLAY_WIDTH, DISPLAY_HEIGHT);
		// camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		// camera.update();

		batch = new SpriteBatch();

		viewport = new FitViewport(DISPLAY_WIDTH, DISPLAY_HEIGHT);
//		mainMenu = new MainMenu(this);
		setScreen(getMainMenu());

		initMusic();
	}

	private void initMusic() {
		theme = Gdx.audio.newMusic(Gdx.files.internal("data/main.ogg"));
		theme.setLooping(true);
		theme.play();
	}

	public void pauseMusic() {
		if (theme != null && theme.isPlaying())
			theme.pause();
	}

	public void resumeMusic() {
		if (theme != null && !theme.isPlaying() && Settings.getLastLoadedInstance().isMusic())
			theme.play();
	}

	@Override
	public void pause() {
		pauseMusic();

		super.pause();
	}

	@Override
	public void resume() {
		resumeMusic();

		super.resume();
	}

	private void loadStartupData() {
		Resources.loadGameStartupResources();
		try {
			Settings.loadSettings(Gdx.files.internal(Resources.getResourcePath() + "settings.properties"));
		} catch (IOException e) {
			exit();
		}
	}
	
	private void loadInGameData() {
		Resources.loadResources();
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
		Gdx.app.exit();
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

	public Vector2 getInputCoordinates(Vector2 position) {
		position.x = Gdx.input.getX();
		position.y = DISPLAY_HEIGHT - Gdx.input.getY();
		return position;
	}

	public MainMenu getMainMenu() {
		if (mainMenu == null) {
			mainMenu = new MainMenu(this);
			loadInGameData();
		}
		return mainMenu;
	}

	public void loadGameSlot(int selectedSlot) {
		getGameScreen().loadGameSlot(selectedSlot);
	}

	public GameScreen getGameScreen() {
		if (gameScreen == null) {
			gameScreen = new GameScreen(this);
		}
		return gameScreen;
	}

	public static DarkmoorGame getInstance() {
		return instance;
	}
}
