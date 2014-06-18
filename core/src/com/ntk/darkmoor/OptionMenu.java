package com.ntk.darkmoor;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.ntk.darkmoor.config.InputType;
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

		int widthUnit = DarkmoorGame.DISPLAY_WIDTH / 3;
		int heightUnit = DarkmoorGame.DISPLAY_HEIGHT / 6;
//		uiSkin.getAll(BitmapFont.class)
		buttons = new TextButton[3];

		buttons[0] = new TextButton("Keyboard : " + Settings.getLastLoadedInstance().getInputScheme(), uiSkin);
		buttons[0].setBounds((Gdx.graphics.getWidth() - widthUnit) / 2, heightUnit * 6, widthUnit, heightUnit);
		buttons[0].setTouchable(Touchable.enabled);
		buttons[0].addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (InputType.QWERTY.toString().equalsIgnoreCase(Settings.getLastLoadedInstance().getInputScheme())) {
					Settings.getLastLoadedInstance().setInputScheme(InputType.AZERTY.toString().toLowerCase());
				} else
					Settings.getLastLoadedInstance().setInputScheme(InputType.QWERTY.toString().toLowerCase());

				return true;
			}

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
			}
		});

		buttons[1] = new TextButton("Language : " + Settings.getLastLoadedInstance().getLanguage(), uiSkin);
		buttons[1].setBounds((Gdx.graphics.getWidth() - widthUnit) / 2, heightUnit <<2, widthUnit, heightUnit);
		buttons[1].setTouchable(Touchable.enabled);
		buttons[1].addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				// ntk: warning! it covers only 2 languages!
				if (stringTable.getAvailableLanguages()[0].equals(Settings.getLastLoadedInstance().getLanguage())) {
					Settings.getLastLoadedInstance().setLanguage(stringTable.getAvailableLanguages()[1]);
				} else
					Settings.getLastLoadedInstance().setLanguage(stringTable.getAvailableLanguages()[0]);

				return true;
			}

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
			}
		});

		buttons[2] = new TextButton("Back", uiSkin);
		buttons[2].setBounds((Gdx.graphics.getWidth() - widthUnit) / 2, heightUnit << 1, widthUnit, heightUnit);
		buttons[2].setTouchable(Touchable.enabled);
		buttons[2].addListener(new InputListener() {
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
			buttons[0].setText("Keyboard : " + Settings.getLastLoadedInstance().getInputScheme());
			buttons[1].setText("Language : " + Settings.getLastLoadedInstance().getLanguage());
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
