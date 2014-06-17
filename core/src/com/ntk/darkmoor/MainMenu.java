package com.ntk.darkmoor;

import org.ntk.commons.StringUtils;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.ntk.darkmoor.config.LanguagesManager;
import com.ntk.darkmoor.config.Log;
import com.ntk.darkmoor.config.Settings;
import com.ntk.darkmoor.engine.gui.campwindows.LoadGameWindow;
import com.ntk.darkmoor.resource.Resources;
import com.ntk.darkmoor.resource.TextureSet;
import com.ntk.darkmoor.stub.Display;
import com.ntk.darkmoor.stub.GameScreen;
import com.ntk.darkmoor.stub.GameScreenBase;
import com.ntk.darkmoor.stub.OptionMenu;

public class MainMenu extends GameScreenBase {

	private static final int FONT_Y_OFFSET = 25;
	private TextButton[] buttons;
	private TextureSet textureSet;
	private BitmapFont font;
	private LanguagesManager stringTable;
	private Music theme;

	private LoadGameWindow loadGame;
	private String languageName;

	private Stage stage;
	private Image mainMenuImage;
	private Texture backgroundTexture;

	private Skin uiSkin;

	public MainMenu(Game game) {
		super(game);
	}

	@Override
	public void loadContent() {
		Log.debug("[MainMenu] loadContent()");

		// Batch = new SpriteBatch();
		backgroundTexture = Resources.createTextureAsset("mainmenu");

		font = Resources.createSharedFontAsset("intro");

		stringTable = LanguagesManager.getInstance(); // "main";
		this.languageName = Settings.getLastLoadedInstance().getLanguage();

		uiSkin = new Skin(Gdx.files.internal("data/skin/uiskin.json"));

		setupBackground();
		setupButtons(uiSkin);

		setupStage();

		theme = Gdx.audio.newMusic(Gdx.files.internal("data/main.ogg"));
		theme.setLooping(true);
		theme.play();
	}

	private void setupStage() {
		stage = new Stage(new StretchViewport(DarkmoorGame.DISPLAY_WIDTH, DarkmoorGame.DISPLAY_WIDTH));

		stage.addActor(mainMenuImage);
		stage.addActor(buttons[0]);
		stage.addActor(buttons[1]);
		stage.addActor(buttons[2]);
		stage.addActor(buttons[3]);

		// stage.

		Gdx.input.setInputProcessor(stage);
	}

	private void setupBackground() {
		mainMenuImage = new Image(backgroundTexture);
		mainMenuImage.setBounds(0, 0, DarkmoorGame.DISPLAY_WIDTH, DarkmoorGame.DISPLAY_WIDTH);
		mainMenuImage.addAction(Actions.fadeIn(2));
		mainMenuImage.setZIndex(0);
	}

	private void setupButtons(Skin uiSkin) {
		int widthUnit = DarkmoorGame.DISPLAY_WIDTH / 6;
		int heightUnit = DarkmoorGame.DISPLAY_HEIGHT / 6;

		buttons = new TextButton[4];

		buttons[0] = new TextButton(stringTable.getString(languageName, "main", 1), uiSkin);// new
																							// ResizableRectangle(158,
																							// 64, 340, 16));
		buttons[0].setBounds(widthUnit, heightUnit << 2, widthUnit, heightUnit);
		buttons[0].setTouchable(Touchable.enabled);
		// buttons[0] = new TextButton("", uiSkin);//new ResizableRectangle(158, 324, 340, 16));
		buttons[0].addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				loadGame = new LoadGameWindow(null, MainMenu.this.uiSkin);// TODO: ntk: give a skin
				return true;
			}

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
			}
		});
		buttons[0].setZIndex(1);

		buttons[1] = new TextButton(stringTable.getString(languageName, "main", 2), uiSkin);// new
																							// ResizableRectangle(158,
																							// 46, 340, 16));
		buttons[1].setBounds(widthUnit, heightUnit << 1, widthUnit, heightUnit);
		buttons[1].setTouchable(Touchable.enabled);
		// buttons[1] = new TextButton("", uiSkin);//new ResizableRectangle(158, 342, 340, 16));
		buttons[1].addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				game.setScreen(new CharGen(game));
				exitScreen();
				return true;
			}

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
			}
		});

		buttons[2] = new TextButton(stringTable.getString(languageName, "main", 3), uiSkin);// new
																							// ResizableRectangle(158,
																							// 27, 340, 16));
		buttons[2].setBounds(widthUnit << 2, heightUnit << 2, widthUnit, heightUnit);
		buttons[2].setTouchable(Touchable.enabled);
		// buttons[2] = new TextButton("", uiSkin);//new ResizableRectangle(158, 360, 340, 16));
		buttons[2].addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				game.setScreen(new OptionMenu(game));
				return true;
			}

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
			}
		});

		buttons[3] = new TextButton(stringTable.getString(languageName, "main", 4), uiSkin);// new
																							// ResizableRectangle(158,
																							// 8, 340, 16));
		buttons[3].setBounds(widthUnit << 2, heightUnit << 1, widthUnit, heightUnit);
		buttons[3].setTouchable(Touchable.enabled);
		// buttons[3] = new TextButton("", uiSkin);//new ResizableRectangle(158, 378, 340, 16));
		buttons[3].addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				DarkmoorGame.exit();
				return true;
			}

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
			}
		});
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

		stage.dispose();
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

		stage.act(Gdx.graphics.getDeltaTime());

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

		if (loadGame != null) {
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

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();

		if (loadGame != null)
			loadGame.draw(batch, 0.5f);

		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		// center the camera: sets stage coordinates 0,0 to bottom left instead of screen center
		stage.getViewport().update(width, height, true);

	}

}
