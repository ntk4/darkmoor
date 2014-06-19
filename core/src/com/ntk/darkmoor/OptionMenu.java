package com.ntk.darkmoor;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.ntk.darkmoor.config.LanguagesManager;
import com.ntk.darkmoor.config.Log;
import com.ntk.darkmoor.config.Settings;
import com.ntk.darkmoor.stub.GameScreenBase;

public class OptionMenu extends GameScreenBase {

	private LanguagesManager stringTable;
	private SpriteBatch batch;

	public OptionMenu(Game game) {
		super(game);
	}

	@Override
	public void loadContent() {
		Log.debug("[OptionMenu] loadContent()");

		batch = game.getBatch();

		stringTable = LanguagesManager.getInstance(); // "main";

		super.loadContent();
	}

	@Override
	protected void setupButtons(Skin uiSkin) {
		super.setupButtons(uiSkin);

		int widthUnit = DarkmoorGame.DISPLAY_WIDTH / 5;
		int heightUnit = DarkmoorGame.DISPLAY_HEIGHT / 6;
		// uiSkin.getAll(BitmapFont.class)
		buttons = new TextButton[6];

		buttons[0] = new TextButton("Music : " + (Settings.getLastLoadedInstance().isMusic() ? "ON" : "OFF"), uiSkin, "transparent-font64");
		buttons[0].setBounds(widthUnit, heightUnit * 6, widthUnit, heightUnit);
		buttons[0].addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Settings.getLastLoadedInstance().setMusic(!Settings.getLastLoadedInstance().isMusic());
				if (Settings.getLastLoadedInstance().isMusic()) {
					game.resumeMusic();
				} else {
					game.pauseMusic();
				}
				return true;
			}
		});

		buttons[1] = new TextButton("Effects are " + (Settings.getLastLoadedInstance().isEffects() ? "ON" : "OFF"),
				uiSkin, "transparent-font64");
		buttons[1].setBounds(widthUnit, heightUnit << 2, widthUnit, heightUnit);
		buttons[1].addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Settings.getLastLoadedInstance().setEffects(!Settings.getLastLoadedInstance().isEffects());
				return true;
			}
		});

		buttons[2] = new TextButton("Full screen is "
				+ (Settings.getLastLoadedInstance().isFullScreen() ? "ON" : "OFF"), uiSkin, "transparent-font64");
		buttons[2].setBounds(widthUnit, heightUnit << 1, widthUnit, heightUnit);
		buttons[2].addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Settings.getLastLoadedInstance().setFullScreen(!Settings.getLastLoadedInstance().isFullScreen());
				return true;
			}
		});

		buttons[3] = new TextButton("HP as bar : " + Settings.getLastLoadedInstance().isHPAsBar(), uiSkin, "transparent-font64");
		buttons[3].setBounds(widthUnit * 3, heightUnit * 6, widthUnit, heightUnit);
		buttons[3].addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Settings.getLastLoadedInstance().setHPAsBar(!Settings.getLastLoadedInstance().isHPAsBar());
				return true;
			}
		});

		buttons[4] = new TextButton("Language : " + Settings.getLastLoadedInstance().getLanguage(), uiSkin, "transparent-font64");
		buttons[4].setBounds(widthUnit * 3, heightUnit << 2, widthUnit, heightUnit);
		buttons[4].addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				// ntk: warning! it covers only 2 languages!
				if (stringTable.getAvailableLanguages()[0].equals(Settings.getLastLoadedInstance().getLanguage())) {
					Settings.getLastLoadedInstance().setLanguage(stringTable.getAvailableLanguages()[1]);
				} else
					Settings.getLastLoadedInstance().setLanguage(stringTable.getAvailableLanguages()[0]);

				return true;
			}
		});

		buttons[5] = new TextButton("Back", uiSkin, "transparent-font64");
		buttons[5].setBounds(widthUnit * 3, heightUnit << 1, widthUnit, heightUnit);
		buttons[5].setTouchable(Touchable.enabled);
		buttons[5].addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				setScreen(game.getMainMenu());

				return true;
			}

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
			}
		});
	}

	@Override
	public void update(float delta, boolean hasFocus, boolean isCovered) {
		super.update(delta, hasFocus, isCovered);
		if (buttons != null) {// buttons[0].getText()
			buttons[0].setText("Music is " + (Settings.getLastLoadedInstance().isMusic() ? "ON" : "OFF"));
			buttons[1].setText("Effects are " + (Settings.getLastLoadedInstance().isEffects() ? "ON" : "OFF"));
			buttons[2].setText("Full screen is " + (Settings.getLastLoadedInstance().isFullScreen() ? "ON" : "OFF"));
			buttons[3].setText("HP as bar : " + (Settings.getLastLoadedInstance().isHPAsBar() ? "ON" : "OFF"));
			buttons[4].setText("Language : " + Settings.getLastLoadedInstance().getLanguage());
		}
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
