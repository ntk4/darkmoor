package com.ntk.darkmoor;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.ntk.darkmoor.config.Settings;
import com.ntk.darkmoor.resource.Resources;
import com.ntk.darkmoor.stub.Display;
import com.ntk.darkmoor.stub.GameScreenBase;
import com.ntk.darkmoor.stub.Scene;

public class IntroScreen extends GameScreenBase {

	private Scene scene;
	private BitmapFont font;

	public IntroScreen(Game game) {
		super(game);
		Display.init();
	}

	@Override
	public void loadContent() {
		scene = Resources.createAsset(Scene.class, "intro");
		// Scene.Font.GlyphTileset.Scale = new Vector2(2, 2);
		scene.getLanguagesManager().setLanguage(Settings.getLastLoadedInstance().getLanguage());

		font = Resources.createFontAsset("intro");
		// Font.GlyphTileset.Scale = new Vector2(2, 2);

		// spriteBatch = new SpriteBatch();
	}

	@Override
	public void unloadContent() {

		if (font != null)
			font.dispose();
		font = null;

		if (scene != null)
			scene.dispose();
		scene = null;
	}

	@Override
	public void update(float delta, boolean hasFocus, boolean isCovered) {
		// No focus byebye
		if (!hasFocus)
			return;

		// Bye bye
		if (Gdx.input.isKeyPressed(Keys.ESCAPE)) // check for mobile back button
			ScreenManager.exitScreen();
		// ScreenManager.Game.Exit();

		// Pause animation
		if (Gdx.input.isKeyPressed(Keys.SPACE))
			scene.setPause(!scene.isPause());

		// Rewind animation
		if (Gdx.input.isKeyPressed(Keys.LEFT))
			scene.setTime(0);

		// Update animation
		if (scene != null)
			scene.update(delta);
	}

	@Override
	public void draw(float delta) {
		// Clears the background
		Display.clearBuffers();

		// spriteBatch.begin();

		if (scene != null)
			scene.draw(game.getBatch());

		// Debug info
		font.setColor(Color.WHITE);
		font.draw(game.getBatch(), String.valueOf(scene.getTime() / 60), 20, 160);

		// spriteBatch.end();
	}
}
