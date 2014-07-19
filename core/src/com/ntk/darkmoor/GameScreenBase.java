package com.ntk.darkmoor;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.ntk.darkmoor.resource.Resources;
import com.ntk.darkmoor.stub.Display;

public abstract class GameScreenBase extends ScreenAdapter implements Disposable {

	protected TextButton[] buttons;
	protected DarkmoorGame game;
	protected Stage stage;
	private SpriteBatch batch;

	protected Skin uiSkin;

	private FPSLogger fpsLogger;

	public GameScreenBase(Game game) {
		this.game = (DarkmoorGame) game;
		batch = this.game.getBatch();
		loadContent();
		fpsLogger = new FPSLogger();
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		update(delta, true, false);
		draw(delta);
//		fpsLogger.log();

	}

	public void loadContent() {
		BitmapFont font48 = Resources.createFontAsset("font48");
		BitmapFont font64 = Resources.createFontAsset("font64");
		BitmapFont gameFont48 = Resources.createFontAsset("gameFont48");

		uiSkin = new Skin();
		uiSkin.addRegions(new TextureAtlas(Gdx.files.internal("data/skin/uiskin.atlas")));
		uiSkin.add("font48", font48);
		uiSkin.add("font64", font64);
		uiSkin.add("gameFont48", gameFont48);

		uiSkin.load(Gdx.files.internal("data/skin/uiskin.json"));
		uiSkin.remove("default-font", BitmapFont.class);
		setupButtons(uiSkin);
		setupStage();
	}

	public void unloadContent() {
		uiSkin = null;
		stage = null;
		game = null;
		buttons = null;
		fpsLogger = null;
	}

	@Override
	public void dispose() {
		if (stage != null)
			stage.dispose();
		if (uiSkin != null)
			uiSkin.dispose();
		unloadContent();
		super.dispose();
	}

	public void update(float delta, boolean hasFocus, boolean isCovered) {
	}

	public void draw(float delta) {
		// Clears the background
		Display.clearBuffers();

		batch.begin();

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();

		batch.end();
	}

	public void exitScreen() {
	}

	public void onLeave() {
	}

	public void onEnter() {
	}

	public void setScreen(GameScreenBase newScreen) {
		this.onLeave();
		game.setScreen(newScreen);
		this.unloadContent();
	}

	protected Image setupBackground() {
		return null;
	}

	protected void setupButtons(Skin uiSkin) {
	}

	protected void setupStage() {
		stage = new Stage(new StretchViewport(DarkmoorGame.DISPLAY_WIDTH, DarkmoorGame.DISPLAY_WIDTH));

		Image background = setupBackground();
		if (background != null)
			stage.addActor(background);

		if (buttons != null) {
			for (TextButton button : buttons) {
				stage.addActor(button);
			}
		}

		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		// center the camera: sets stage coordinates 0,0 to bottom left instead of screen center
		stage.getViewport().update(width, height, true);
	}

	public Skin getSkin() {
		return uiSkin;
	}

	public Stage getStage() {
		return stage;
	}

	public DarkmoorGame getGame() {
		return game;
	}
}
