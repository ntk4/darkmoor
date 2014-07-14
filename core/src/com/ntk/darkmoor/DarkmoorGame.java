package com.ntk.darkmoor;

import java.io.IOException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.badlogic.gdx.utils.async.AsyncTask;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ntk.darkmoor.config.SaveGame;
import com.ntk.darkmoor.config.Settings;
import com.ntk.darkmoor.engine.Dungeon;
import com.ntk.darkmoor.resource.Resources;

public class DarkmoorGame extends Game {
	private static final int MAX_THREADS = 2;
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
	private AsyncExecutor executor;

	public SaveGame savedGame;
	

	@Override
	public void create() {
		instance = this;
		executor = new AsyncExecutor(MAX_THREADS);
		loadStartupData();
		camera = new OrthographicCamera(DISPLAY_WIDTH, DISPLAY_HEIGHT);
		// camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		// camera.update();

		batch = new SpriteBatch();

		viewport = new FitViewport(DISPLAY_WIDTH, DISPLAY_HEIGHT);
		setScreen(getMainMenu());
//		initMusic();
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

	public void stopMusic() {
		if (theme != null && theme.isPlaying())
			theme.stop();
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
		try {
			Settings.loadSettings(Gdx.files.internal(Resources.getResourcePath() + "settings.properties"));

			loadSaveGameFile(Gdx.files.internal(Resources.getResourcePath() + "/savegame.xml"));
		} catch (IOException e) {
			exit();
		}

		Resources.loadGameStartupResources();
	}

	private void loadSaveGameFile(FileHandle handle) {
		savedGame = new SaveGame(handle.path());
		if (handle.exists())
			savedGame.load();
	}

	private void loadInGameData() {
		Resources.loadGameResources();
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
		instance = null;
		Gdx.app.exit();
	}

	@Override
	public void dispose() {
		if (gameScreen != null)
			gameScreen.dispose();
		if (mainMenu != null)
			mainMenu.dispose();
		stopMusic();
//		theme.dispose();
		batch.dispose();

		Resources.unloadResources();
		super.dispose();
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

	public boolean loadGameSlot(int selectedSlot) {
		if (savedGame.isSlotEmpty(selectedSlot))
			return false;
		getGameScreen().loadGameSlot(selectedSlot);
		return true;
	}
	
	public void submitTask(AsyncTask<Dungeon> task) {
//		timer.scheduleTask(task, 1);
		executor.submit(task);
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

	public SaveGame getSavedGame() {
		return savedGame;
	}

	public void setSavedGame(SaveGame savedGame) {
		this.savedGame = savedGame;
	}
}
