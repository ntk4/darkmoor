package com.ntk.darkmoor;

import org.ntk.commons.StringUtils;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.ntk.darkmoor.config.LanguagesManager;
import com.ntk.darkmoor.config.Log;
import com.ntk.darkmoor.config.Settings;
import com.ntk.darkmoor.engine.gui.campwindows.LoadGameWindow;
import com.ntk.darkmoor.resource.Resources;
import com.ntk.darkmoor.resource.TextureSet;
import com.ntk.darkmoor.stub.GameScreenBase;

public class MainMenu extends GameScreenBase {

	// private static final int FONT_Y_OFFSET = 25;
	
	private TextureSet textureSet;
	private BitmapFont font;
	private LanguagesManager stringTable;

	private LoadGameWindow loadGame;
	private String languageName;

	private Texture backgroundTexture;

	private SpriteBatch batch;

	public MainMenu(Game game) {
		super(game);
	}

	@Override
	public void loadContent() {
		Log.debug("[MainMenu] loadContent()");

		batch = game.getBatch();

		// Batch = new SpriteBatch();
		backgroundTexture = Resources.createTextureAsset("mainmenu");

		font = Resources.createSharedFontAsset("intro");

		stringTable = LanguagesManager.getInstance(); // "main";
		this.languageName = Settings.getLastLoadedInstance().getLanguage();
		
		super.loadContent();
	}

	@Override
	protected Image setupBackground() {
		Image mainMenuImage = new Image(backgroundTexture);
		mainMenuImage.setBounds(0, 0, DarkmoorGame.DISPLAY_WIDTH, DarkmoorGame.DISPLAY_WIDTH);
//		mainMenuImage.addAction(Actions.fadeIn(2));
//		mainMenuImage.setZIndex(0);
		return mainMenuImage;
	}

	@Override
	protected void setupButtons(Skin uiSkin) {
		int widthUnit = 320;//DarkmoorGame.DISPLAY_WIDTH / 5;
		int heightUnit = 100;//DarkmoorGame.DISPLAY_HEIGHT / 5;

		buttons = new TextButton[4];

		buttons[0] = new TextButton(stringTable.getString(languageName, "main", 1), uiSkin, "transparent-font48");
		buttons[0].setBounds(320, 480, widthUnit, heightUnit);
		buttons[0].addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				loadGame = new LoadGameWindow(null, MainMenu.this.uiSkin, stage);// TODO: ntk: give a skin
				stage.addActor(loadGame);
				return true;
			}
		});

		buttons[1] = new TextButton(stringTable.getString(languageName, "main", 2), uiSkin, "transparent-font48");
		buttons[1].setBounds(340+widthUnit, 480, widthUnit, heightUnit);
		buttons[1].addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				setScreen(new CharGen(game));
				exitScreen();
				return true;
			}
		});

		buttons[2] = new TextButton(stringTable.getString(languageName, "main", 3), uiSkin, "transparent-font48");
		buttons[2].setBounds(320, 330, widthUnit, heightUnit);
		buttons[2].addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				setScreen(new OptionMenu(game));
				return false;
			}
		});

		buttons[3] = new TextButton(stringTable.getString(languageName, "main", 4), uiSkin, "transparent-font48");
		buttons[3].setBounds(340+widthUnit, 330, widthUnit, heightUnit);
		buttons[3].addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				DarkmoorGame.exit();
				return true;
			}
		});
	}

	@Override
	public void unloadContent() {
		super.unloadContent();
		Log.debug("[MainMenu] : unloadContent");

		if (textureSet != null)
			textureSet.dispose();
		textureSet = null;

		Resources.unlockSharedFontAsset(font);
		font = null;


	}

	@Override
	public void update(float delta, boolean hasFocus, boolean isCovered) {
		// No focus byebye
		if (!hasFocus)
			return;

		stage.act(Gdx.graphics.getDeltaTime());

		if (stringTable == null || !StringUtils.equals(Settings.getLastLoadedInstance().getLanguage(), languageName)) {
			// StringTable = new StringTable();
			this.languageName = Settings.getLastLoadedInstance().getLanguage();

			for (int id = 0; id < buttons.length; id++)
				buttons[id].setText(stringTable.getString(languageName, "main", id + 1));
		}

//		if (loadGame != null) {
//			// Load a game
//			loadGame.update(delta);
//
//			// A slot is selected
//			if (loadGame.getSelectedSlot() != -1) {
//				// close window
//				loadGame.close();
//
//				// Run the game
//				GameScreen scr = new GameScreen(game);
//				game.setScreen(scr);
//
//				// Load saved game
//				scr.loadGameSlot(loadGame.getSelectedSlot());
//			}
	}

	@Override
	public void draw(float delta) {
		super.draw(delta);
		batch.begin();

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();

		batch.end();
	}


}
