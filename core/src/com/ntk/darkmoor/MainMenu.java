package com.ntk.darkmoor;

import org.ntk.commons.StringUtils;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.ntk.darkmoor.config.LanguagesManager;
import com.ntk.darkmoor.config.Log;
import com.ntk.darkmoor.config.Settings;
import com.ntk.darkmoor.engine.GameColors;
import com.ntk.darkmoor.engine.gui.ScreenButton;
import com.ntk.darkmoor.engine.gui.campwindows.LoadGameWindow;
import com.ntk.darkmoor.resource.Resources;
import com.ntk.darkmoor.resource.TextureSet;
import com.ntk.darkmoor.stub.Display;
import com.ntk.darkmoor.stub.GameScreen;
import com.ntk.darkmoor.stub.GameScreenBase;
import com.ntk.darkmoor.stub.OptionMenu;

public class MainMenu extends GameScreenBase {

	private ScreenButton[] buttons;
	private TextureSet textureSet;
	private BitmapFont font;
	private LanguagesManager stringTable;
	private Music theme;

	private LoadGameWindow loadGame;
	private String languageName;
	private int menuID;

	public MainMenu(Game game) {
		super(game);
	}

	@Override
	public void loadContent() {
		Log.debug("[MainMenu] loadContent()");

		// Batch = new SpriteBatch();

		textureSet = Resources.createTextureSetAsset("main menu");

		font = Resources.createSharedFontAsset("intro");

		stringTable = LanguagesManager.getInstance(); // "main";

		buttons = new ScreenButton[4];
		buttons[0] = new ScreenButton("", new Rectangle(156, 324, 340, 14));
		buttons[0].addListener(new EventListener() {
			@Override
			public boolean handle(Event event) {
				loadGame = new LoadGameWindow(null, null);// TODO: ntk: give a skin
				return true;
			}
		});

		buttons[1] = new ScreenButton("", new Rectangle(156, 342, 340, 14));
		buttons[1].addListener(new EventListener() {
			@Override
			public boolean handle(Event event) {
				game.setScreen(new CharGen(game));
				exitScreen();
				return true;
			}
		});

		buttons[2] = new ScreenButton("", new Rectangle(156, 360, 340, 14));
		buttons[2].addListener(new EventListener() {
			@Override
			public boolean handle(Event event) {
				game.setScreen(new OptionMenu(game));
				return true;
			}
		});

		buttons[3] = new ScreenButton("", new Rectangle(156, 378, 340, 14));
		buttons[3].addListener(new EventListener() {
			@Override
			public boolean handle(Event event) {
				DarkmoorGame.exit();
				return true;
			}
		});

		theme = Gdx.audio.newMusic(Gdx.files.internal("data/main.ogg"));
		theme.setLooping(true);
		theme.play();

	}

	@Override
	public void unloadContent() {
		Log.debug("[MainMenu] : unloadContent");

		if (textureSet != null)
			textureSet.dispose();
		textureSet = null;

		Resources.unlockSharedFontAsset(font);
		font = null;

		if (theme != null)
			theme.dispose();
		theme = null;

		// if (batch != null)
		// batch.dispose();
		// batch = null;

		// StringTable.Dispose();
		// StringTable = null;

		// buttons.clear();
		buttons = null;
	}

	@Override
	public void onLeave() {
		super.onLeave();

		if (theme != null && theme.isPlaying())
			theme.stop();
	}

	@Override
	public void onEnter() {
		super.onEnter();

		if (theme != null && !theme.isPlaying())
			theme.play();
	}

	@Override
	public void update(float delta, boolean hasFocus, boolean isCovered) {
		// No focus byebye
		if (!hasFocus)
			return;

		// Play sound
		// if (Theme.State != AudioSourceState.Playing)
		// Theme.Play();

		if (Gdx.input.isKeyPressed(Keys.S))
			theme.stop();

		if (Gdx.input.isKeyPressed(Keys.P))
			theme.play();

		// ntk
		// Does the default language change ?
		if (stringTable == null || !StringUtils.equals(Settings.getLastLoadedInstance().getLanguage(), languageName)) {
			// StringTable = new StringTable();
			this.languageName = Settings.getLastLoadedInstance().getLanguage();

			for (int id = 0; id < buttons.length; id++)
				buttons[id].setText(stringTable.getString(languageName, "main", id + 1));
		}

		// Mouse interaction
		Vector2 mousePos = new Vector2(Gdx.input.getX(), Gdx.input.getY());

		if (loadGame == null) { // section Main menu
			for (int id = 0; id < buttons.length; id++) {
				ScreenButton button = buttons[id];

				// Mouse over ?
				if (button.getRectangle().contains(mousePos)) {
					// button.TextColor = Color.FromArgb(255, 85, 85);
					menuID = id;
					if (Gdx.input.isButtonPressed(Buttons.LEFT))
						button.onSelectEntry();
				}

			}

			// Bye bye
			if (Gdx.input.isKeyPressed(Keys.ESCAPE))
				DarkmoorGame.exit();

			// Run intro
			if (Gdx.input.isKeyPressed(Keys.I))
				game.setScreen(new IntroScreen(game));

			// Key up
			if (Gdx.input.isKeyPressed(Keys.UP)) {
				menuID--;
				if (menuID < 0)
					menuID = buttons.length - 1;
			}
			// Key down
			else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
				menuID++;
				if (menuID >= buttons.length)
					menuID = 0;
			}
			// Enter
			else if (Gdx.input.isKeyPressed(Keys.ENTER)) {
				buttons[menuID].onSelectEntry();
			}
		} else {// section Load game
			// Load a game
			loadGame.update(delta);

			// A slot is selected
			if (loadGame.getSelectedSlot() != -1) {
				// close window
				loadGame.close();

				// Run the game
				GameScreen scr = new GameScreen(game);
				game.setScreen(scr);

				// Load saved game
				scr.loadGameSlot(loadGame.getSelectedSlot());
			}

			// Close the window
			if (loadGame.isClosing())
				loadGame = null;
		}
	}

	@Override
	public void draw(float delta) {
		// Clears the background
		Display.clearBuffers();
		SpriteBatch batch = game.getBatch();

		batch.begin();

		// Background
		batch.draw(textureSet.getSprite(0), 0, 0, 640, 400);

		// Draw buttons
		for (int id = 0; id < buttons.length; id++) {
			ScreenButton button = buttons[id];

			Color textColor = id == menuID ? GameColors.getColor(255, 85, 85) : Color.WHITE;
			font.setColor(textColor);
			font.draw(batch, button.getText(), button.getRectangle().x, button.getRectangle().y);

			// Batch.DrawString(Font, new Vector2(button.Rectangle.Location.X, button.Rectangle.Location.Y),
			// id == MenuID ? Color.FromArgb(255, 85, 85) : Color.White, button.Text);
		}

		if (loadGame != null)
			loadGame.draw(batch, 0.5f);

		batch.end();
	}

}
